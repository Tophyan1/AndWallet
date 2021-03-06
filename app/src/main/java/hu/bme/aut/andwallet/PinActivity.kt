package hu.bme.aut.andwallet

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.andwallet.fragments.InitPinDialogFragment
import kotlinx.android.synthetic.main.activity_pin.*

class PinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)
        pref = getSharedPreferences(AppConstants.PREFERENCE, MODE_PRIVATE)

        enterButton.setOnClickListener {
            val pin = pref.getInt(AppConstants.PIN_PREF, -1)
            if (pinText.text.toString().toInt() == pin)
                startActivity(Intent(this, MainActivity::class.java))
            else
                Snackbar.make(enterButton, getString(R.string.wrong_pin), Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    private lateinit var pref: SharedPreferences

    override fun onResume() {
        super.onResume()
        if (pref.getBoolean(AppConstants.FIRST_LAUNCH_PREF, true)) {
            InitPinDialogFragment().show(supportFragmentManager, InitPinDialogFragment.TAG)
            pref.edit().putBoolean(AppConstants.FIRST_LAUNCH_PREF, false).apply()
        }
    }
}