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
import android.R.attr.start
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation.ArgbEvaluator
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list.view.*
import androidx.core.os.HandlerCompat.postDelayed
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.annotation.ContentView
import androidx.core.animation.doOnEnd


class MainActivity : AppCompatActivity(), FoodRecyclerAdapter.OnItemClickListener, AddFoodFragment.AddListener {

    private var foodRecyclerView: RecyclerView? = null
    private var recyclerViewAdapter: FoodRecyclerAdapter? = null
    private var viewModel: FoodListViewModel? = null
    private var db: FoodDatabase? = null
    private var totalCals: Int = 0

    // I decided to have the add_food be done in an activity, this fragment currently does nothing
    private var foodFragment = supportFragmentManager.findFragmentById(R.id.add_food) as AddFoodFragment?

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

        viewModel!!.getAllCals().observe(this, Observer { cal ->
            Log.wtf("cal", "$cal")
            totalCals = 0
            cal.forEach { cal ->
                totalCals += cal
            }
            calsChanged()
        })

        fab.setOnClickListener {

            val colorFrom = R.color.red
            val colorTo = R.color.navy
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 50 // milliseconds
            colorAnimation.addUpdateListener { animator -> fab.setBackgroundColor(animator.animatedValue as Int) }
            colorAnimation.start()

            // I don't think this is blocking, is it?
            val intent = Intent(applicationContext, FoodDetailsActivity::class.java)
            colorAnimation.doOnEnd {
                startActivity(intent)
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
            }

        }

    }

    override fun onItemClick(food: Food, view: View) {

        val intent = Intent(applicationContext, FoodDetailsActivity::class.java)
        intent.putExtra("idFood", food.id)
        startActivity(intent)
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out)

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

    private fun calsChanged() {
        totalCal.text = getString(R.string.dynamicTotalCal, totalCals.toString())
    }

    private fun deleteAllFoods() {
        db!!.foodDao().deleteAllFoods()
    }

    override fun addPressed() {
        Log.wtf("ready()", "ready called from fab")
        saveFood()
    }

    private fun saveFood() {
        val nameFood = addName?.text.toString()
        val calFood = addCal?.text.toString()
        Log.wtf("ready()","\nname: $nameFood \ncal: $calFood" )
        val food = Food(0,nameFood,calFood.toInt())
        viewModel?.addFood(food)
    }

}
