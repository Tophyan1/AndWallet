package hu.bme.aut.andwallet.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hu.bme.aut.andwallet.PiggyBankActivity
import hu.bme.aut.andwallet.R
import hu.bme.aut.andwallet.data.PiggyBank
import kotlinx.android.synthetic.main.fragment_piggy_bank.*

class PiggyBankFragment(private var piggyBank: PiggyBank) : Fragment(R.layout.fragment_piggy_bank) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextView.text = piggyBank.name
        progressBar.max = piggyBank.fullPrice.toFloat()
        progressBar.progress = piggyBank.progress.toFloat()
        progressBar.progressText = "${piggyBank.progress}/${piggyBank.fullPrice}"


        addButton.setOnClickListener {
            ChangeSavingsDialogFragment().show(
                (context as PiggyBankActivity).supportFragmentManager,
                ChangeSavingsDialogFragment.ADD_TAG
            )
        }

        subtractButton.setOnClickListener {
            ChangeSavingsDialogFragment().show(
                (context as PiggyBankActivity).supportFragmentManager,
                ChangeSavingsDialogFragment.SUBTRACT_TAG
            )
        }
    }

    fun updateProgress(bank: PiggyBank) {
        piggyBank = bank
        progressBar.progress = piggyBank.progress.toFloat()
        progressBar.progressText = "${piggyBank.progress}/${piggyBank.fullPrice}"
    }

    companion object {
        const val TAG = "PiggyBankFragment"
    }
}