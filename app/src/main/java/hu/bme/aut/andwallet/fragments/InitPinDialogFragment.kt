package hu.bme.aut.andwallet.fragments

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.andwallet.R
import kotlinx.android.synthetic.main.dialog_set_pin.*

class InitPinDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Please set your PIN")
            .setView(getContentView())
            .setPositiveButton("SET") { _, _ ->

                val pin = when(pinEditText.text.isEmpty()) {
                    true -> 1234
                    false -> pinEditText.text.toString().toInt()
                }
                val pref = context!!.getSharedPreferences("app", MODE_PRIVATE)

                pref.edit().putInt("PIN", pin).apply()
            }

        return dialog.create()
    }

    lateinit var pinText: TextView
    lateinit var pinEditText: EditText
    lateinit var toggleButton: ToggleButton


    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_set_pin, null)
        pinText = contentView.findViewById(R.id.pinText)
        pinEditText = contentView.findViewById(R.id.pinEditText)
        toggleButton = contentView.findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener {
            pinEditText.transformationMethod = when (toggleButton.isChecked) {
                true -> HideReturnsTransformationMethod.getInstance()
                false -> PasswordTransformationMethod.getInstance()
            }
        }
        return contentView
    }

    companion object {
        const val TAG = "InitPinDialogFragment"
    }
}