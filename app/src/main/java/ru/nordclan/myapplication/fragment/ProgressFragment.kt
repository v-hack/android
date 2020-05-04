package ru.nordclan.myapplication.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.nordclan.myapplication.R

class ProgressFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext()).also {
            it.setContentView(R.layout.progress_bar)
            it.setCancelable(false)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
}
