package hu.bme.aut.andwallet.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import hu.bme.aut.andwallet.R
import hu.bme.aut.andwallet.data.PiggyBank

class NewPiggyBankDialogFragment(private val listener: PiggyBankDialogListener) : DialogFragment() {

    interface PiggyBankDialogListener {
        fun onPiggyBankCreated(piggyBank: PiggyBank)
    }

    companion object {
        const val TAG = "NewPiggyBankDialogFragment"
    }

    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.new_item_to_save_for))
            .setView(getContentView())
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (isValid())
                    listener.onPiggyBankCreated(
                        PiggyBank(
                            id = null,
                            name = nameEditText.text.toString(),
                            fullPrice = priceEditText.text.toString().toInt(),
                            progress = 0
                        )
                    )
            }

        return builder.create()
    }

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_new_piggy_bank, null)
        nameEditText = contentView.findViewById(R.id.newPiggyBankNameEditText)
        priceEditText = contentView.findViewById(R.id.newPiggyBankPriceEditText)
        return contentView
    }

    private fun isValid() = nameEditText.text.isNotEmpty() && priceEditText.text.isNotEmpty()
}