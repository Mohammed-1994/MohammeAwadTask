package com.example.mohammeawadtask.data


import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("status")
    val status: Int
)