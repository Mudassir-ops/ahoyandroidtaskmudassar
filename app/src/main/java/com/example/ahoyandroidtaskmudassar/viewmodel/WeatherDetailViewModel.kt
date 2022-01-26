package com.example.ahoyandroidtaskmudassar.viewmodel




import androidx.lifecycle.*

import com.example.ahoyandroidtaskmudassar.model.FavouritesTable
import com.example.ahoyandroidtaskmudassar.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel
@Inject
constructor(private val databaseRepository: DatabaseRepository) : ViewModel() {

     val name = MutableLiveData<String>()

    val getFavouritesCityWeather:LiveData<List<FavouritesTable>> get() =
        databaseRepository.getfavourites.flowOn(Dispatchers.Main).asLiveData(context = viewModelScope.coroutineContext)


     val data:LiveData<FavouritesTable> get()= databaseRepository.getFavouriteCityByName(name.value!!).flowOn(Dispatchers.Main).asLiveData(context = viewModelScope.coroutineContext)


}













