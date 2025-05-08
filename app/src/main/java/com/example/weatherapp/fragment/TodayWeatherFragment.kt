package com.example.weatherapp.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.utils.toString
import com.example.weatherapp.R
import com.example.weatherapp.adapter.HourlyTempAdapter
import com.example.weatherapp.databinding.TodayWeatherBinding
import com.example.weatherapp.state.WeatherUiState
import com.example.weatherapp.utils.getWeatherIcon
import com.example.weatherapp.utils.requestSingleLocationUpdate
import com.example.weatherapp.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class TodayWeatherFragment : Fragment() {

    private lateinit var binding: TodayWeatherBinding
    private lateinit var adapter: HourlyTempAdapter
    private lateinit var viewModel: WeatherViewModel
    private lateinit var loadingLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = TodayWeatherBinding.inflate(inflater, container, false)
        loadingLayout = inflater.inflate(R.layout.loading_ui, binding.root, false)
        binding.root.addView(loadingLayout)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]

        setUpRecyclerView()
        observeNetwork()
        observeUiState()
        checkLocationPermission()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.checkNetworkConnection()
            getUserLocation(forceRefresh = true)
        }

        if (viewModel.uiState.value !is WeatherUiState.Success) {
            getUserLocation(forceRefresh = true)
        }
    }

    override fun onResume() {
        super.onResume()
        getUserLocation()
    }

    private fun observeNetwork() {
        viewModel.networkState.observe(viewLifecycleOwner) { isConnected ->
            binding.noInternetMsg.visibility = if (isConnected) View.GONE else View.VISIBLE

            if (!isConnected) {
                binding.swipeRefreshLayout.isRefreshing = false
                Toast.makeText(
                    requireContext(),
                    R.string.no_internet_connection_nshowing_last_known_weather,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (viewModel.uiState.value !is WeatherUiState.Success) {
                getUserLocation(forceRefresh = true)
            }
        }

        viewModel.checkNetworkConnection()
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WeatherUiState.Loading -> {
                    showLoading(true)
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                is WeatherUiState.Success -> {
                    showLoading(false)
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.weatherGroup.visibility = View.VISIBLE

                    val weatherList = state.data
                    Log.e("DATA", "observeUiState: $weatherList")

                    val hourlyWeather = weatherList.firstOrNull()?.hours?.map {
                        it.copy(datetime = formatTime(it.datetime))
                    } ?: emptyList()
                    adapter.updateData(hourlyWeather)

                    val todayWeather = weatherList.firstOrNull()
                    binding.tempTextView.text = "${todayWeather?.temp}°C"
                    binding.maxtemp.text = "H:${todayWeather?.tempmax}°C"
                    binding.mintemp.text = "L:${todayWeather?.tempmin}°C"
                    binding.condition.text = todayWeather?.conditions
                    binding.weatherIconImageView.setImageResource(
                        getWeatherIcon(todayWeather?.icon ?: "")
                    )
                }

                is WeatherUiState.Error -> {
                    showLoading(false)
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(
                        requireContext(),
                        "Error: ${state.errMessage.toString(requireContext())}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == viewModel.LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getUserLocation()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.location_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkLocationPermission() {
        val permissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                viewModel.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getUserLocation()
        }
    }

    private fun getUserLocation(forceRefresh: Boolean = false) {
        if (!forceRefresh && !viewModel.shouldFetchWeather()) {
            viewModel.lastLat?.let { lat ->
                viewModel.lastLon?.let { lon ->
                    viewModel.getStateAndCountry(lat, lon) { city ->
                        if (isAdded) {
                            requireActivity().runOnUiThread {
                                binding.cityTextView.text = city
                            }
                        }
                    }
                    return
                }
            }
        }

        requireContext().requestSingleLocationUpdate { lat, lon ->
            viewModel.getStateAndCountry(lat, lon) { city ->
                if (isAdded) {
                    requireActivity().runOnUiThread {
                        binding.cityTextView.text = city
                    }
                }
            }
            viewModel.loadWeather(lat, lon)
        }
    }

    private fun setUpRecyclerView() {
        binding.hourlyTempRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HourlyTempAdapter(emptyList())
        binding.hourlyTempRecyclerView.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun formatTime(time: String): String {
        return try {
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = inputFormat.parse(time)
            outputFormat.format(date ?: return time)
        } catch (e: Exception) {
            e.printStackTrace()
            time
        }
    }
}
