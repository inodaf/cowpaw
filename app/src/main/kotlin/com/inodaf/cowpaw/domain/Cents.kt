package com.inodaf.cowpaw.domain

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class Cents(val value: Long) {
    fun format(locale: Locale = Locale("pt", "BR"), withSymbol: Boolean = true): String {
        val symbol = Currency.getInstance(locale).symbol
        val numberFormat = NumberFormat.getInstance(locale).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }

        return "${numberFormat.format(value / 100.0)}".let {
            if (withSymbol) "$symbol $it" else it
        }
    }
}