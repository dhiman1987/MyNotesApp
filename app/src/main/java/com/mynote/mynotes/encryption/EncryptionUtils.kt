package com.mynote.mynotes.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptionUtils {
    private const val KEY_ALIAS = "dot-dash-oti-gopon-chabi"
    private const val IMPORT_KEY_ALIAS = "dot-dash-gopon-chabi"
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val CIPHER_TRANSFORMER = "AES/GCM/NoPadding"
    private lateinit var keyStore: KeyStore

    private val TAG = "EncryptionUtils"

    fun initialize() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        val isStrongBoxAvailable = keyStore.getProvider().name.contains("StrongBox")
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            generateKey(isStrongBoxAvailable)
            Log.v(TAG, "new key generated")
        } else {
            Log.v(TAG, "existing key found")
        }

        if (!keyStore.containsAlias(IMPORT_KEY_ALIAS)) {
            generateImportKey(isStrongBoxAvailable)
            Log.v(TAG, "new import key generated")
        } else {
            Log.v(TAG, "existing import key found")
        }
    }

    fun getEncryptCipher(): Cipher {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMER)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        return cipher
    }

    fun getDecryptCipher(iv: ByteArray): Cipher {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMER)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        return cipher
    }

    fun getImportEncryptCipher(): Cipher {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMER)
        cipher.init(Cipher.ENCRYPT_MODE, getImportSecretKey())
        return cipher
    }

    fun getImportDecryptCipher(iv: ByteArray): Cipher {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMER)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getImportSecretKey(), spec)
        return cipher
    }

    private fun getSecretKey(): SecretKey {
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun getImportSecretKey(): SecretKey {
        return keyStore.getKey(IMPORT_KEY_ALIAS, null) as SecretKey
    }

    private fun generateKey(isStrongBoxEnabled:Boolean) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setIsStrongBoxBacked(isStrongBoxEnabled)
                .setUserAuthenticationRequired(true)
                .build())
        keyGenerator.generateKey()
    }

    private fun generateImportKey(isStrongBoxEnabled:Boolean) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(IMPORT_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setIsStrongBoxBacked(isStrongBoxEnabled)
                .setUserAuthenticationRequired(false)
                .build())
        keyGenerator.generateKey()
    }
}
