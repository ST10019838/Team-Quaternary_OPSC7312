package com.example.bot_lobby.observers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// The following code was taken from youtube.com
// Author: Philipp Lackner (https://www.youtube.com/@PhilippLackner)
// Link: https://www.youtube.com/watch?v=TzV0oCRDNfM

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkConnectivityObserver(
    private val context: Context
): ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(ConnectivityObserver.Status.Available) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(ConnectivityObserver.Status.Lost) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(ConnectivityObserver.Status.Unavailable) }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

//    val isConnected =
//        flow<Boolean> {
////            delay(1000L)
//            LocalDatabase.getDatabase(context).sessionDao.getSession()
//        }
//        .stateIn(
//        context, SharingStarted.WhileSubscribed(5000L), false
//    )
//
//    fun isConnnected(): Flow<ConnectivityObserver.Status> {
//        return callbackFlow {
//            val callback = object : ConnectivityManager.NetworkCallback() {
//                override fun onAvailable(network: Network) {
//                    super.onAvailable(network)
//                    launch { send(ConnectivityObserver.Status.Available) }
//                }
//
//                override fun onLosing(network: Network, maxMsToLive: Int) {
//                    super.onLosing(network, maxMsToLive)
//                    launch { send(ConnectivityObserver.Status.Losing) }
//                }
//
//                override fun onLost(network: Network) {
//                    super.onLost(network)
//                    launch { send(ConnectivityObserver.Status.Lost) }
//                }
//
//                override fun onUnavailable() {
//                    super.onUnavailable()
//                    launch { send(ConnectivityObserver.Status.Unavailable) }
//                }
//            }
//
//            connectivityManager.registerDefaultNetworkCallback(callback)
//            awaitClose {
//                connectivityManager.unregisterNetworkCallback(callback)
//            }
//        }.distinctUntilChanged()
//    }

}