package com.example.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.utils.toString
import com.example.weatherapp.R
import com.example.weatherapp.adapter.DailyTempAdapter
import com.example.weatherapp.databinding.ForecastWeatherBinding
import com.example.weatherapp.state.WeatherUiState
import com.example.weatherapp.viewModel.WeatherViewModel

class ForecastFragment : Fragment() {

    private lateinit var binding: ForecastWeatherBinding
    private lateinit var adapter: DailyTempAdapter
    private lateinit var viewModel: WeatherViewModel
    private lateinit var loadingLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ForecastWeatherBinding.inflate(inflater, container, false)
        loadingLayout = inflater.inflate(R.layout.loading_ui, binding.root, false)
        binding.root.addView(loadingLayout)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            viewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]
            setUpRecyclerView()
            observeUiState()
        }
    }


    private fun setUpRecyclerView() {
        binding.dailyTempRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = DailyTempAdapter(emptyList())
        binding.dailyTempRecyclerView.adapter = adapter
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WeatherUiState.Loading -> {
                    showLoading(true)
                }

                is WeatherUiState.Success -> {
                    showLoading(false)
                    val forecastList = state.data.drop(1).take(5) // Take only 5 days of forecast
                    adapter.updateDate(forecastList)
                }

                is WeatherUiState.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "Error: ${state.errMessage.toString(requireContext())}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
