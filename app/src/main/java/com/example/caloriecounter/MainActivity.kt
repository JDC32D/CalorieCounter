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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import android.animation.ValueAnimator
import android.animation.ArgbEvaluator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.animation.doOnEnd
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


class MainActivity : AppCompatActivity(), FoodRecyclerAdapter.OnItemClickListener {

    private var foodRecyclerView: RecyclerView? = null
    private var recyclerViewAdapter: FoodRecyclerAdapter? = null
    private var viewModel: FoodListViewModel? = null
    private var db: FoodDatabase? = null
    private var totalCals: Int = 0
    private var schulteQuotes = arrayOf<String>()
    private var CHANNEL_ID = "schulte"
    private var notificationId = 1

    // I decided to have the add_food be done in an activity, this fragment currently does nothing
    private var foodFragment = supportFragmentManager.findFragmentById(R.id.add_food) as AddFoodFragment?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FoodDatabase.getDatabase(this)

        foodRecyclerView = findViewById(R.id.recycler_view)
        recyclerViewAdapter = FoodRecyclerAdapter(arrayListOf(), this)

        // This is unsafe if moving it to a fragment...maybe just unsafe
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
        createNotificationChannel()

    }

    // Schulte said funny things all the time and these are my favorites
    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pregnant_woman_black_24dp)
            .setContentTitle(getString(R.string.schulteTitle))
            .setContentText(getRandQuote())
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    override fun onPause() {
        super.onPause()
       createNotification()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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

    private fun getRandQuote() : String {
        schulteQuotes = resources.getStringArray(R.array.Schulte)
        val rand = Random().nextInt(schulteQuotes.size)
        return schulteQuotes[rand]
    }

}
