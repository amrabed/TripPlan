package com.amrabed.tripplan.data.repositories

import com.amrabed.tripplan.auth.Authenticator
import com.amrabed.tripplan.core.UserProfile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UserRepository {
    private val collection =
        if (Authenticator.user != null) Firebase.firestore.collection("users") else null

    fun create(user: UserProfile) = update(user)
    fun read(listener: EventListener<QuerySnapshot>) = collection?.addSnapshotListener(listener)
    fun update(user: UserProfile) = collection?.document(user.id)?.set(user, SetOptions.merge())
    fun delete(user: UserProfile) = collection?.document(user.id)?.delete()

    fun get(id: String, listener: EventListener<DocumentSnapshot>) =
        collection?.document(id)?.addSnapshotListener(listener)
}