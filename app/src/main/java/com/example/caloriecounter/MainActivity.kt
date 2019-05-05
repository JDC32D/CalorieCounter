package com.example.caloriecounter

import Database.Food
import Database.FoodDatabase
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity(), FoodRecyclerAdapter.OnItemClickListener {

    private var foodRecyclerView: RecyclerView? = null
    private var recyclerViewAdapter: FoodRecyclerAdapter? = null
    private var viewModel: FoodListViewModel? = null
    private var db: FoodDatabase? = null

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
        //Would rather make a dialog fragment here than new activity
//        fab.setOnClickListener {
//            var intent = Intent(applicationContext, FoodDetailsActivity::class.java)
//            startActivity(intent)
//        }

    }

    override fun onItemClick(food: Food) {
        Log.wtf("onItemClick()", "food tapped")
    }

}
