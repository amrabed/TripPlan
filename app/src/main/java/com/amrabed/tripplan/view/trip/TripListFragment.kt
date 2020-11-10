package com.amrabed.tripplan.view.trip

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrabed.tripplan.R
import com.amrabed.tripplan.auth.Authenticator
import com.amrabed.tripplan.core.Trip
import com.amrabed.tripplan.data.models.TripViewModel
import com.amrabed.tripplan.databinding.ListHeaderBinding
import com.amrabed.tripplan.databinding.TripListElementBinding
import com.amrabed.tripplan.databinding.TripListFragmentBinding
import kotlinx.android.synthetic.main.trip_list_fragment.*
import kotlinx.android.synthetic.main.trip_list_fragment.view.*

/**
 * Trip List View
 */
class TripListFragment : Fragment() {
    private val viewModel by activityViewModels<TripViewModel>()
    private val userId by lazy { arguments?.getString("user") ?: Authenticator.user!!.uid }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.trips, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> findNavController().navigate(R.id.filterDialog)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        TripListFragmentBinding.inflate(inflater, parent, false).apply {
            viewModel.userId = userId
            model = viewModel
            lifecycleOwner = viewLifecycleOwner
            swipeToRefresh.setOnRefreshListener { refresh() }
            addFab.setOnClickListener { Handler().showTripDetails(Trip(), true) }
            listenForTrips()
        }.root

    override fun onResume() {
        super.onResume()
        loadUserTrips()
    }

    private fun refresh() {
        view?.swipeToRefresh?.isRefreshing = true
        loadUserTrips()
        view?.swipeToRefresh?.isRefreshing = false
    }

    private fun loadUserTrips() {
        viewModel.userId = userId
        viewModel.loadTrips()
    }

    private fun listenForTrips() =
        viewModel.tripList.observe(viewLifecycleOwner) { groups ->
            if (groups.isEmpty()) {
                emptyTripList.visibility = View.VISIBLE
                listView.adapter = null
            } else {
                emptyTripList.visibility = View.GONE
                listView.adapter = ConcatAdapter(groups.map {
                    TripListAdapter(it.value, getString(it.key.text))
                })
            }
        }

    inner class Handler {
        fun showTripDetails(trip: Trip, inEditMode: Boolean) {
            viewModel.edit(inEditMode)
            viewModel.select(trip)
            findNavController().navigate(R.id.showDetails)
        }
    }

    inner class TripListAdapter(private val trips: MutableList<Trip>, private val header: String) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, type: Int) = if (type == 0)
            TripViewHolder(TripListElementBinding.inflate(layoutInflater, parent, false))
        else HeaderViewHolder(ListHeaderBinding.inflate(layoutInflater, parent, false))

        override fun getItemCount() = trips.size + 1
        override fun getItemViewType(position: Int) = if (position == 0) 1 else 0

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is HeaderViewHolder) {
                holder.bind(header)
            } else if (holder is TripViewHolder) {
                holder.bind(trips[position - 1])
            }
        }
    }

    inner class TripViewHolder(private val binding: TripListElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: Trip) {
            binding.trip = trip
            binding.handler = Handler()
        }
    }

    inner class HeaderViewHolder(private val binding: ListHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: String) {
            binding.header = header
        }
    }
}
