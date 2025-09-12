package com.inodaf.cowpaw

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import java.text.NumberFormat
import java.util.Locale

class InvoiceWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("CowPaw.InvoiceWidget", "Update")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val widgetText = context.getString(R.string.appwidget_text)
        val views = RemoteViews(context.packageName, R.layout.invoice_widget)

        val currencySymbol = NumberFormat.getCurrencyInstance(Locale.getDefault()).currency!!.symbol

        val amount = getCurrentAmount(context)
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale.getDefault())
            .format(amount)
            .replace(currencySymbol, "")

        views.setTextViewText(R.id.invoice_widget_amount, formattedAmount)
        views.setTextViewText(R.id.appwidget_text, widgetText)

        views.setTextColor(
            R.id.invoice_widget_amount,
            if (amount > 0) context.getResources()
                .getColor(R.color.widget_invoice_amount) else context.getResources()
                .getColor(R.color.widget_invoice_amount_credit)
        )

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getCurrentAmount(context: Context): Float {
        val preferences = context.getSharedPreferences(
            context.getString(R.string.key_amount_file),
            Context.MODE_PRIVATE
        )

        return preferences.getFloat(
            context.getString(R.string.key_amount_value),
            0.0f
        )
    }
}
