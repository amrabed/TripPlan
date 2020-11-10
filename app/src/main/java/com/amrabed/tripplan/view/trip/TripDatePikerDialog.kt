package com.amrabed.tripplan.view.trip

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.amrabed.tripplan.core.Trip
import com.amrabed.tripplan.data.models.TripViewModel
import org.joda.time.LocalDate
import java.util.*

class TripDateDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel by activityViewModels<TripViewModel>()
    private val updateStartDate by lazy { arguments?.getBoolean(UPDATE_START_DATE) ?: false }
    private val date by lazy {
        LocalDate((if (updateStartDate) trip.startDate else trip.endDate) ?: Date())
    }

    private lateinit var trip: Trip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.selectedTrip.observe(activity as LifecycleOwner) { trip = it }
    }

    override fun onCreateDialog(state: Bundle?) =
        DatePickerDialog(requireContext(), this, date.year, date.monthOfYear - 1, date.dayOfMonth)


    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        if (updateStartDate) {
            trip.startDate = LocalDate(year, month + 1, day).toDate()
        } else {
            trip.endDate = LocalDate(year, month + 1, day).toDate()
        }
        viewModel.select(trip)
    }
}

const val UPDATE_START_DATE = "start date"
