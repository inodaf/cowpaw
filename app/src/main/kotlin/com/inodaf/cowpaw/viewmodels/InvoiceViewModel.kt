package com.inodaf.cowpaw.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inodaf.cowpaw.inbound.TransactionAddedChannel
import com.inodaf.cowpaw.usecases.GetCurrentInvoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Make it real-time by using MutableSharedFlow instead of Result and LiveData

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getCurrentInvoice: GetCurrentInvoice,
    private val transactionAddedChannel: TransactionAddedChannel
) : ViewModel() {
    var invoice = MutableLiveData<GetCurrentInvoice.Output>(); private set;

    init {
        viewModelScope.launch {
            transactionAddedChannel.flow.collect {
                getCurrentInvoice().map { invoice.value = it }
            }
        }
    }
}
