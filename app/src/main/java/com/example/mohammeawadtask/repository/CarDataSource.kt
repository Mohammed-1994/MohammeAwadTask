package com.example.mohammeawadtask.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mohammeawadtask.api.ApiClient.FIRST_PAGE
import com.example.mohammeawadtask.api.ApiInterface
import com.example.mohammeawadtask.data.Data
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


private const val TAG = "MovieDataSource myTag"

class CarDataSource(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Data>() {

    private var page = FIRST_PAGE
    val networkStatus: MutableLiveData<NetworkStatus> = MutableLiveData()


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {
        networkStatus.postValue(NetworkStatus.LOADING)
        compositeDisposable.add(
            apiService.getCars(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (3 >= params.key) {

                            callback.onResult(it.data, params.key + 1)
                            networkStatus.postValue(NetworkStatus.LOADED)
                        } else {
                            Log.d(TAG, "loadAfter: End of list")
                            networkStatus.postValue(NetworkStatus.END_OF_LIST)

                        }
                    },
                    {
                        Log.d(TAG, "loadAfter: error")
                        networkStatus.postValue(NetworkStatus.ERROR)
                        Log.e(TAG, "loadAfter: retrofit Error", it)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Data>
    ) {
        networkStatus.postValue(NetworkStatus.LOADING)
        compositeDisposable.add(
            apiService.getCars(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.data, null, page + 1)
                        networkStatus.postValue(NetworkStatus.LOADED)
                    },
                    {
                        networkStatus.postValue(NetworkStatus.ERROR)
                        Log.e(TAG, "loadInitial: retrofit Error", it)
                    }
                )
        )

    }


}