package com.amrabed.tripplan.view.trip

import com.amrabed.tripplan.R
import com.amrabed.tripplan.core.Trip

enum class Filter(val text: Int) {
    ONGOING(R.string.ongoing),
    UPCOMING(R.string.upcoming),
    THIS_MONTH(R.string.thisMonth),
    NEXT_MONTH(R.string.nextMonth),
    LATER(R.string.later),
    PAST_TRIPS(R.string.pastTrips),
    ALL_TRIPS(R.string.allTrips);

    fun applyTo(trip: Trip) = when (this) {
        ONGOING -> trip.isOngoing()
        UPCOMING -> trip.isUpcoming()
        THIS_MONTH -> trip.isThisMonth()
        NEXT_MONTH -> trip.isNextMonth()
        LATER -> trip.isLater()
        PAST_TRIPS -> trip.isPastTrip()
        else -> true
    }
}