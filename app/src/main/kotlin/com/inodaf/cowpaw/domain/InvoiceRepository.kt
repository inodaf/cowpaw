package com.inodaf.cowpaw.domain

interface InvoiceRepository {
    fun getForCurrentMonth(): Result<Invoice>

    fun save(it: Invoice): Result<Unit>
//    fun getPast(): Result<List<Invoice>>
}
