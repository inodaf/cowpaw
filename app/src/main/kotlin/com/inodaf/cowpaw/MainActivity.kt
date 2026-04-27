package com.inodaf.cowpaw

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.inodaf.cowpaw.viewmodels.InvoiceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: InvoiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isOnboarded()) return startOnboarding()

        setContentView(R.layout.activity_main)

        viewModel.invoice.observe(this) {
            val amount = String.format("R$ %.2f", it.total.value)
            findViewById<TextView>(R.id.current_invoice).text =  amount
        }
    }

    private fun isOnboarded(): Boolean {
        val preferences = getSharedPreferences(
            getString(R.string.key_onboarding_completed_file),
            MODE_PRIVATE
        )

        return preferences.getBoolean(
            getString(R.string.key_onboarding_completed_value),
            false
        )
    }

    private fun startOnboarding() {
        startActivity(Intent(this, OnboardingActivity::class.java))
    }
}
