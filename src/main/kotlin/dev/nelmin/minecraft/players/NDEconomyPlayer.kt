package dev.nelmin.minecraft.players

import dev.nelmin.minecraft.NDCore
import dev.nelmin.minecraft.persistence.PersistentProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.KMutableProperty0

/**
 * Represents an economy player within the game, extending from the base player class NDPlayer.
 * This class provides functionality for managing an economy system, including cash and bank balances,
 * as well as transferring funds between players.
 */
open class NDEconomyPlayer(bukkitPlayer: Player) : NDPlayer(bukkitPlayer) {
    /**
     * Represents the cash balance for an instance of `NDEconomyPlayer`.
     *
     * This property is backed by a `PersistentProperty` and stored in the `PersistentDataContainer`,
     * ensuring that the cash value persists across sessions.
     *
     * The cash balance is of type `Double`, initialized with a default value of `0.0`.
     * It is used to manage the player's available currency within the economy system.
     */
    var cash: Double by PersistentProperty(
        "cash",
        PersistentDataType.DOUBLE,
        0.0,
        persistentDataContainer
    )

    /**
     * Represents the bank value associated with an `NDEconomyPlayer`.
     *
     * This property is persisted using a `PersistentDataContainer`, allowing the value
     * to persist across sessions. The type of the value is `Double`, with a default
     * value of `0.0` if no existing value is present in the container. The data is
     * stored using the `PersistentProperty` delegate for efficient management.
     *
     * The `bank` value typically reflects the player's current bank account balance
     * in the economy system. Modifications to this value are automatically updated
     * in the persistent container.
     */
    var bank: Double by PersistentProperty(
        "bank",
        PersistentDataType.DOUBLE,
        0.0,
        persistentDataContainer
    )

    /**
     * Represents the interest rate for an `NDEconomyPlayer`'s account.
     *
     * This property is persisted in a `PersistentDataContainer` and signifies the
     * rate at which interest is applied to the player's balance.
     * The interest rate is stored as a `Double` and defaults to 0.0 if not set.
     */
    var interestRate: Double by PersistentProperty(
        "interestRate",
        PersistentDataType.DOUBLE,
        0.0,
        persistentDataContainer
    )

    /**
     * Transfers a specified amount of money from the source player's balance to the receiver's balance.
     * Ensures that the transfer adheres to constraints such as sufficient funds, valid payment amount,
     * and receiver's balance limits.
     *
     * @param amount The amount of money to transfer.
     * @param receiver The player receiving the money.
     * @param sourceBalance A reference to the source player's balance.
     * @param targetBalance A reference to the receiver player's balance.
     * @return An `EconomyPayResponse` indicating the result of the transfer operation.
     */
    private suspend fun transferMoney(
        amount: Double,
        receiver: NDEconomyPlayer,
        sourceBalance: KMutableProperty0<Double>,
        targetBalance: KMutableProperty0<Double>
    ): EconomyPayResponse = withContext(Dispatchers.Default) {
        if (this === receiver) {
            return@withContext EconomyPayResponse.CANNOT_PAY_SELF
        }

        if (amount <= 0.0) {
            return@withContext EconomyPayResponse.INVALID_PAYMENT_AMOUNT
        }

        if (amount > sourceBalance.get()) {
            return@withContext EconomyPayResponse.INSUFFICIENT_FUNDS
        }

        if (targetBalance.get() == Double.MAX_VALUE) {
            return@withContext EconomyPayResponse.RECEIVER_FULL
        }

        NDCore.mutex.withLock {
            val newReceiverBalance = if (targetBalance.get() + amount > Double.MAX_VALUE) {
                sourceBalance.set(sourceBalance.get() - amount)
                Double.MAX_VALUE
            } else {
                sourceBalance.set(sourceBalance.get() - amount)
                targetBalance.get() + amount
            }
            targetBalance.set(newReceiverBalance)
            EconomyPayResponse.SUCCESS
        }
    }

    /**
     * Transfers a specified amount of cash from the current player to a designated receiver.
     *
     * @param amount The amount of money to transfer. Must be greater than 0.
     * @param receiver The target player to receive the payment.
     * @return An EconomyPayResponse indicating the result of the transaction, such as success or failure reasons.
     */
    suspend fun pay(amount: Double, receiver: NDEconomyPlayer): EconomyPayResponse =
        transferMoney(amount, receiver, ::cash, receiver::cash)

    /**
     * Transfers a specified amount of money from the current player's bank balance to the receiver's bank balance.
     *
     * @param amount The amount of money to be transferred. Must be greater than 0 and less than or equal to the sender's bank balance.
     * @param receiver The recipient of the transfer. Must not be the sender themselves.
     * @return An instance of [EconomyPayResponse] indicating the success or failure of the transfer operation.
     */
    suspend fun wire(amount: Double, receiver: NDEconomyPlayer): EconomyPayResponse =
        transferMoney(amount, receiver, ::bank, receiver::bank)
}

/**
 * Represents the response for an economy payment transaction.
 *
 * This enum provides predefined responses indicating the outcome of the transaction,
 * including whether it succeeded or failed, along with an appropriate message.
 *
 * @property success Indicates whether the payment transaction was successful.
 * @property message Provides a descriptive message about the response.
 */
enum class EconomyPayResponse(val success: Boolean, val message: String) {
    /**
     * Represents a failure response when a user attempts to make a payment to themselves.
     * This response indicates that the operation is not allowed and the payment was not processed.
     *
     * @property success Indicates whether the operation was successful. Always `false` for this response type.
     * @property message Provides a descriptive message explaining the failure reason, "Cannot pay yourself".
     */
    CANNOT_PAY_SELF(success = false, message = "Cannot pay yourself"),

    /**
     * Represents a payment response indicating that the payer has insufficient funds to complete the transaction.
     * This response is unsuccessful.
     */
    INSUFFICIENT_FUNDS(success = false, message = "Insufficient funds"),

    /**
     * Represents a response indicating that the payment amount is invalid.
     *
     * This is used to signify that the provided payment amount does not meet the required
     * conditions for processing, such as being less than or equal to zero.
     */
    INVALID_PAYMENT_AMOUNT(success = false, message = "Invalid payment amount"),

    /**
     * Indicates that the payment could not be completed because the receiver has reached their maximum allowable cash limit.
     *
     * @property success Represents the failure status of the transaction; always `false` in this case.
     * @property message Provides a descriptive message explaining the reason for the failure.
     */
    RECEIVER_FULL(success = false, message = "Receiver has too much cash to accept payment"),

    /**
     * Indicates a successful payment operation.
     *
     * @property success A boolean value representing the success status of the payment operation.
     * @property message A string message conveying additional details about the outcome of the payment.
     */
    SUCCESS(success = true, message = "Payment was accepted")
}