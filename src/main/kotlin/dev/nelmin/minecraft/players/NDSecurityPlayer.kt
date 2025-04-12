package dev.nelmin.minecraft.players

import dev.nelmin.minecraft.NDCore
import dev.nelmin.minecraft.persistence.PersistentProperty
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/**
 * Represents a secure player in the context of the Nucleoid security system. Extends the base NDPlayer functionality
 * by providing features for handling hashed passwords and password verification.
 *
 * This class is designed to store and manage password data securely, using hashed passwords
 * and providing utilities for password generation and verification.
 *
 * @constructor Creates a new instance of NDSecurityPlayer.
 * @param bukkitPlayer The associated Bukkit Player instance.
 */
open class NDSecurityPlayer(bukkitPlayer: Player) : NDPlayer(bukkitPlayer) {
    /**
     * Represents a securely stored password for the `NDSecurityPlayer`.
     *
     * This variable is a delegated property using `PersistentProperty`,
     * allowing the password to persist across sessions by storing it in
     * a `PersistentDataContainer`. The password is stored in a hashed
     * format to enhance security.
     *
     * The underlying data key for this property is `hashedPassword`, and
     * it is stored as a `PersistentDataType.STRING`.
     *
     * This variable should only be modified through the higher-level
     * operations defined in the `hashedPassword` property or by specifically
     * interacting with the associated container for advanced use cases.
     *
     * Default behavior initializes the password as an empty string if no
     * value is currently present in the container.
     */
    private var _hashedPassword: String by PersistentProperty(
        "hashedPassword",
        PersistentDataType.STRING,
        "",
        persistentDataContainer
    )

    /**
     * Represents the hashed version of a user's password.
     * Setting this property automatically hashes the provided password
     * using the `NDCore.hashPassword` method.
     * The hashed password is securely stored and used for verifying user authentication.
     */
    var hashedPassword: String
        get() = _hashedPassword
        set(value) {
            _hashedPassword = NDCore.hashPassword(value)
        }

    /**
     * Verifies if the provided password matches the stored hashed password.
     *
     * @param password The plain text password provided for verification.
     * @return true if the provided password matches the stored hash, false otherwise.
     */
    fun verifyPassword(password: String): Boolean = NDCore.verifyPassword(password, _hashedPassword)

    /**
     * Generates a secure password containing at least one uppercase letter, one lowercase letter, one digit,
     * and one special character. The remaining characters are randomly selected from all allowed character types.
     *
     * @param length The total length of the generated password. Defaults to 16 if not specified.
     * @param automaticallySetPassword A flag indicating whether the generated password should be
     * automatically hashed and set as the user's password. Defaults to true.
     * @return A randomly generated secure password as a string.
     */
    fun generateSecurePassword(length: Int = 16, automaticallySetPassword: Boolean = true): String {
        val upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lowerCase = "abcdefghijklmnopqrstuvwxyz"
        val numbers = "0123456789"
        val specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?"

        val password = StringBuilder()
        password.append(upperCase.random())
        password.append(lowerCase.random())
        password.append(numbers.random())
        password.append(specialChars.random())

        val allChars = upperCase + lowerCase + numbers + specialChars

        repeat(length - 4) {
            password.append(allChars.random())
        }

        val randomPassword = password.toString().toList().shuffled().joinToString("")

        if (automaticallySetPassword) hashedPassword = randomPassword
        return randomPassword
    }
}