package com.amrabed.tripplan.data.repositories

import com.amrabed.tripplan.core.Trip
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object TripRepository {
    private const val TRIPS = "trips"
    private val users = Firebase.firestore.collection("users")

    fun read(userId: String, listener: EventListener<QuerySnapshot>) =
        users.document(userId).collection(TRIPS).addSnapshotListener(listener)

    fun update(userId: String, trip: Trip) =
        users.document(userId).collection(TRIPS).document(trip.id).set(trip, SetOptions.merge())

    fun delete(userId: String, trip: Trip) =
        users.document(userId).collection(TRIPS).document(trip.id).delete()
}