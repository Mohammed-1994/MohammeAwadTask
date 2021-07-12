package com.example.mohammeawadtask.ui

import android.graphics.Movie
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.mohammeawadtask.data.Data
import com.example.mohammeawadtask.repository.NetworkStatus
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val movieRepo: CarPageListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Data>> by lazy {
        movieRepo.fetchMoviePageList(compositeDisposable)
    }

    val networkStatus: LiveData<NetworkStatus> by lazy {
        movieRepo.getNetworkStat()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}