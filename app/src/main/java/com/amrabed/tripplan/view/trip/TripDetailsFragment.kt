package com.amrabed.tripplan.view.trip

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amrabed.tripplan.R
import com.amrabed.tripplan.core.Trip
import com.amrabed.tripplan.data.models.TripViewModel
import com.amrabed.tripplan.databinding.TripDetailsFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.trip_details_fragment.*
import org.joda.time.LocalDate

/**
 * Fragment to show and edit the details of the selected trip
 */
class TripDetailsFragment : Fragment() {
    private val viewModel by activityViewModels<TripViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> viewModel.edit(true)
            R.id.delete -> AlertDialog.Builder(requireContext())
                .setMessage(R.string.confirmTripDelete)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deleteSelectedTrip()
                    findNavController().navigateUp()
                }
                .create().show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        TripDetailsFragmentBinding.inflate(inflater, parent, false).apply {
            model = viewModel
            lifecycleOwner = viewLifecycleOwner
            handler = Handler()
        }.root

    inner class Handler {
        fun showDialog(updateStart: Boolean) = findNavController().navigate(
            R.id.datePikerDialog,
            bundleOf(Pair(UPDATE_START_DATE, updateStart))
        )

        fun doneEditing(trip: Trip) {
            val error = validate(trip)
            if (error == null) {
                viewModel.edit(false)
                viewModel.update(trip)
                findNavController().navigateUp()
            } else {
                Snackbar.make(container, error, Snackbar.LENGTH_LONG).show()
            }
        }

        private fun validate(trip: Trip) = when {
            TextUtils.isEmpty(trip.destination) -> R.string.emptyDestination
            trip.startDate == null -> R.string.emptyStartDate
//            LocalDate.fromDateFields(trip.startDate).isBefore(LocalDate.now()) -> R.string.earlyStartDate
            trip.endDate == null -> R.string.emptyEndDate
            LocalDate.fromDateFields(trip.endDate)
                .isBefore(LocalDate.fromDateFields(trip.startDate)) -> R.string.earlyEndDate
            else -> null
        }
    }
}