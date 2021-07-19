package com.inodaf.cowpaw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var currentInvoice: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isOnboarded() == false) {
            return startOnboarding()
        }

        setContentView(R.layout.activity_main)
        currentInvoice = findViewById(R.id.current_invoice)
        currentInvoice.setText(getPersistedAmount())
    }

    private fun getPersistedAmount(): String? {
        val persistenceLayer =  getSharedPreferences(getString(R.string.key_amount_file), Context.MODE_PRIVATE)
        return persistenceLayer.getString(getString(R.string.key_amount_value), "0.0")
    }

    private fun isOnboarded(): Boolean? {
        val persistenceLayer =  getSharedPreferences(getString(R.string.key_onboarding_completed_file), Context.MODE_PRIVATE)
        val isOnboarded =  persistenceLayer.getString(getString(R.string.key_onboarding_completed_value), "false")
        return isOnboarded?.toBoolean()
    }

    private fun startOnboarding() {
        val onboardingActivityIntent = Intent(this, OnboardingActivity::class.java)
        startActivity(onboardingActivityIntent)
    }
}
