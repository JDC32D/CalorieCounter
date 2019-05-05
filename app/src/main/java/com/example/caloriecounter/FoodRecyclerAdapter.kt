package com.example.caloriecounter

import Database.Food
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodRecyclerAdapter(foods: ArrayList<Food>, listener: OnItemClickListener)
    : RecyclerView.Adapter<FoodRecyclerAdapter.RecyclerViewHolder>() {

    private var listFoods: List<Food> = foods
    private var listenerFood: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(food: Food)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return listFoods.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var currentFood: Food = listFoods[position]
        var nameFood = currentFood.name
        var calorieFood = currentFood.calories

        holder!!.foodName.text = nameFood
        holder!!.foodCal.text = calorieFood.toString()

        holder.bind(currentFood, listenerFood)
    }

    fun addFoods(listFoods: List<Food>) {
        this.listFoods = listFoods
        notifyDataSetChanged()
    }

    class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var foodName = itemView.findViewById<TextView>(R.id.name_food)!!
        var foodCal = itemView.findViewById<TextView>(R.id.calorie_food)!!
        fun bind(food: Food, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(food)
            }
        }
    }
}