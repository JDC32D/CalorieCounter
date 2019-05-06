package com.example.caloriecounter

import Database.Food
import Database.FoodDatabase
import Fragments.AddFoodFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_food_layout.*
import android.view.MenuInflater
import android.view.MenuItem

class MainActivity : AppCompatActivity(), FoodRecyclerAdapter.OnItemClickListener, AddFoodFragment.AddListener {

    private var foodRecyclerView: RecyclerView? = null
    private var recyclerViewAdapter: FoodRecyclerAdapter? = null
    private var viewModel: FoodListViewModel? = null
    private var db: FoodDatabase? = null
    private var foodFragment = supportFragmentManager.findFragmentById(R.id.food_fragment) as AddFoodFragment?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FoodDatabase.getDatabase(this)

        foodRecyclerView = findViewById(R.id.recycler_view)
        recyclerViewAdapter = FoodRecyclerAdapter(arrayListOf(), this)

        // This is unsafe if moving it to a fragment
        foodRecyclerView!!.layoutManager = LinearLayoutManager(this)
        foodRecyclerView!!.adapter = recyclerViewAdapter

        viewModel = ViewModelProviders.of(this).get(FoodListViewModel::class.java)

        viewModel!!.getListFoods().observe(this, Observer { foods ->
            recyclerViewAdapter!!.addFoods(foods!!)
        })

        fab.setOnClickListener {
//            if (foodFragment == null) {
//                foodFragment = AddFoodFragment()
//                AddFoodFragment().show(supportFragmentManager, "TAG")
//            }
            var intent = Intent(applicationContext, FoodDetailsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onItemClick(food: Food) {
        var intent = Intent(applicationContext, FoodDetailsActivity::class.java)
        intent.putExtra("idFood", food.id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete_all_items -> {
                deleteAllFoods()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllFoods() {
        db!!.foodDao().deleteAllFoods()
    }

    override fun addPressed() {
        Log.wtf("ready()", "ready called from fab")
        saveFood()
    }

    private fun saveFood() {
        var nameFood = addName?.text.toString()
        var calFood = addCal?.text.toString()
        Log.wtf("ready()","\nname: $nameFood \ncal: $calFood" )
        var food = Food(0,nameFood,calFood.toInt())
        viewModel?.addFood(food)
    }

}
