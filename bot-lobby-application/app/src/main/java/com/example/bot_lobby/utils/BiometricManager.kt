package com.example.bot_lobby.utils

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.bot_lobby.models.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import java.util.concurrent.Executor

object BiometricAuthHelper {

    // Check if biometric authentication is supported
    fun isBiometricSupported(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

    // Authenticate using biometric data
    fun authenticate(activity: FragmentActivity): Deferred<Boolean> {
        val deferred = CompletableDeferred<Boolean>()

        // Executor for UI thread
        val executor: Executor = ContextCompat.getMainExecutor(activity)



        // Create BiometricPrompt instance
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    deferred.complete(true) // Success
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    deferred.complete(false) // Failed
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    deferred.complete(false) // Error
                }
            }
        )

        // Prompt info for biometric dialog
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Authenticate using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        // Start biometric authentication
        biometricPrompt.authenticate(promptInfo)

        return deferred
    }
}