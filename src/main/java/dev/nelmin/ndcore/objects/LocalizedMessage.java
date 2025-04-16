package dev.nelmin.ndcore.objects;

import dev.nelmin.ndcore.exceptions.ConfigurationKeyNotFoundException;
import dev.nelmin.ndcore.exceptions.TranslationAlreadyAvailableException;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LocalizedMessage {
    private final Map<String, String> translations = new HashMap<>();
    @Getter
    private LanguageCode fallbackLanguageCode = LanguageCode.ENGLISH;
    @Getter
    private String fallbackMessage = "Sorry, no translation found for this message.";

    public LocalizedMessage fallbackLanguageCode(LanguageCode languageCode) {
        this.fallbackLanguageCode = languageCode;
        return this;
    }

    public LocalizedMessage fallbackMessage(String fallbackMessage) {
        this.fallbackMessage = fallbackMessage;
        return this;
    }

    public String getTranslation(LanguageCode languageCode) {
        return Objects.requireNonNullElseGet(this.translations.get(languageCode.get()), () -> this.fallbackMessage);
    }

    public LocalizedMessage add(@NotNull LanguageCode languageCode, @NotNull String message) {
        this.translations.put(languageCode.get(), message);
        return this;
    }

    public LocalizedMessage remove(@NotNull LanguageCode languageCode) {
        this.translations.remove(languageCode.get());
        return this;
    }

    public boolean hasTranslation(LanguageCode languageCode) {
        return translations.containsKey(languageCode.get());
    }

    public LocalizedMessage getFromYamlFile(LanguageCode languageCode, String key, Path path, String fileFormat, boolean force) throws IOException, InvalidConfigurationException, ConfigurationKeyNotFoundException, TranslationAlreadyAvailableException {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        File file = path.resolve(fileFormat.replace("{lang}", languageCode.get())).toFile();
        yamlConfiguration.load(file);
        String message = yamlConfiguration.getString(key);

        if (null == message) {
            throw new ConfigurationKeyNotFoundException("The configuration key " + key + " was not found in the file " + file.getAbsolutePath());
        }

        updateTranslation(languageCode, message, force);

        return this;
    }

    private void updateTranslation(LanguageCode languageCode, String message, boolean force) throws TranslationAlreadyAvailableException {
        if (!force) {
            if (this.translations.containsKey(languageCode.get())) {
                throw new TranslationAlreadyAvailableException("An translation for this message in the Language " + languageCode.get() + " is already available!");
            }
            this.translations.put(languageCode.get(), message);
            return;
        }

        this.translations.remove(languageCode.get());
        this.translations.put(languageCode.get(), message);
    }
}
