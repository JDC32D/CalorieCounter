package com.example.caloriecounter

import Database.Food
import Database.FoodDAO
import Database.FoodDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.add_food_layout.*

class FoodDetailsActivity : AppCompatActivity() {

    private var foodDao: FoodDAO? = null
    private var viewModel: FoodListViewModel? = null
    private var currentFood: Int? = null
    private var food: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_food_layout)
        val db: FoodDatabase = FoodDatabase.getDatabase(this)
        foodDao = db.foodDao()
        viewModel = ViewModelProviders.of(this).get(FoodListViewModel::class.java)

        currentFood = intent.getIntExtra("idFood", -1)
        if (currentFood != -1) {
            setTitle(R.string.editFood)
            food = foodDao!!.getFoodById(currentFood!!)
            addName.setText(food!!.name)
            addCal.setText(food!!.calories.toString())
        } else {
            setTitle(R.string.addFood)
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.wtf("OptionsSelected","${item!!.itemId}")
        when (item.itemId) {
            R.id.done_item -> {
                Log.wtf("Details->OptionSelected","${R.id.done_item}")
                if (currentFood == -1) {
                    saveFood()
                    Toast.makeText(this, getString(R.string.saveFood), Toast.LENGTH_SHORT).show()
                } else {
                    updateFood()
                    Toast.makeText(this, getString(R.string.updatedFood), Toast.LENGTH_SHORT).show()
                }
                finish()
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
            }
            R.id.delete_item -> {
                deleteFood()
                Toast.makeText(this, getString(R.string.deleteFood), Toast.LENGTH_SHORT).show()
                finish()
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        if (currentFood == -1) {
            menu.findItem(R.id.delete_item).isVisible = false
        }
        return true
    }

    private fun saveFood() {
        val nameFood = addName.text.toString()
        val calFood = addCal.text.toString()
        val food = Food(0, nameFood, calFood.toInt())
        viewModel!!.addFood(food)
    }

    private fun deleteFood() {
        foodDao!!.delete(food!!)
    }

    private fun updateFood() {
        val nameFood = addName.text.toString()
        val calFood = addCal.text.toString()
        val contact = Food(food!!.id, nameFood, calFood.toInt())
        foodDao!!.update(contact)
    }
}