package com.example.data.repository

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.data.utils.NetworkUtils
import com.example.domain.repository.NetworkRepository

class NetworkRepositoryImpl(private val context: Context) : NetworkRepository {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isConnected(): Boolean {
        return NetworkUtils.isConnected(context)
    }
}
