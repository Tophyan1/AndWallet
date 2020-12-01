package hu.bme.aut.andwallet.data

object Wallet {
    var balance = 0

    fun add(amount: Int) {
        balance += amount
    }

    fun remove(amount: Int) {
        balance -= amount
    }

    fun init(transactions: List<Transaction> = listOf(), piggyBank: PiggyBank? = null) {
        balance = 0
        for (transaction in transactions) {
            balance += transaction.amount
        }
        balance -= piggyBank?.progress ?: 0
    }
}