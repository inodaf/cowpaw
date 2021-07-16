package com.inodaf.cowpaw

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class OnboardingActivity : AppCompatActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val startButton = findViewById<Button>(R.id.start_button)

        startButton.setOnClickListener {
            Log.d("CowPaw.Onboarding", "Start Button Clicked")
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        setOnboardingCompleted(value = true)
        startMain()
    }

    private fun setOnboardingCompleted(value: Boolean) {
        val onboardingPreferences = getSharedPreferences(getString(R.string.key_onboarding_completed_file), Context.MODE_PRIVATE)

        with (onboardingPreferences.edit()) {
            putString(getString(R.string.key_onboarding_completed_value), value.toString())
            commit()
        }
    }

    private fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun hasDeniedPermissions(): Boolean {
        val grantedPermission: (String) -> Boolean = { it: String ->
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        return requiredPermissions.none(grantedPermission)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissions() {
        if (hasDeniedPermissions()) requestPermissions(requiredPermissions, 1)
    }
}
