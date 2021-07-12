package com.example.mohammeawadtask.data


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("constractionYear")
    val constractionYear: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("isUsed")
    val isUsed: Boolean
)