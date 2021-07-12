package com.example.mohammeawadtask.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    NO_MORE
}

class NetworkStatus(val status: Status, val msg: String) {
    companion object {
        val LOADED: NetworkStatus = NetworkStatus(Status.SUCCESS, "Success")
        val LOADING: NetworkStatus = NetworkStatus(Status.RUNNING, "Running")
        val ERROR: NetworkStatus = NetworkStatus(Status.FAILED, "Failed")
        val END_OF_LIST: NetworkStatus = NetworkStatus(Status.NO_MORE, "No more cars")

    }
}