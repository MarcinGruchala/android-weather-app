package com.example.weather_app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.weather_app.databinding.DialogInsertLocationBinding

private const val TAG = "WeatherForecastActivity"
class InsertLocationDialog(
    private val dialogMessage: String,
    private val btnSubmitClickListener: (String) -> Unit
): DialogFragment() {
    lateinit var binding: DialogInsertLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInsertLocationBinding.inflate(inflater, container, false)
        binding.apply {
            tvInsertLocationDialogDescription.text = dialogMessage
            btnSubmitLocality.setOnClickListener {
                val locality = etInsertLocality.text.toString()
                btnSubmitClickListener(locality)
                dismiss()
            }
            btnCalncelLocationInsert.setOnClickListener {
                dismiss()
            }
        }
        return binding.root
    }
}