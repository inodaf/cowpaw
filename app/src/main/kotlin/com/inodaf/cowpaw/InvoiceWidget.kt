package com.inodaf.cowpaw

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.inodaf.cowpaw.outbound.WidgetsUpdater
import com.inodaf.cowpaw.usecases.GetCurrentInvoice
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InvoiceWidget : AppWidgetProvider() {

    @Inject lateinit var getCurrentInvoice: GetCurrentInvoice

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("CowPaw.InvoiceWidget", "Update")

        context.startService(Intent(context, WidgetsUpdater::class.java))

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
        val invoice = getCurrentInvoice().getOrNull()

        val widgetText = context.getString(R.string.appwidget_text)
        val views = RemoteViews(context.packageName, R.layout.invoice_widget)

        views.setTextViewText(R.id.appwidget_text, widgetText)

        if (invoice == null) {
            views.setTextViewText(R.id.invoice_widget_amount, "0,00")
            appWidgetManager.updateAppWidget(appWidgetId, views)
            return
        }

        views.setTextViewText(
            R.id.invoice_widget_amount,
            invoice.total.format(withSymbol = false)
        )

        val amountColor = if (invoice.total.value > 0) {
            context.resources.getColor(R.color.widget_invoice_amount, null)
        } else {
            context.resources.getColor(R.color.widget_invoice_amount_credit, null)
        }

        views.setTextColor(R.id.invoice_widget_amount, amountColor)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
