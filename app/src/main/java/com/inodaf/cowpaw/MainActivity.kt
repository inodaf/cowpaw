package com.inodaf.cowpaw

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.inodaf.cowpaw.models.Invoice
import com.inodaf.cowpaw.viewmodels.InvoiceViewModel
import com.inodaf.cowpaw.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: InvoiceViewModel by viewModels()
    private val model = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isOnboarded() == false) {
            return startOnboarding()
        }

        setContentView(R.layout.activity_main)
        defineObservers()
    }

    private fun defineObservers() {
        model.invoice.observe(this, Observer<Invoice> { updatedInvoice ->
            updateTextView("$ ${updatedInvoice.amount}")
        })
    }

    private fun updateTextView(text: String?) {
        findViewById<TextView>(R.id.current_invoice).text = text
    }

    private fun getCurrentInvoice(): String? {
        val persistenceLayer = getSharedPreferences(getString(R.string.key_amount_file), Context.MODE_PRIVATE)
        return persistenceLayer.getString(getString(R.string.key_amount_value), "$ 0.00")
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
