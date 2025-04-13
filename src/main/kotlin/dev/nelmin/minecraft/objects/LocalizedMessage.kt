package dev.nelmin.minecraft.objects

import org.bukkit.configuration.file.YamlConfiguration
import java.nio.file.Path

/**
 * This class provides a mechanism for handling localized messages with optional fallback configurations.
 * It supports setting translations for different language codes, retrieving localized messages,
 * and specifying fallback behaviors for languages or messages not found.
 */
class LocalizedMessage {
    /**
     * Stores a map of localized messages where the key represents the language code
     * and the value is the message in the corresponding language. This serves as the
     * primary data structure for managing translations within the `LocalizedMessage` class.
     */
    private val messages: MutableMap<String, String> = mutableMapOf()

    /**
     * The language code used as a fallback when a message translation for the requested language is not available.
     * Defaults to "en" (English).
     */
    private var fallbackLanguageCode: String = "en"

    /**
     * The default message used as a fallback when no translation is found for a given key.
     * This message is utilized in scenarios where the requested translation or the specified language code
     * is unavailable within the localized message repository.
     */
    private var fallbackMessage: String = "Sorry, no translation found for this message."

    /**
     * Sets the fallback language code to be used when a message translation is unavailable.
     *
     * @param languageCode The language code to set as the fallback language. For example, "en" for English.
     * @return The current instance of [LocalizedMessage], allowing for method chaining.
     */
    fun fallbackLanguageCode(languageCode: String): LocalizedMessage {
        this.fallbackLanguageCode = languageCode
        return this
    }

    /**
     * Retrieves the fallback language code currently set for this instance.
     *
     * @return The fallback language code as a string.
     */
    fun fallbackLanguageCode(): String = fallbackLanguageCode

    /**
     * Sets the fallback message to be used when no translation is found.
     *
     * @param message the fallback message to be used.
     * @return the current instance of the LocalizedMessage class.
     */
    fun fallbackMessage(message: String): LocalizedMessage {
        this.fallbackMessage = message
        return this
    }

    /**
     * Provides the default fallback message returned when no translation is found.
     *
     * @return The fallback message as a string.
     */
    fun fallbackMessage(): String = fallbackMessage

    /**
     * Retrieves a localized message corresponding to the given language code.
     * If no message is found for the specified language code, the message
     * associated with the fallback language code is returned.
     *
     * @param languageCode The language code to look up the message for.
     * @return The localized message for the specified language code, or the
     *         message for the fallback language code if no match is found. Returns
     *         null if neither message exists.
     */
    operator fun get(languageCode: String): String? = messages[languageCode] ?: messages[fallbackLanguageCode]

    /**
     * Adds or updates a localized message for the specified language code.
     *
     * @param languageCode The language code for which the message is being set.
     * @param message The localized message to be associated with the given language code.
     * @return The instance of [LocalizedMessage], allowing chaining of method calls.
     */
    operator fun set(languageCode: String, message: String): LocalizedMessage {
        messages[languageCode] = message
        return this
    }

    /**
     * Checks if a translation exists for the specified language code.
     *
     * @param languageCode the language code to check for a corresponding translation.
     * @return true if a translation exists for the given language code, false otherwise.
     */
    fun hasTranslation(languageCode: String): Boolean = messages.containsKey(languageCode)

    /**
     * Retrieves a specific value from a YAML file based on the provided key and language code.
     *
     * @param key The key to look for in the YAML file.
     * @param languageCode The language code to replace in the file name pattern.
     * @param path The base path where the YAML file is located.
     * @param fileFormat The file name pattern for the YAML file, where "%lang" will be replaced with the language code. Defaults to "message-%lang.yml".
     * @return The value associated with the specified key in the YAML file, or the fallback message if the key is not found.
     */
    fun getYamlFromFile(
        key: String,
        languageCode: String,
        path: Path,
        fileFormat: String = "message-%lang.yml"
    ): String? {
        val yamlConfiguration =
            YamlConfiguration().apply { load(path.resolve(fileFormat.replace("%lang", languageCode)).toFile()) }
        return yamlConfiguration.getString(key) ?: fallbackMessage
    }
}