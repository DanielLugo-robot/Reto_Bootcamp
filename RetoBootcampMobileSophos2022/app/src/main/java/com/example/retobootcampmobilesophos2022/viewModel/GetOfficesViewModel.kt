package com.example.retobootcampmobilesophos2022.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retobootcampmobilesophos2022.model.Office
import com.example.retobootcampmobilesophos2022.model.Offices
import com.example.retobootcampmobilesophos2022.viewModel.network.sophosApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetOfficesViewModel : ViewModel() {

    init { getOffices() }
    val citiesLiveData = MutableLiveData<List<Office>>()

    fun getOffices(){

        val apiService: sophosApiService by lazy {
            sophosApiService.create()
        }

    viewModelScope.launch {
        val call = apiService.getOffices()
        call.enqueue(object : Callback<Offices> {
            override fun onResponse(call: Call<Offices>, response: Response<Offices>) {
                val cities = response.body()
                citiesLiveData.postValue(cities?.Items)
            }
            override fun onFailure(call: Call<Offices>, t: Throwable) {}
        })
    }

    }
}