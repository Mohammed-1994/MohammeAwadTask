package com.example.mohammeawadtask.api

import com.example.mohammeawadtask.data.Car
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


// base_url = http://demo1585915.mockable.io/api/v1/cars?page={page}
interface ApiInterface {

    @GET("api/v1/cars")
    fun getCars(@Query("page") page: Int): Single<Car>

}