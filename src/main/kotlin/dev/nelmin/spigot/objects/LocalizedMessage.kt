package dev.nelmin.spigot.objects

/**
 * Represents a localized message system that manages translations for different language codes.
 * Provides functionality to define translations, fallback settings, and retrieve messages.
 */
class LocalizedMessage {
    /**
     * A mutable map that stores localized message translations with their corresponding language codes.
     * Keys represent language codes, and values represent the translated messages.
     *
     * This map is utilized to retrieve or store translations, check for the existence of a translation
     * for a specific language code, or apply fallback mechanisms when a translation is not available.
     */
    private val messages: MutableMap<String, String> = mutableMapOf()

    /**
     * Holds the default fallback language code used when no translation is
     * available for a specific language. By default, this value is set to "en".
     * It can be modified or accessed via dedicated methods in the containing class.
     */
    private var fallbackLanguageCode: String = "en"

    /**
     * Holds the default fallback message to be used when no translation is available
     * for the requested language code.
     *
     * This message is returned as the default value when a translation is missing
     * from the collection of localized messages.
     */
    private var fallbackMessage: String = "Sorry, no translation found for this message."

    /**
     * Sets the fallback language code for the localized messages.
     *
     * @param languageCode The language code to set as the fallback.
     * @return The updated instance of LocalizedMessage.
     */
    fun fallbackLanguageCode(languageCode: String): LocalizedMessage {
        this.fallbackLanguageCode = languageCode
        return this
    }

    /**
     * Retrieves the fallback language code that is used when a localized message
     * translation is not found for a specified language.
     *
     * @return The fallback language code as a string.
     */
    fun fallbackLanguageCode(): String = fallbackLanguageCode

    /**
     * Sets the fallback message to be used if no translation is found.
     *
     * @param message the fallback message to set
     * @return the instance of the current `LocalizedMessage` object
     */
    fun fallbackMessage(message: String): LocalizedMessage {
        this.fallbackMessage = message
        return this
    }

    /**
     * Retrieves the fallback message that is used when no translation is found for the requested message.
     *
     * @return The fallback message as a string.
     */
    fun fallbackMessage(): String = fallbackMessage

    /**
     * Retrieves a localized message based on the given language code.
     *
     * If a message corresponding to the specified language code is not found,
     * the method returns the message associated with the fallback language code.
     *
     * @param languageCode The language code used to lookup the localized message.
     * @return The localized message corresponding to the language code,
     *         or the message for the fallback language code if the specified code is not available.
     *         Returns null if no messages are defined.
     */
    operator fun get(languageCode: String): String? = messages[languageCode] ?: messages[fallbackLanguageCode]

    /**
     * Sets a localized message for the specified language code.
     *
     * @param languageCode The language code for which the message is being set.
     * @param message The message in the specified language.
     * @return The current instance of LocalizedMessage, allowing method chaining.
     */
    operator fun set(languageCode: String, message: String): LocalizedMessage {
        messages[languageCode] = message
        return this
    }

    /**
     * Checks whether a translation exists for the specified language code.
     *
     * @param languageCode the language code to check for an existing translation.
     * @return true if a translation exists for the given language code, false otherwise.
     */
    fun hasTranslation(languageCode: String): Boolean = messages.containsKey(languageCode)
}