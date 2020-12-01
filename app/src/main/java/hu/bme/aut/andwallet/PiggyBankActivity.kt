package hu.bme.aut.andwallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.andwallet.data.ApplicationDatabase
import hu.bme.aut.andwallet.data.PiggyBank
import hu.bme.aut.andwallet.fragments.ChangeSavingsDialogFragment
import hu.bme.aut.andwallet.fragments.NewPiggyBankDialogFragment
import hu.bme.aut.andwallet.fragments.NoPiggyBankFragment
import hu.bme.aut.andwallet.fragments.PiggyBankFragment
import kotlin.concurrent.thread

class PiggyBankActivity : AppCompatActivity(),
                          NewPiggyBankDialogFragment.PiggyBankDialogListener,
                          ChangeSavingsDialogFragment.ChangeSavingsDialogListener {

    private var piggyBank: PiggyBank? = null
    private val database = ApplicationDatabase.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piggy_bank)

        thread {
            piggyBank = database.applicationDao().getPiggyBank()
            runOnUiThread {
                initFragment()
            }
        }
    }

    private fun initFragment() {
        if (piggyBank == null) {
            val fragment = NoPiggyBankFragment()

            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.frame, fragment, NoPiggyBankFragment.TAG)
            ft.commit()
        } else {
            val fragment = PiggyBankFragment(piggyBank!!)

            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.frame, fragment, PiggyBankFragment.TAG)
            ft.commit()
        }
    }

    override fun onPiggyBankCreated(piggyBank: PiggyBank) {
        this.piggyBank = piggyBank
        val fragment = PiggyBankFragment(this.piggyBank!!)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame, fragment, NoPiggyBankFragment.TAG)
        ft.commit()

        thread {
            database.applicationDao().insertPiggyBank(this.piggyBank!!)
            this.piggyBank = database.applicationDao().getPiggyBank()
        }
    }

    override fun addSum(amount: Int) {
        var newProgress = piggyBank!!.progress + amount
        if (newProgress > piggyBank!!.fullPrice)
            newProgress = piggyBank!!.fullPrice
        piggyBank = piggyBank!!.copy(progress = newProgress)
        val fragment = supportFragmentManager.findFragmentById(R.id.frame) as PiggyBankFragment
        fragment.updateProgress(piggyBank!!)
        thread {
            database.applicationDao().updatePiggyBank(piggyBank!!)
        }
    }

    override fun subtractSum(amount: Int) {
        var newProgress = piggyBank!!.progress - amount
        if (newProgress < 0)
            newProgress = 0
        piggyBank = piggyBank!!.copy(progress = newProgress)
        val fragment = supportFragmentManager.findFragmentById(R.id.frame) as PiggyBankFragment
        fragment.updateProgress(piggyBank!!)
        thread {
            database.applicationDao().updatePiggyBank(piggyBank!!)
        }
    }


}