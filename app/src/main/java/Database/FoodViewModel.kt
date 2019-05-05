package Database

//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class FoodViewModel(app: Application): AndroidViewModel(app) {
//
//    private val repository: FoodRepository
//    val allFoods: LiveData<List<Food>>
//
//    init {
//        val foodDao = FoodDatabase.getDatabase(app).foodDao()
//        repository = FoodRepository(foodDao)
//        allFoods = repository.allFoods
//    }
//
//    fun insert(food: Food) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insert(food)
//    }
//}