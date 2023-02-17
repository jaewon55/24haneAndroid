package com.hane24.hoursarenotenough24.error

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hane24.hoursarenotenough24.R

class UnknownServerErrorDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val onClick = DialogInterface.OnClickListener { _, _ -> }

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)
            builder.setMessage("서버에서 알 수 없는 에러가 발생했습니다.\n증상이 계속 된다면 앱 관리자에게 문의하세요.")
                .setPositiveButton("확인", onClick)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun showUnknownServerErrorDialog(fragmentManager: FragmentManager) {
            val newDialog = UnknownServerErrorDialog()
            newDialog.show(fragmentManager, "network_error_dialog")
        }
    }
}