package com.amrabed.tripplan.core

import android.os.Parcelable
import com.amrabed.tripplan.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class UserProfile(
    @DocumentId var id: String = UUID.randomUUID().toString(),
    var name: String? = null,
    var email: String? = null,
    var imageURL: String? = null,
    var role: Role = Role.USER,
    var dateCreated: Date = Date(),
//    var lastSignIn: Date
) : Parcelable {
    constructor(user: FirebaseUser) : this(user.uid, user.displayName!!, user.email!!, user.photoUrl.toString())
}

enum class Role(val text: Int) {
    USER(R.string.user), MANAGER(R.string.manager), ADMIN(R.string.admin)
}