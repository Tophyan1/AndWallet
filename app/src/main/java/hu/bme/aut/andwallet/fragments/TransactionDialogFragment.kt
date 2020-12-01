package hu.bme.aut.andwallet.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import hu.bme.aut.andwallet.MainActivity
import hu.bme.aut.andwallet.R
import hu.bme.aut.andwallet.data.Transaction
import java.util.*

class TransactionDialogFragment : DialogFragment() {
    interface TransactionDialogListener {
        fun onTransactionCreated(newItem: Transaction)
        fun onTransactionUpdated(item: Transaction)
    }

    companion object {
        const val NEW_TAG = "TransactionDialogFragment_new"
        const val EDIT_TAG = "TransactionDialogFragment_edit"
    }

    private lateinit var listener: TransactionDialogListener
    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var datePicker: DatePicker

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? TransactionDialogListener
            ?: throw RuntimeException("Activity must implement the TransactionDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
            .setView(getContentView())
            .setNegativeButton(R.string.cancel, null)

        when (isEditMode()) {
            true -> {
                val item = arguments!!.getSerializable(MainActivity.KEY_EDIT) as Transaction

                nameEditText.setText(item.name)
                amountEditText.setText(item.amount.toString())
                val c = GregorianCalendar()
                c.time = item.date
                datePicker.updateDate(
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                )
                builder.setTitle("Edit Transaction")
                    .setPositiveButton(R.string.ok) { _, _ ->
                        if (isValid())
                            listener.onTransactionUpdated(getTransaction(item.id))

                    }
            }
            false -> {
                builder.setTitle("New Transaction")
                    .setPositiveButton(R.string.ok) { _, _ ->
                        if (isValid())
                            listener.onTransactionCreated(getTransaction())
                    }
            }
        }

        return builder.create()
    }

    private fun isEditMode() = arguments?.containsKey(MainActivity.KEY_EDIT) == true

    private fun getTransaction(id: Long? = null) = Transaction(
        id = id,
        name = nameEditText.text.toString(),
        amount = try {
            amountEditText.text.toString().toInt()
        } catch (e: java.lang.NumberFormatException) {
            0
        },
        date = GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth).time
    )


    private fun isValid() =
        nameEditText.text.isNotEmpty()  && amountEditText.text.isNotEmpty()

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_transaction_change, null)
        nameEditText = contentView.findViewById(R.id.TransactionNameEditText)
        amountEditText = contentView.findViewById(R.id.TransactionAmountEditText)
        datePicker = contentView.findViewById(R.id.TransactionDatePicker)
        return contentView
    }
}