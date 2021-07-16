package com.inodaf.cowpaw.utils

val DATE_PATTERN = Regex("\\d{2}/\\d{2}")
val TIME_PATTERN = Regex("\\d{2}h\\d{2}")
val AMOUNT_PATTERN = Regex("\\d*.\\d*,\\d{2}")

fun isFinancialMessage(message: String): Boolean {
  val messagePattern = listOf(DATE_PATTERN, TIME_PATTERN, AMOUNT_PATTERN)
  return messagePattern.filter { message.contains(it) }.size == 3
}