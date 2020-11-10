package com.amrabed.tripplan.view.user

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amrabed.tripplan.R
import com.amrabed.tripplan.core.UserProfile
import com.amrabed.tripplan.data.models.UserViewModel
import com.amrabed.tripplan.databinding.UserProfileEditFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.user_profile_edit_fragment.*

/**
 * Fragment to show and edit the profile of the selected user
 */
class UserProfileEditFragment : Fragment() {
    private val viewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        UserProfileEditFragmentBinding.inflate(inflater, parent, false).apply {
            model = viewModel
            lifecycleOwner = viewLifecycleOwner
            handler = Handler()
        }.root

    inner class Handler {
        fun showRoleDialog() = findNavController().navigate(R.id.changeUserRole)
        fun doneEditing(user: UserProfile) {
            val error = validate(user)
            if (error == null) {
                val isNewUser = arguments?.getBoolean(NEW_USER) ?: true
                if (isNewUser) {
                    viewModel.add(user)
                } else {
                    viewModel.update(user)
                }
                findNavController().popBackStack()// (R.id.backToUserList)
            } else {
                Snackbar.make(container, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun validate(user: UserProfile) = when {
        TextUtils.isEmpty(user.name) -> R.string.emptyName
        TextUtils.isEmpty(user.email) -> R.string.emptyEmail
        else -> null
    }
}

const val NEW_USER = "new"