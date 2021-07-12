package com.example.mohammeawadtask.repository

import android.graphics.Movie
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mohammeawadtask.api.ApiInterface
import com.example.mohammeawadtask.data.Data
import io.reactivex.disposables.CompositeDisposable

class CarDataSourceFactory(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Data>() {

    val moviesLiveDataSource = MutableLiveData<CarDataSource>()

    override fun create(): DataSource<Int, Data> {
        val movieDataSource = CarDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}