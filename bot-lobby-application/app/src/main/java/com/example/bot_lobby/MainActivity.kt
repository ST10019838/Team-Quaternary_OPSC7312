package com.example.bot_lobby

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.Column

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.services.LoginService
import com.example.bot_lobby.services.RegisterService
import com.example.bot_lobby.ui.pages.AnnouncementsTab
import com.example.bot_lobby.ui.pages.HomeTab
import com.example.bot_lobby.ui.pages.ProfileTab
import com.example.bot_lobby.ui.pages.ScoutingTab
import com.example.bot_lobby.ui.pages.TeamsTab
import com.example.bot_lobby.ui.screens.LoginScreen
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.example.bot_lobby.observers.ConnectivityObserver
import com.example.bot_lobby.observers.NetworkConnectivityObserver
import com.example.bot_lobby.ui.screens.LandingScreen
import com.example.bot_lobby.view_models.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


// class MainActivity : ComponentActivity(), AppCompatActivity() {
//     companion object {
//         val userApi: UserApi = RetrofitInstance.UserApi
//         // Declare loginService without instantiation here
//
class MainActivity :  /*ComponentActivity(),*/ AppCompatActivity() {

    companion object {
        val userApi: UserApi = RetrofitInstance.UserApi
        lateinit var PassedActivity: MainActivity // Declare an instance reference
        lateinit var loginService: LoginService
        lateinit var registerService: RegisterService
        lateinit var connectivityObserver: ConnectivityObserver

        const val REQUEST_NOTIFICATION_PERMISSION_CODE = 1001 // Declare constant here
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PassedActivity = this
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        enableEdgeToEdge()

        loginService = LoginService(userApi)
        registerService = RegisterService(userApi, this)

        // Check and request notification permission
        if (!isNotificationPermissionGranted()) {
            requestNotificationPermission()
        }

        setContent {
            val connectivity by connectivityObserver.observe()
                .collectAsState(ConnectivityObserver.Status.Unavailable)
            var dismissedOfflineDialog by remember { mutableStateOf(false) }

//            val showOfflineDialog by remember {mutableStateOf(connectivity != ConnectivityObserver.Status.Available && !dismissedOfllineDialog)}

            val showOfflineDialog =
                connectivity != ConnectivityObserver.Status.Available && !dismissedOfflineDialog

            if (connectivity == ConnectivityObserver.Status.Available) {
                dismissedOfflineDialog = false
            }

            var showStudentNotice by remember { mutableStateOf(true) }

            val context = LocalContext.current
            val sessionViewModel = viewModel { SessionViewModel(context) }
//            val session by sessionViewModel.session.collectAsState()
//            var isLoading by remember { mutableStateOf(false) }
//
//
//
//            LaunchedEffect(true) {
//                Log.i("SESSION BOI", session.toString())
//                isLoading = true
//                delay(3000L)
//
//                Log.i("SESSION BOI", session.toString())
//
//                isLoading = false
//            }

//            val coroutineScope = rememberCoroutineScope()
//
//            val vm = TestViewModel(applicationContext)
//
////            LaunchedEffect(true) {
////                models =
////            }
//
//           val state by vm.session.collectAsStateWithLifecycle()

//            val sessionViewModel = SessionViewModel(LocalContext.current)


//            val test by sessionViewModel.test.collectAsState()
//            val session by sessionViewModel.session.collectAsStateWithLifecycle()

//            val shouldLogIn = session != null


//            Log.i("Session?", time.toString() )

//            if(session != null)
//                Log.i("SESSION IS NOT NULL",session.toString() )

//            val screenToDisplay = if(session != null)
//                LandingScreen() else LoginScreen()

//            val status by connectivityObserver.observe().collectAsState(
//                initial = ConnectivityObserver.Status.Unavailable
//            )
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = "Network status: $status")
//            }

            BotLobbyTheme {
                Navigator(LoginScreen()) {
                    SlideTransition(it)
                }
            }
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, // Changed type here
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


//@Composable
//private fun RowScope.TabNavigationItem(tab: Tab) {
//    val tabNavigator = LocalTabNavigator.current
//    val isSelected = tabNavigator.current == tab
//
//    NavigationBarItem(
//        selected = isSelected,
//        onClick = { tabNavigator.current = tab },
//        icon = {
//            Icon(
//                tab.options.icon!!,
//                contentDescription = tab.options.title,
//                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f)
//            )
//        },
//        label = {
//            Text(
//                text = tab.options.title,
//                textAlign = TextAlign.Center,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f),
//                style = if (isSelected) MaterialTheme.typography.bodyMedium
//                else MaterialTheme.typography.bodySmall
//            )
//        },
//        colors = NavigationBarItemDefaults.colors(
//            indicatorColor = MaterialTheme.colorScheme.primary,
//            selectedTextColor = MaterialTheme.colorScheme.primary
//        )
//    )
//}
