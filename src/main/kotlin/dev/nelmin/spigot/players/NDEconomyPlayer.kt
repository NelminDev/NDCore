package dev.nelmin.spigot.players

import dev.nelmin.spigot.NDCore
import dev.nelmin.spigot.persistence.PersistentProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.KMutableProperty0

/**
 * Represents an economy-centric player that extends `NDPlayer` functionality.
 * It incorporates additional economic properties and actions relevant for managing
 * in-game currency via cash and bank balances.
 *
 * This class leverages persistent data storage to store and retrieve economic properties for the player,
 * ensuring all data is persistent and survives across sessions.
 *
 * @constructor Initializes an `NDEconomyPlayer` by wrapping a Bukkit `Player` instance.
 * @param bukkitPlayer The wrapped Bukkit `Player` instance providing base player functionality.
 */
open class NDEconomyPlayer(bukkitPlayer: Player) : NDPlayer(bukkitPlayer) {
    /**
     * Represents the in-hand cash balance of the player.
     *
     * This property stores the amount of cash a player holds outside of their bank account.
     * The value is persisted using the provided `PersistentDataContainer` and can be accessed
     * or modified through the delegated property mechanism.
     *
     * Default Value: 0.0
     *
     * The `cash` property is thread-safe and ensures consistent updates and retrievals
     * through the use of mutex locks during operations.
     */
    var cash: Double by PersistentProperty(
        "cash",
        PersistentDataType.DOUBLE,
        0.0,
        persistentDataContainer
    )

    /**
     * Represents the player's bank balance stored persistently using a `PersistentDataContainer`.
     *
     * The bank balance is managed persistently, allowing the value to be saved and loaded across server sessions.
     * It utilizes a `PersistentProperty` delegation to link the player's bank balance to a specific namespaced key
     * and datatype within the persistent data storage.
     *
     * Default value: `0.0`
     * Storage type: `PersistentDataType.DOUBLE`
     *
     * This property is thread-safe and allows concurrent access through a mutex lock mechanism, ensuring data consistency.
     */
    var bank: Double by PersistentProperty(
        "bank",
        PersistentDataType.DOUBLE,
        0.0,
        persistentDataContainer
    )

    /**
     * Represents the interest rate associated with an economy player.
     *
     * This property is persistently stored and managed through a `PersistentDataContainer`.
     * It is initialized with a default value of `0.0` and dynamically retrieves or updates its value
     * from persistent storage as needed. The interest rate can be used to calculate earnings or payments
     * related to banking or other economic activities.
     *
     * The underlying data type for this property is `Double`, and it ensures that the value is safely
     * synchronized using thread-safe mechanisms during retrieval or assignment.
     */
    var interestRate: Double by PersistentProperty(
        "interestRate",
        PersistentDataType.DOUBLE,
        0.0,
        persistentDataContainer
    )

    /**
     * Transfers money from the source player to the receiver player while updating their respective balances.
     * Various validations are performed, such as ensuring the amount is positive, sufficient funds are available,
     * and the receiver's balance does not exceed the maximum limit.
     *
     * @param amount The amount of money to transfer. It must be a positive value.
     * @param receiver The player receiving the money. Must not be the same as the source player.
     * @param sourceBalance A mutable reference to the source player's balance, which will be decremented by the transfer amount.
     * @param targetBalance A mutable reference to the receiver player's balance, which will be incremented by the transfer amount.
     * @return An `EconomyPayResponse` indicating the result of the operation, which could be success, insufficient funds, invalid amount, etc.
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
     * Processes a payment from this player to a specified receiver.
     *
     * This method attempts to transfer the specified amount of cash from the caller's
     * account to the receiver's account. Various checks are performed to ensure
     * the validity and feasibility of the transaction, such as ensuring the
     * payment amount is positive, the payer has sufficient funds, and the receiver
     * can accommodate the transfer.
     *
     * @param amount The amount of cash to be transferred to the receiver. Must be a positive value.
     * @param receiver The player to whom the payment is being made.
     * @return An instance of `EconomyPayResponse` indicating the result of the payment operation.
     */
    suspend fun pay(amount: Double, receiver: NDEconomyPlayer): EconomyPayResponse =
        transferMoney(amount, receiver, ::cash, receiver::cash)

    /**
     * Transfers a specified amount of money from the current player's bank balance to another player's bank balance.
     *
     * This method attempts to wire the specified amount to the receiver's bank.
     * It validates the transaction before processing, ensuring conditions such as sufficient funds
     * and valid payment amounts are met. The outcome of the operation is returned as an `EconomyPayResponse`.
     *
     * @param amount The amount of money to transfer. Must be greater than zero.
     * @param receiver The player who will receive the money.
     * @return An `EconomyPayResponse` representing the result of the operation,
     * which may indicate success, insufficiency of funds, invalid payment amounts,
     * or other conditions related to the transaction.
     */
    suspend fun wire(amount: Double, receiver: NDEconomyPlayer): EconomyPayResponse =
        transferMoney(amount, receiver, ::bank, receiver::bank)
}

/**
 * Represents the result of an economy payment operation between two players.
 *
 * This enum encapsulates potential outcomes of the payment process, providing a
 * boolean value to indicate success and a descriptive message explaining the result.
 */
enum class EconomyPayResponse(val success: Boolean, val message: String) {
    /**
     * Represents a response indicating that a payment operation cannot be performed
     * because the payer attempted to pay themselves.
     *
     * This response is used in scenarios where a player tries to transfer money to their
     * own account, which is regarded as an invalid transaction in this system.
     *
     * @property success Always false for this response type, as the operation is not allowed.
     * @property message A string describing the reason for the rejection: "Cannot pay yourself".
     */
    CANNOT_PAY_SELF(success = false, message = "Cannot pay yourself"),
    /**
     * Represents a response indicating that a payment operation has failed due to insufficient funds.
     *
     * This status is used in scenarios where the payer does not have enough balance
     * to cover the payment amount. It ensures that the transaction is halted and
     * provides feedback about the reason for failure.
     *
     * @property success Indicates the success status of the operation, always `false` for this response type.
     * @property message Provides a descriptive message, "Insufficient funds", explaining the failure reason.
     */
    INSUFFICIENT_FUNDS(success = false, message = "Insufficient funds"),
    /**
     * Represents a failure response when a payment amount is invalid.
     *
     * This response is returned when an attempt is made to process a payment with an amount
     * that is less than or equal to zero. It ensures that only valid, positive amounts can be
     * used for payment transactions, preventing invalid operations.
     */
    INVALID_PAYMENT_AMOUNT(success = false, message = "Invalid payment amount"),
    /**
     * Indicates that the payment cannot be completed because the receiver has reached the maximum
     * allowable cash limit. This response is returned when attempting to transfer funds to a player
     * whose cash balance cannot accommodate the additional amount without exceeding the maximum threshold.
     *
     * @property success Always `false` for this status, as the payment attempt is unsuccessful.
     * @property message Describes the reason for the failure: "Receiver has too much cash to accept payment".
     */
    RECEIVER_FULL(success = false, message = "Receiver has too much cash to accept payment"),
    /**
     * Represents a successful payment response in the economy system.
     *
     * This response is returned when a payment is processed successfully
     * with no errors or issues. The associated message indicates that
     * the payment was accepted.
     *
     * @property success Indicates that the payment operation was successful.
     * @property message Describes the result of the operation, specifying that
     * the payment was accepted.
     */
    SUCCESS(success = true, message = "Payment was accepted")
}