package com.inodaf.cowpaw.inbound

import kotlinx.coroutines.flow.MutableSharedFlow

class TransactionAddedChannel {
    val flow = MutableSharedFlow<Unit>(extraBufferCapacity = 1);

    fun emit() = flow.tryEmit(Unit)
}