package com.hane24.hoursarenotenough24

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.hane24.hoursarenotenough24.databinding.ActivityMainLicenseDialogBinding
import com.hane24.hoursarenotenough24.databinding.ActivityMainLicenseFormatBinding

class LicenseDialogFragment : DialogFragment() {
    private val binding by lazy { ActivityMainLicenseDialogBinding.inflate(layoutInflater) }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context).create()

        binding.licenseCancel.setOnClickListener {
            dialog.dismiss()
            dialog.cancel()
        }

        val items = listOf(Pair("Retrofit2", getString(R.string.license_retrofit2)))
        binding.licenseRecyclerView.adapter = LicenseAdapter(items)

        dialog.setView(binding.root)
        dialog.create()
        return dialog
    }
}

class LicenseAdapter(val items: List<Pair<String, String>>) :
    RecyclerView.Adapter<LicenseAdapter.LicenseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicenseViewHolder =
        LicenseViewHolder(
            ActivityMainLicenseFormatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LicenseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class LicenseViewHolder(private val binding: ActivityMainLicenseFormatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<String, String>) {
            binding.nameText.text = item.first
            binding.contentText.text = item.second
        }
    }
}