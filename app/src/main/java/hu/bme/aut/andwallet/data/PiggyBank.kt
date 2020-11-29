package hu.bme.aut.andwallet.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "saving")
data class PiggyBank (
    @ColumnInfo(name = "id") @PrimaryKey val id: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val fullPrice:  Int,
    @ColumnInfo(name = "progress") val progress: Int
) : Serializable