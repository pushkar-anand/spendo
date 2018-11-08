package me.pushkaranand.spendo.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
                super.onOpen(db)
                PopulateDBAsync(INSTANCE).execute()
            }
        }

        private fun getPreDefinedCategories(): List<Category> {
            val categories = arrayListOf<Category>()
            categories.add(Category(name = "Food and Dining", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Entertainment", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Transportation", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Stationary", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Rations", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Shopping", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Bills and Utilities", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Gifts and Donation", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Health and Fitness", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Salary", spend = 0.0, spendLimit = 0.0))
            categories.add(Category(name = "Others", spend = 0.0, spendLimit = 0.0))
            return categories
        }

        internal class PopulateDBAsync(spendoDatabase: SpendoDatabase?) : AsyncTask<Void, Void, Void>() {

            private val categoryDao: CategoryDao? = spendoDatabase?.categoryDao()

            override fun doInBackground(vararg params: Void?): Void? {

                categoryDao?.deleteAll()
                val categories = getPreDefinedCategories()
                for (category in categories) {
                    categoryDao?.newCategory(category)
                }
                return null
            }
        }
    }

}
