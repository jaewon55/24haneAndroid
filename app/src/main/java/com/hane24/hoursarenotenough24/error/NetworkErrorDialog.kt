package com.hane24.hoursarenotenough24.error

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hane24.hoursarenotenough24.R

class NetworkErrorDialog(private val onClick: DialogInterface.OnClickListener): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)
            builder.setMessage("네트워크가 연결되지 않았습니다.\nWi-Fi 또는 데이터를 활성화 해주세요.")
                .setPositiveButton("다시 시도", onClick)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun showNetworkErrorDialog(fragmentManager: FragmentManager, onClick: DialogInterface.OnClickListener) {
            val newDialog = NetworkErrorDialog(onClick)
            newDialog.show(fragmentManager, "network_error_dialog")
        }
    }
}