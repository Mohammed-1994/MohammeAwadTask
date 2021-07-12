package com.example.mohammeawadtask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mohammeawadtask.api.ApiClient.POST_PER_PAGE
import com.example.mohammeawadtask.api.ApiInterface
import com.example.mohammeawadtask.data.Data
import com.example.mohammeawadtask.repository.CarDataSource
import com.example.mohammeawadtask.repository.CarDataSourceFactory
import com.example.mohammeawadtask.repository.NetworkStatus
import io.reactivex.disposables.CompositeDisposable

class CarPageListRepository(private val apiService: ApiInterface) {
    private lateinit var moviePageList: LiveData<PagedList<Data>>
    private lateinit var movieDataSourceFactory: CarDataSourceFactory

    fun fetchMoviePageList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Data>> {
        movieDataSourceFactory = CarDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(movieDataSourceFactory, config).build()


        return moviePageList

    }

    fun getNetworkStat(): LiveData<NetworkStatus> {
        return Transformations.switchMap(
            movieDataSourceFactory.moviesLiveDataSource, CarDataSource::networkStatus
        )
    }


}