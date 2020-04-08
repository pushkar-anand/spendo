package me.pushkaranand.spendo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                            SpendoDatabase::class.java,
                            "spendo.db"
                        )
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    populateCategories(INSTANCE!!.categoryDao())
                }
            }
        }

        private suspend fun populateCategories(categoryDao: CategoryDao) {
            val categories = getPreDefinedCategories()
            categories.forEach {
                categoryDao.insert(it)
            }
        }


        private fun getPreDefinedCategories(): List<Category> {
            val categories = arrayListOf<Category>()
            categories.add(Category(0, name = "Food and Dining", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Entertainment", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Transportation", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Stationary", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Rations", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Shopping", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Bills and Utilities", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Gifts and Donation", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Health and Fitness", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Salary", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(0, name = "Others", spend = 0.0, spendLimit = 0.0))
            return categories
        }
    }

}
