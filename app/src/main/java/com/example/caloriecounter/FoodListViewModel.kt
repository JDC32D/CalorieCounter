package com.example.caloriecounter

import Database.Food
import Database.FoodDatabase
import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FoodListViewModel(app: Application) : AndroidViewModel(app) {

    var listFood: LiveData<List<Food>>
    var listCals: LiveData<List<Int>>

    private val appDb: FoodDatabase

    init {
        appDb = FoodDatabase.getDatabase(this.getApplication())
        listFood = appDb.foodDao().getAll()
        listCals = appDb.foodDao().getAllCalories()
    }

    fun getListFoods(): LiveData<List<Food>> {
        return listFood
    }

    fun getAllCals(): LiveData<List<Int>> {
        return listCals
    }

    fun addFood(food: Food) {
        AddAsynTask(appDb).execute(food)
    }

    class AddAsynTask(db: FoodDatabase) : AsyncTask<Food, Void, Void>() {
        private var foodDb = db
        override fun doInBackground(vararg params: Food): Void? {
            foodDb.foodDao().insert(params[0]) // Maybe make insert take only one food
            return null
        }
    }
}