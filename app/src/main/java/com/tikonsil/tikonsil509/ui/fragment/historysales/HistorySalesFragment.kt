package com.tikonsil.tikonsil509.ui.fragment.historysales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tikonsil.tikonsil509.databinding.FragmentHistorySalesBinding
import com.tikonsil.tikonsil509.presentation.historysales.HistorySalesViewModel

class HistorySalesFragment : ValidateSaleHistory<FragmentHistorySalesBinding,HistorySalesViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerFrameLayout.startShimmer()
        observehistorysales()
    }
    override fun getViewModel()=HistorySalesViewModel::class.java
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?)= FragmentHistorySalesBinding.inflate(inflater,container,false)



}