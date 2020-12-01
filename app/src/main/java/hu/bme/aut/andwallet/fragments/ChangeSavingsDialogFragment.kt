package hu.bme.aut.andwallet.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import hu.bme.aut.andwallet.R

class ChangeSavingsDialogFragment : DialogFragment() {
    interface ChangeSavingsDialogListener {
        fun addSum(amount: Int)
        fun subtractSum(amount: Int)
    }

    companion object {
        const val ADD_TAG = "ChangeSavingsDialogFragment_add"
        const val SUBTRACT_TAG = "ChangeSavingsDialogFragment_subtract"
    }

    private lateinit var listener: ChangeSavingsDialogListener
    private lateinit var amountEditText: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? ChangeSavingsDialogListener
            ?: throw RuntimeException("Activity must implement ChangeSavingsDialogListener interface")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
            .setView(getContentView())
            .setNegativeButton(R.string.cancel, null)

        when (tag) {
            ADD_TAG -> {
                builder.setTitle(getString(R.string.add_amount))
                    .setPositiveButton(R.string.ok) { _, _ ->
                        val amount = getAmountFromEditText()
                        listener.addSum(amount)
                    }
            }
            SUBTRACT_TAG -> {
                builder.setTitle(getString(R.string.subtract_amount))
                    .setPositiveButton(R.string.ok) { _, _ ->
                        val amount = getAmountFromEditText()
                        listener.subtractSum(amount)
                    }
            }
            else -> {
                builder.setTitle("Oops! Try again!")
            }
        }
        return builder.create()
    }

    private fun getAmountFromEditText() = try {
        amountEditText.text.toString().toInt()
    } catch (e: NumberFormatException) {
        0
    }

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_change_savings, null)
        amountEditText = contentView.findViewById(R.id.amountEditText)
        return contentView
    }
}