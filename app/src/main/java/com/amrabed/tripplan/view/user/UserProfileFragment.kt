package com.amrabed.tripplan.view.user

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amrabed.tripplan.R
import com.amrabed.tripplan.data.models.UserViewModel
import com.amrabed.tripplan.databinding.UserProfileFragmentBinding

/**
 * Fragment to show and edit the profile of the selected user
 */
class UserProfileFragment : Fragment() {
    private val viewModel by activityViewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(viewModel.hasHigherRole())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> findNavController().navigate(R.id.editUserProfile)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        UserProfileFragmentBinding.inflate(inflater, parent, false).apply {
            model = viewModel
            lifecycleOwner = viewLifecycleOwner
            handler = Handler()
        }.root

    inner class Handler {
        fun showRoleDialog() = findNavController().navigate(R.id.changeUserRole)
        fun showDeleteDialog() = findNavController().navigate(R.id.deleteUser)
        fun showUserTrips() = findNavController()
            .navigate(R.id.trips, bundleOf(Pair("user", viewModel.getUserId())))
    }
}