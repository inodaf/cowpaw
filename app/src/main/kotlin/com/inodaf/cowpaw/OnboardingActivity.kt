package com.inodaf.cowpaw

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit

// TODO:
// Shortcut for opening the widgets drawer
// Invoices due date setup


class OnboardingActivity : AppCompatActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS
    )

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
            if (grantResult == PackageManager.PERMISSION_DENIED) return
        }

        setCompleted()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun setCompleted() {
        val preferences = getSharedPreferences(
            getString(R.string.key_onboarding_completed_file),
            Context.MODE_PRIVATE
        )

        preferences.edit(commit = true) {
            putBoolean(getString(R.string.key_onboarding_completed_value), true)
        }
    }

    private fun requestPermissions() {
        val notPermitted = requiredPermissions.none {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (notPermitted) requestPermissions(requiredPermissions, 1)
    }
}
