package Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Food::class), version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDAO

    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null
        fun getDatabase(context: Context): FoodDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, FoodDatabase::class.java, "foodsDb")
                    // Blocking the main thread is not safe, need to find a safer way to do this
                    .allowMainThreadQueries().build()
            }
            return INSTANCE as FoodDatabase
        }

//        fun getDatabase(context: Context): FoodDatabase {
//            val tempInstance = INSTANCE
//            if(tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    FoodDatabase::class.java,
//                    "Food_Database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
    }
}