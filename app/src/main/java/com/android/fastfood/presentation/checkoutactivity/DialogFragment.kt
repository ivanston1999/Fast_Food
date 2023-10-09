package com.android.fastfood.presentation.checkoutactivity

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SuccessDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Order Successfull")
            .setMessage("Thank You for Order")
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            .create()
    }
}
