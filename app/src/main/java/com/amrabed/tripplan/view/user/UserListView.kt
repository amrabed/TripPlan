package com.amrabed.tripplan.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.amrabed.tripplan.R
import com.amrabed.tripplan.core.UserProfile
import com.amrabed.tripplan.data.models.UserViewModel
import com.amrabed.tripplan.databinding.UserListElementBinding
import com.amrabed.tripplan.databinding.UserListFragmentBinding

/**
 * User List View
 */
class UserListView : Fragment() {
    private val viewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        UserListFragmentBinding.inflate(inflater, parent, false).apply {
            model = viewModel
            handler = Handler()
            lifecycleOwner = viewLifecycleOwner
            viewModel.allUsers.observe(viewLifecycleOwner) {
                listView.adapter = UserListAdapter(it)
            }
        }.root

    inner class Handler {
        fun showUserProfile(user: UserProfile) {
            viewModel.select(user)
            findNavController().navigate(R.id.userProfile)
        }

        fun createNewUser() {
            viewModel.select(UserProfile())
            findNavController().navigate(R.id.newUserProfile)
        }
    }

    inner class UserListAdapter(private val users: MutableList<UserProfile>) :
        RecyclerView.Adapter<UserViewHolder>() {
        override fun getItemCount() = users.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(UserListElementBinding.inflate(layoutInflater, parent, false))

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
            holder.bind(users[position])
    }

    inner class UserViewHolder(private val binding: UserListElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserProfile) {
            binding.user = user
            binding.handler = Handler()
        }
    }

}
