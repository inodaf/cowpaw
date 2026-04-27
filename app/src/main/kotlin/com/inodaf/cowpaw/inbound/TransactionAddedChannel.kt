package com.inodaf.cowpaw.inbound

import kotlinx.coroutines.flow.MutableSharedFlow

class TransactionAddedChannel {
    val flow = MutableSharedFlow<Unit>();

    suspend fun emit() = flow.emit(Unit)
}