package me.pushkaranand.spendo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.pushkaranand.spendo.db.dao.CategoryDao
import me.pushkaranand.spendo.db.dao.TransactionDao
import me.pushkaranand.spendo.db.entity.Category
import me.pushkaranand.spendo.db.entity.Transaction

@Database(entities = [Transaction::class, Category::class], version = 1)
abstract class SpendoDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {

        @Volatile
        private var INSTANCE: SpendoDatabase? = null

        internal fun getDatabase(context: Context): SpendoDatabase? {
            if (INSTANCE == null) {
                synchronized(SpendoDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SpendoDatabase::class.java, "spendo.db"
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }

}
