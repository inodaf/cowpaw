package com.inodaf.cowpaw.repositories

interface BalanceRepository {
    fun getCurrent(): Float
    fun setCurrent(amount: Float): Unit
}