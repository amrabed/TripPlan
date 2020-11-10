package com.amrabed.tripplan.data.models

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabed.tripplan.core.Trip
import com.amrabed.tripplan.data.repositories.TripRepository
import com.amrabed.tripplan.view.trip.Filter
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {
    var selectedTrip = MutableLiveData<Trip>()
    var isEditing = MutableLiveData(false)
    var loading = MutableLiveData(false)
    lateinit var userId: String

    internal val tripList = MutableLiveData<Map<Filter, MutableList<Trip>>>().apply { loadTrips() }

    fun loadTrips(filter: (Trip) -> Boolean = { true }) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loading.postValue(true)
                TripRepository.read(userId) { snapshot, error ->
                    loading.postValue(false)
                    if (error != null) {
                        Log.w(TAG, error.toString())
                        return@read
                    }

                    tripList.postValue(snapshot!!.documents.map { d -> d.toObject<Trip>()!! }
                        .filter(filter)
                        .groupByTo(sortedMapOf()) { trip ->
                            when {
                                trip.isOngoing() -> Filter.ONGOING
                                trip.isUpcoming() -> Filter.UPCOMING
                                else -> Filter.PAST_TRIPS
                            }
                        }.filterValues { it.isNotEmpty() }
                        .mapValues {
                            it.value.sortedBy { trip -> trip.startDate }.toMutableList()
                        })
                }
            } catch (error: Exception) {
                loading.postValue(false)
            }
        }
    }

    fun applyFilter(filter: Filter) = loadTrips { trip -> filter.applyTo(trip) }
    fun update(trip: Trip) = TripRepository.update(userId, trip)

    fun select(trip: Trip) {
        selectedTrip.value = trip
    }

    fun deleteSelectedTrip() {
        if (selectedTrip.value != null) {
            TripRepository.delete(userId, selectedTrip.value!!)
        }
    }

    fun edit(value: Boolean) {
        isEditing.value = value
    }

    fun search(query: String?) {
        if (!TextUtils.isEmpty(query)) {
            loadTrips { trip -> trip.destination?.contains(query!!, true) ?: false }
        }
    }

    companion object {
        private val TAG = TripViewModel::class.java.simpleName
    }
}