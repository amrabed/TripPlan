package com.amrabed.tripplan.view.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amrabed.tripplan.R
import com.amrabed.tripplan.data.models.UserViewModel

class UserDeleteDialog : DialogFragment() {
    private val viewModel by activityViewModels<UserViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext()).setMessage(R.string.confirmUserDelete)
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.delete(viewModel.selectedUser.value!!)
                findNavController().navigate(R.id.backToUserList)
            }.create()
}