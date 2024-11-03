package com.example.bot_lobby.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.bot_lobby.models.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executor

object BiometricAuthHelper {

    // Check if biometric authentication is supported
    fun isBiometricSupported(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }
    // Register user biometrics data
    fun registerBiometricData(user: User, activity: AppCompatActivity): User = runBlocking {
        val supported = isBiometricSupported(activity)
        if(!supported){
            Log.d("BiometricAuthHelper", "Biometrics failed due to not supported")
        }else{
            Log.d("BiometricAuthHelper", "Biometrics supported")
        }
        val isAuthenticated = authenticate(activity).await()

        if (isAuthenticated) {
            // Placeholder identifier for biometric registration success
            user.biometrics = "biometric_registered_${System.currentTimeMillis()}"
            Log.d("BiometricAuthHelper", "Biometric data registered for user: ${user.username}")
        } else {
            Log.e("BiometricAuthHelper", "Biometric registration failed for user: ${user.username}")
        }

        user
    }
    // Authenticate using biometric data
    fun authenticate(activity: AppCompatActivity): Deferred<Boolean> {
        val deferred = CompletableDeferred<Boolean>()

        // Use the main thread executor
        val executor: Executor = ContextCompat.getMainExecutor(activity)

        // Ensure biometric authentication runs on the main thread
        activity.runOnUiThread {
            // Create BiometricPrompt instance with the main thread executor
            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        deferred.complete(true) // Authentication succeeded
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        deferred.complete(false) // Authentication failed
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        deferred.complete(false) // Authentication error occurred
                    }
                }
            )

            // Build prompt info for the biometric dialog
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Authenticate using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            // Start biometric authentication
            biometricPrompt.authenticate(promptInfo)
        }

        return deferred // Return the Deferred result
    }

}