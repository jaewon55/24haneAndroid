package com.hane24.hoursarenotenough24.overview

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.hane24.hoursarenotenough24.databinding.FragmentOverviewDialogBinding

class TimeDialogFragment(private val isMonth: Boolean) : DialogFragment() {
    private val binding by lazy { FragmentOverviewDialogBinding.inflate(layoutInflater) }
    private val viewModel: OverViewViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context).create()

        val numPicker = binding.dialogNumPicker
        val currentTargetTime: Long

        numPicker.minValue = 0
        if (isMonth) {
            currentTargetTime = viewModel.getMTargetTime() ?: 0
            numPicker.maxValue = getPickerIndex(MONTH_MIN_VALUE, MONTH_MAX_VALUE, MONTH_STEP)
            numPicker.value =
                getPickerIndex(MONTH_MIN_VALUE, currentTargetTime.toInt() / 3600, MONTH_STEP)
            numPicker.displayedValues =
                getDisplayedValues(numPicker.maxValue + 1, MONTH_MIN_VALUE, MONTH_STEP)
        } else {
            currentTargetTime = viewModel.getDTargetTime() ?: 0
            numPicker.maxValue = getPickerIndex(DAY_MIN_VALUE, DAY_MAX_VALUE, DAY_STEP)
            numPicker.value =
                getPickerIndex(DAY_MIN_VALUE, currentTargetTime.toInt() / 3600, DAY_STEP)
            numPicker.displayedValues =
                getDisplayedValues(numPicker.maxValue + 1, DAY_MIN_VALUE, DAY_STEP)
        }

        binding.dialogCancel.setOnClickListener {
            // 취소 버튼
            dialog.dismiss()
            dialog.cancel()
        }

        binding.dialogSave.setOnClickListener {
            // 확인 버튼
            val select = getSelectValue(numPicker.value) * 3600L
            if (currentTargetTime != select) {
                viewModel.changeTargetTime(select, isMonth)
            }

            dialog.dismiss()
            dialog.cancel()
        }
        dialog.setView(binding.root)
        dialog.create()

        return dialog
    }

    private fun getPickerIndex(min: Int, max: Int, step: Int) = (max - min) / step

    private fun getDisplayedValues(count: Int, min: Int, step: Int) =
        Array(count) { "${min + it * step} 시간" }

    private fun getSelectValue(index: Int) =
        if (isMonth) (index * MONTH_STEP) + MONTH_MIN_VALUE else (index * DAY_STEP) + DAY_MIN_VALUE

    companion object {
        private const val MONTH_MAX_VALUE = 240
        private const val MONTH_MIN_VALUE = 20
        private const val MONTH_STEP = 10
        private const val DAY_MAX_VALUE = 23
        private const val DAY_MIN_VALUE = 1
        private const val DAY_STEP = 1
    }

}