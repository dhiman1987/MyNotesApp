package com.mynote.mynotes.encryption

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.crypto.Cipher

class BiometricAuthHelper(
    private val activity: FragmentActivity,
    private val onAuthenticated: (Cipher) -> Unit
) {
    private val biometricPrompt: BiometricPrompt

    init {
        val executor = ContextCompat.getMainExecutor(activity)
        biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val cryptoObject = result.cryptoObject
                if (cryptoObject != null) {
                        onAuthenticated(cryptoObject.cipher!!)
                }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                    // Handle authentication error
            }
        })
    }

    fun authenticate(cipher: Cipher) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }
}
