package com.example.mohammeawadtask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mohammeawadtask.R
import com.example.mohammeawadtask.api.ApiClient
import com.example.mohammeawadtask.databinding.ActivityMainBinding
import com.example.mohammeawadtask.repository.NetworkStatus

private const val TAG = "MainActivity myTag"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    lateinit var carRepository: CarPageListRepository
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = ApiClient.getClient()
        carRepository = CarPageListRepository(apiService)

        viewModel = getViewModel()

        val carAdapter = CarPageListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 1)

        binding.rvCarList.layoutManager = gridLayoutManager
        binding.rvCarList.setHasFixedSize(true)
        binding.rvCarList.adapter = carAdapter


        viewModel.moviePagedList.observe(this, {
            carAdapter.submitList(it)
        })

        viewModel.networkStatus.observe(this, {
            binding.progressBarPopular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkStatus.LOADING) View.VISIBLE
                else View.GONE

            binding.txtErrorPopular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkStatus.ERROR) View.VISIBLE
                else View.GONE

            if (!viewModel.listIsEmpty())
                carAdapter.setNetworkState(it)

        })

        binding.swipeRefresh.setOnRefreshListener {

            viewModel.moviePagedList.observe(this, {
                carAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
            })
        }

    }


    private fun getViewModel(): MainActivityViewModel {

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(carRepository) as T
            }
        }
        val viewModel =
            ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)


        return viewModel

    }
}