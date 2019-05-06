package Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.caloriecounter.R
import kotlinx.android.synthetic.main.add_food_layout.*

class AddFoodFragment : DialogFragment() {

    private var listener: AddListener? = null
    interface AddListener {
        fun addPressed()
    }

    fun registerListener(listener: AddListener) { this.listener = listener }
    fun deregisterListener(listener: AddListener) {
        if (this.listener == listener) {
            this.listener = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_food_layout, container)
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

}