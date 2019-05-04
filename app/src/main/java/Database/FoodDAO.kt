package Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FoodDAO {

    @Query("SELECT * FROM food_table")
    fun getAll(): LiveData<List<Food>>

    @Query("SELECT * FROM food_table WHERE name LIKE :name")
    fun findByName(name: String): LiveData<List<Food>>

   // @Query("SELECT * FROM food_table WHERE meal IS Breakfast")

    //vararg -> Variable number of arguments
    // https://kotlinlang.org/docs/reference/functions.html#variable-number-of-arguments-varargs
    @Insert
    fun insert(vararg foods: Food)

    @Delete
    fun delete(food: Food)

    @Update
    fun update(vararg foods: Food)

}