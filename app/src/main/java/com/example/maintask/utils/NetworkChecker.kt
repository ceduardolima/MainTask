package com.example.maintask.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkChecker(private val connectivityManager: ConnectivityManager?) {

    fun performActionIfConnected(context: Context?, action: () -> Unit) {
        if (hasInternet())
            action()
        else Toast.makeText(context, "Não há conexão com a Internet", Toast.LENGTH_LONG).show()
    }

    private fun hasInternet(): Boolean {
        // verifica se os dados estão ativados
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        // Validação das conexões:
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) // wifi
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) // dados moveis
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) // VPN
    }
}