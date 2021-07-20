package com.inodaf.cowpaw

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
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

  override fun onEnabled(context: Context) {
    // Enter relevant functionality for when the first widget is created
  }

  override fun onDisabled(context: Context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

internal fun getPersistedAmount(context: Context): String? {
  val persistenceLayer =  context.getSharedPreferences(context.getString(R.string.key_amount_file), Context.MODE_PRIVATE)
  return persistenceLayer.getString(context.getString(R.string.key_amount_value), "0.0")
}

internal fun updateAppWidget(
  context: Context,
  appWidgetManager: AppWidgetManager,
  appWidgetId: Int
) {


  val widgetText = context.getString(R.string.appwidget_text)
  // Construct the RemoteViews object
  val views = RemoteViews(context.packageName, R.layout.invoice_widget)
  val amount = getPersistedAmount(context)

  views.setTextViewText(R.id.invoice_widget_amount, amount)
  views.setTextViewText(R.id.appwidget_text, widgetText)

  // Instruct the widget manager to update the widget
  appWidgetManager.updateAppWidget(appWidgetId, views)
}