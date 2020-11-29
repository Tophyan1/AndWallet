package hu.bme.aut.andwallet.data

import androidx.room.*

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM `transaction`")
    fun getAll(): List<Transaction>

    @Insert
    fun insert(transactions: Transaction): Long

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun deleteItem(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    fun deleteAllItems()

    @Query("SELECT * FROM `saving` LIMIT 1")
    fun getPiggyBank(): PiggyBank?

    @Update
    fun updatePiggyBank(piggyBank: PiggyBank)

    @Insert
    fun insertPiggyBank(piggyBank: PiggyBank)
}