package com.inodaf.cowpaw.domain

interface InvoiceRepository {
    fun getForCurrentMonth(): Result<Invoice>
    fun setCurrent(amount: Float): Unit

//    fun save(it: Invoice): Result<Unit>
//    fun getPast(): Result<List<Invoice>>
}