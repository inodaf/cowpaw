package com.inodaf.cowpaw.utils

class SMSDescriptor {
  var message: String = ""
  val datePattern = Regex("\\d{2}/\\d{2}")
  val timePattern = Regex("\\d{2}h\\d{2}")
  val amountPattern = Regex("\\d*.\\d*,\\d{2}")

  fun isBankingSMS(): Boolean {
    if (message.isEmpty()) return false

    val messageSchema = listOf(datePattern, timePattern, amountPattern)
    return messageSchema.filter { message.contains(it) }.size == 3
  }

  fun isPurchaseSMS(): Boolean {
    if (message.isEmpty()) return false
    return message.contains(Regex("(compra aprovada)|(pre-autorizacao)", RegexOption.IGNORE_CASE))
  }

  fun isReversalSMS(): Boolean {
    if (message.isEmpty()) return false
    return message.contains(Regex("(confirmamos o estorno da compra)", RegexOption.IGNORE_CASE))
  }

  fun getAmount(): String {
    if (message.isEmpty()) return ""
    return amountPattern.find(message)?.value ?: ""
  }
}