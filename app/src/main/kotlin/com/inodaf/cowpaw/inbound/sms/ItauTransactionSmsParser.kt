package com.inodaf.cowpaw.inbound.sms

import com.inodaf.cowpaw.domain.Transaction
import com.inodaf.cowpaw.inbound.sms.TransactionSmsParser.ParseError

class ItauTransactionSmsParser : TransactionSmsParser {

    override fun parse(content: String): Result<Transaction> {
        if (content.isEmpty() || !isTransaction(content)) {
            return Result.failure(ParseError.NotATransaction())
        }

        val amount = getAmount(content)
            ?: return Result.failure(ParseError.NotATransaction())

        return when {
            isPurchase(content) -> Transaction.purchase(amount)
            isReversal(content) -> Transaction.refund(amount)
            else -> Result.failure(ParseError.NotATransaction())
        }
    }

    private fun isTransaction(content: String): Boolean {
        return listOf(DATE_PATTERN, TIME_PATTERN, AMOUNT_PATTERN)
            .filter { content.contains(it) }
            .size == 3
    }

    private fun isPurchase(content: String): Boolean {
        return content.contains(
            Regex(
                "(compra aprovada)|(pre-autorizacao)",
                RegexOption.IGNORE_CASE
            )
        )
    }

    private fun isReversal(content: String): Boolean {
        return content.contains(
            Regex(
                "(confirmamos o estorno da compra)",
                RegexOption.IGNORE_CASE
            )
        )
    }

    private fun getAmount(content: String): Long? {
        return AMOUNT_PATTERN.find(content)?.value
            ?.replace(",", "")
            ?.replace(".", "")
            ?.trim()
            ?.toLongOrNull()
    }

    companion object {
        val DATE_PATTERN = Regex("\\d{2}/\\d{2}")
        val TIME_PATTERN = Regex("\\d{2}h\\d{2}")
        val AMOUNT_PATTERN = Regex("\\d*.\\d*,\\d{2}")
    }
}
