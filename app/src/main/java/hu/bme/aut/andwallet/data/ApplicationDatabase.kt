package hu.bme.aut.andwallet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class, PiggyBank::class],  version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun applicationDao(): ApplicationDao

    companion object {
        private var INSTANCE: ApplicationDatabase? = null

        fun getInstance(context: Context) : ApplicationDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    ApplicationDatabase::class.java,
                    "transactions"
                ).build()
            }
            return INSTANCE!!
        }

    }
}