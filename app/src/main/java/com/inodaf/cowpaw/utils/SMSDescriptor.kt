package com.inodaf.cowpaw.utils

class SMSDescriptor(private val message: String) {
  val datePattern = Regex("\\d{2}/\\d{2}")
  val timePattern = Regex("\\d{2}h\\d{2}")
  val amountPattern = Regex("\\d*.\\d*,\\d{2}")

  fun matchBankingSMS(): Boolean {
    val messageSchema = listOf(datePattern, timePattern, amountPattern)
    return messageSchema.filter { message.contains(it) }.size == 3
  }

  fun matchPurchaseSMS(): Boolean {
    return message.contains(Regex("(compra aprovada)|(pre-autorizacao)", RegexOption.IGNORE_CASE))
  }

  fun matchReversalSMS(): Boolean {
    return message.contains(Regex("(confirmamos o estorno da compra)", RegexOption.IGNORE_CASE))
  }

  fun getAmount(): String? {
    return amountPattern.find(message)?.value
  }
}