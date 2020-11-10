package com.amrabed.tripplan.view.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.amrabed.tripplan.data.models.UserViewModel

class UserRoleDialog : DialogFragment() {
    private val viewModel by activityViewModels<UserViewModel>()
    private val roles by lazy { viewModel.getRoles() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setItems(roles.map { getString(it.text) }.toTypedArray()) { _, index ->
                viewModel.updateRole(roles[index])
            }.create()
}