package dev.nelmin.minecraft.players

import dev.nelmin.minecraft.NDCore
import dev.nelmin.minecraft.persistence.PersistentProperty
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/**
 * Represents a secured version of NDPlayer, adding functionality for password management.
 * This class manages a hashed password for the player and provides utility methods
 * for password verification and secure password generation.
 *
 * @constructor Creates an instance of NDSecurityPlayer with the associated Bukkit Player.
 * @param bukkitPlayer The Bukkit Player object associated with this NDSecurityPlayer.
 */
open class NDSecurityPlayer(bukkitPlayer: Player) : NDPlayer(bukkitPlayer) {
    /**
     * A private variable that securely stores the hashed password for a player entity.
     *
     * This delegated property uses `PersistentProperty` to persistently store and retrieve
     * the hashed password in a `PersistentDataContainer`. The hashed password is identified
     * within the container using the key `"hashedPassword"` and is stored as a `PersistentDataType.STRING`.
     *
     * The initial value of the hashed password is an empty string. This value is stored in a
     * thread-safe manner, ensuring data consistency during read and write operations.
     */
    private var _hashedPassword: String by PersistentProperty(
        "hashedPassword",
        PersistentDataType.STRING,
        "",
        persistentDataContainer
    )

    /**
     * Represents the hashed version of a user's password.
     *
     * This property uses a secure hashing mechanism to store passwords, ensuring
     * that the actual password value is never stored in plain text. Assigning a
     * value to this variable automatically hashes the password using `NDCore.hashPassword`.
     *
     * The hashed password is used for secure authentication and password verification purposes.
     */
    var hashedPassword: String
        get() = _hashedPassword
        set(value) {
            _hashedPassword = NDCore.hashPassword(value)
        }

    /**
     * Verifies whether the provided plain text password matches the stored hashed password.
     *
     * @param password the plain text password to verify.
     * @return true if the password matches the stored hash, false otherwise.
     */
    fun verifyPassword(password: String): Boolean = NDCore.verifyPassword(password, _hashedPassword)

    /**
     * Generates a secure password with a mix of uppercase letters, lowercase letters, numbers,
     * and special characters. The generated password's length is customizable, and it ensures
     * the inclusion of at least one character from each category for added security.
     *
     * @param length The desired length of the password. Defaults to 16. Must be 4 or greater
     *               to ensure all character categories are included.
     * @param automaticallySetPassword If true, automatically sets the generated password as
     *                                 the hashedPassword for the associated user. Defaults to true.
     * @return The generated secure password as a String.
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