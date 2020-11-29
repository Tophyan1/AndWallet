package hu.bme.aut.andwallet.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hu.bme.aut.andwallet.PiggyBankActivity
import hu.bme.aut.andwallet.R
import kotlinx.android.synthetic.main.fragment_no_piggy_bank.*

class NoPiggyBankFragment : Fragment(R.layout.fragment_no_piggy_bank) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton.setOnClickListener {
            NewPiggyBankDialogFragment(activity as PiggyBankActivity).show(
                (activity as PiggyBankActivity).supportFragmentManager,
                NewPiggyBankDialogFragment.TAG
            )
        }
    }

    companion object {
        const val TAG = "NoPiggyBankFragment"
    }

}