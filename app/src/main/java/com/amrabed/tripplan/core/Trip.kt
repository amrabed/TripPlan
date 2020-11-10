package com.amrabed.tripplan.core

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize
import org.joda.time.Days
import org.joda.time.LocalDate
import java.util.*
import kotlin.random.Random

/**
 * Trip details
 */
@Parcelize
data class Trip(
    @DocumentId val id: String = UUID.randomUUID().toString(),
    var image: String = IMAGES[RANDOM.nextInt(0, IMAGES.size)],
    var destination: String? = null,
    // Using java.util.Date as per Firebase recommendation at https://github.com/firebase/firebase-admin-java/issues/86
    var startDate: Date? = null,
    var endDate: Date? = null,
    var comment: String? = null,
    var isHidden: Boolean = false
) : Parcelable {
    fun isUpcoming() = remainingDays() >= 0
    fun isOngoing(): Boolean {
        val today = LocalDate()
        return LocalDate(startDate).isBefore(today) && LocalDate(endDate).isAfter(today)
    }

    fun isThisMonth() = LocalDate().monthOfYear == LocalDate(startDate).monthOfYear
    fun isNextMonth() = LocalDate().plusMonths(1).monthOfYear == LocalDate(startDate).monthOfYear
    fun isLater() = LocalDate().plusMonths(1).monthOfYear < LocalDate(startDate).monthOfYear
    fun isPastTrip() = LocalDate().isAfter(LocalDate(endDate))

    fun summary() = if (isUpcoming()) "" + remainingDays() + " days left" else
        LocalDate(startDate).toString("MMM dd") + " - " +
                LocalDate(endDate).toString("MMM dd")

    private fun remainingDays() =
        Days.daysBetween(LocalDate.now(), LocalDate.fromDateFields(startDate)).days
}

val RANDOM = Random(System.currentTimeMillis())
val IMAGES = arrayOf(
    "https://cdn.pixabay.com/photo/2016/07/30/00/03/winding-road-1556177__340.jpg",
    "https://cdn.pixabay.com/photo/2014/08/15/11/29/sea-418742__340.jpg",
    "https://cdn.pixabay.com/photo/2014/12/15/17/16/pier-569314__340.jpg",
    "https://cdn.pixabay.com/photo/2015/11/02/18/32/water-1018808__340.jpg",
    "https://cdn.pixabay.com/photo/2018/07/14/15/27/cafe-3537801__340.jpg",
    "https://cdn.pixabay.com/photo/2014/01/04/13/34/taxi-238478__340.jpg",
    "https://cdn.pixabay.com/photo/2015/03/26/09/48/chicago-690364__340.jpg",
    "https://cdn.pixabay.com/photo/2014/07/01/12/34/street-381227__340.jpg",
    "https://cdn.pixabay.com/photo/2014/07/10/10/20/golden-gate-bridge-388917__340.jpg",
    "https://cdn.pixabay.com/photo/2017/06/26/08/43/ribblehead-viaduct-2443085__340.jpg"
)