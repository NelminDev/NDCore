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

/**
 * Represents a message that can be translated into multiple languages.
 */
public class LocalizedMessage {
    private final Map<String, String> translations = new HashMap<>();
    @Getter
    private LanguageCode fallbackLanguageCode = LanguageCode.ENGLISH;
    @Getter
    private String fallbackMessage = "Sorry, no translation found for this message.";

    /**
     * Sets the fallback language code to use when a translation is not found.
     *
     * @param languageCode the language code to use as fallback
     * @return this LocalizedMessage instance for chaining
     */
    public LocalizedMessage fallbackLanguageCode(@NotNull LanguageCode languageCode) {
        this.fallbackLanguageCode = Objects.requireNonNull(languageCode, "languageCode must not be null");
        return this;
    }

    /**
     * Sets the fallback message to use when a translation is not found.
     *
     * @param fallbackMessage the message to use as fallback
     * @return this LocalizedMessage instance for chaining
     */
    public LocalizedMessage fallbackMessage(@NotNull String fallbackMessage) {
        this.fallbackMessage = Objects.requireNonNull(fallbackMessage, "fallbackMessage must not be null");
        return this;
    }

    /**
     * Gets the translation for the specified language code.
     * Returns the fallback message if no translation is found.
     *
     * @param languageCode the language code to get the translation for
     * @return the translated message or the fallback message
     */
    public String getTranslation(@NotNull LanguageCode languageCode) {
        Objects.requireNonNull(languageCode, "languageCode must not be null");
        return Objects.requireNonNullElseGet(this.translations.get(languageCode.get()), () -> this.fallbackMessage);
    }

    /**
     * Adds a translation for the specified language code.
     *
     * @param languageCode the language code to add the translation for
     * @param message      the translated message
     * @return this LocalizedMessage instance for chaining
     */
    public LocalizedMessage add(@NotNull LanguageCode languageCode, @NotNull String message) {
        Objects.requireNonNull(languageCode, "languageCode must not be null");
        Objects.requireNonNull(message, "message must not be null");
        this.translations.put(languageCode.get(), message);
        return this;
    }

    /**
     * Removes the translation for the specified language code.
     *
     * @param languageCode the language code to remove the translation for
     * @return this LocalizedMessage instance for chaining
     */
    public LocalizedMessage remove(@NotNull LanguageCode languageCode) {
        Objects.requireNonNull(languageCode, "languageCode must not be null");
        this.translations.remove(languageCode.get());
        return this;
    }

    /**
     * Checks if a translation exists for the specified language code.
     *
     * @param languageCode the language code to check for
     * @return true if a translation exists, false otherwise
     */
    public boolean hasTranslation(@NotNull LanguageCode languageCode) {
        Objects.requireNonNull(languageCode, "languageCode must not be null");
        return translations.containsKey(languageCode.get());
    }

    /**
     * Loads a translation from a YAML file.
     *
     * @param languageCode the language code of the translation
     * @param key          the key in the YAML file containing the translation
     * @param path         the path to the directory containing the YAML file
     * @param fileFormat   the format of the filename, with {lang} as placeholder for the language code
     * @param force        true to overwrite existing translation, false to throw exception if translation exists
     * @return this LocalizedMessage instance for chaining
     * @throws IOException                          if the file cannot be read
     * @throws InvalidConfigurationException        if the file is not valid YAML
     * @throws ConfigurationKeyNotFoundException    if the key is not found in the file
     * @throws TranslationAlreadyAvailableException if a translation already exists and force is false
     */
    public LocalizedMessage getFromYamlFile(@NotNull LanguageCode languageCode, @NotNull String key, @NotNull Path path,
                                            @NotNull String fileFormat, boolean force) throws IOException, InvalidConfigurationException,
            ConfigurationKeyNotFoundException, TranslationAlreadyAvailableException {
        Objects.requireNonNull(languageCode, "languageCode must not be null");
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(path, "path must not be null");
        Objects.requireNonNull(fileFormat, "fileFormat must not be null");

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

    /**
     * Updates or adds a translation.
     *
     * @param languageCode the language code to update
     * @param message      the translated message
     * @param force        true to overwrite existing translation, false to throw exception if translation exists
     * @throws TranslationAlreadyAvailableException if a translation already exists and force is false
     */
    private void updateTranslation(@NotNull LanguageCode languageCode, @NotNull String message, boolean force)
            throws TranslationAlreadyAvailableException {
        Objects.requireNonNull(languageCode, "languageCode must not be null");
        Objects.requireNonNull(message, "message must not be null");

        if (!force) {
            if (this.translations.containsKey(languageCode.get())) {
                throw new TranslationAlreadyAvailableException("A translation for this message in the Language " + languageCode.get() + " is already available!");
            }
            this.translations.put(languageCode.get(), message);
            return;
        }

        this.translations.remove(languageCode.get());
        this.translations.put(languageCode.get(), message);
    }
}