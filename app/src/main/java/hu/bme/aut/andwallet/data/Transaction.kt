package hu.bme.aut.andwallet.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable
import java.util.*

@Entity(tableName = "transaction")
@TypeConverters(DateConverter::class)
data class Transaction(
    @ColumnInfo(name = "ID") @PrimaryKey val id: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "date") val date: Date
) : Serializable