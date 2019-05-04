package Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "Name") var name: String,
    @ColumnInfo(name = "Calories") var calories: Int,
    @ColumnInfo(name = "Fat") var fat: Int?,
    @ColumnInfo(name = "Cholesterol") var cholesterol: Int?,
    @ColumnInfo(name = "Sodium") var sodium: Int? ,
    @ColumnInfo(name = "Carbohydrate") var carbohydrate: Int?,
    @ColumnInfo(name = "Protein") var protein: Int?
    //@ColumnInfo(name = "Meal") var meal: Meal
)