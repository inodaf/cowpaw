package com.inodaf.cowpaw.domain

class Invoice private constructor(
    private var computedTotal: Long,
    private val transactions: MutableList<Transaction>
) {
    fun total(): Long = computedTotal

    fun append(transaction: Transaction) {
        transactions.add(transaction)
        computedTotal = when (transaction.type) {
            Transaction.Type.Purchase -> computedTotal + transaction.amount
            Transaction.Type.Reversal -> computedTotal - transaction.amount
        }
    }

     companion object {
         fun empty() = Invoice(computedTotal = 0L, transactions = mutableListOf())
     }
}
