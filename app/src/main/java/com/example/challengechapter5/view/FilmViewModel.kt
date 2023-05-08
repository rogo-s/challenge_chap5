package com.example.challengechapter5.view

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengechapter5.model.ResponseDataFilm
import com.example.challengechapter5.model.ResponseDetailFilm
import com.example.challengechapter5.model.ResultFilm
import com.example.challengechapter5.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmViewModel : ViewModel() {

    lateinit var liveDataFilm : MutableLiveData<List<ResultFilm>>
    lateinit var liveDataDetailFilm : MutableLiveData<ResponseDetailFilm>

    init {
        liveDataFilm = MutableLiveData()
        liveDataDetailFilm = MutableLiveData()
    }

    fun getDataFilm() : MutableLiveData<List<ResultFilm>>{
        return liveDataFilm
    }

    fun getDetailFilm() : MutableLiveData<ResponseDetailFilm>{
        return liveDataDetailFilm
    }

    fun callApiFilmPopular(){
        RetrofitClient.instance.getAllFilmPopular()
            .enqueue(object : Callback<ResponseDataFilm> {
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<ResponseDataFilm>,
                    response: Response<ResponseDataFilm>
                ) {
                    if(response.isSuccessful){
                        liveDataFilm.postValue(response.body()!!.results)

                    }else{
                        liveDataFilm.postValue(null)
                    }
                }

                @SuppressLint("NullSafeMutableLiveData")
                override fun onFailure(call: Call<ResponseDataFilm>, t: Throwable) {
                    liveDataFilm.postValue(null)
                }
            })
    }

    fun callApiFilmTopRate(){
        RetrofitClient.instance.getAllFilmTopRate()
            .enqueue(object : Callback<ResponseDataFilm>{
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<ResponseDataFilm>,
                    response: Response<ResponseDataFilm>
                ) {
                    if(response.isSuccessful){
                        liveDataFilm.postValue(response.body()!!.results)
                    }else{
                        liveDataFilm.postValue(null)
                    }
                }

                @SuppressLint("NullSafeMutableLiveData")
                override fun onFailure(call: Call<ResponseDataFilm>, t: Throwable) {
                    liveDataFilm.postValue(null)
                }
            })
    }

    fun callApiDetailFilm(id : Int){
        RetrofitClient.instance.getDetailFilm(id)
            .enqueue(object : Callback<ResponseDetailFilm>{
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<ResponseDetailFilm>,
                    response: Response<ResponseDetailFilm>
                ) {
                    if(response.isSuccessful){
                        liveDataDetailFilm.postValue(response.body())
                    }else{
                        liveDataDetailFilm.postValue(null)
                    }
                }

                @SuppressLint("NullSafeMutableLiveData")
                override fun onFailure(call: Call<ResponseDetailFilm>, t: Throwable) {
                    liveDataDetailFilm.postValue(null)
                }

            })
    }



}