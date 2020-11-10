package com.amrabed.tripplan.view.trip

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.amrabed.tripplan.R
import com.amrabed.tripplan.data.models.TripViewModel

class FilterDialog : DialogFragment() {
    private val viewModel by activityViewModels<TripViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.filter)
            .setItems(Filter.values().map { getString(it.text) }.toTypedArray()) { _, i ->
                viewModel.applyFilter(Filter.values()[i])
            }.create()
}