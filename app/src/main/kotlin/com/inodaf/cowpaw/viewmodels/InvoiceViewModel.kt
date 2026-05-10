package com.inodaf.cowpaw.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inodaf.cowpaw.inbound.TransactionAddedChannel
import com.inodaf.cowpaw.usecases.GetCurrentInvoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    val getCurrentInvoice: GetCurrentInvoice,
    val transactionAddedChannel: TransactionAddedChannel
) : ViewModel() {
    var invoice = MutableLiveData<GetCurrentInvoice.Output>(); private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            refresh()
            transactionAddedChannel.flow.collect { refresh() }
        }
    }

    private fun refresh() {
        getCurrentInvoice().map { invoice.postValue(it) }
    }
}
