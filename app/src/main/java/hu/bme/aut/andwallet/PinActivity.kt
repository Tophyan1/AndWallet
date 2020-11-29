package hu.bme.aut.andwallet

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.andwallet.fragments.InitPinDialogFragment
import kotlinx.android.synthetic.main.activity_pin.*

class PinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)
        pref = getSharedPreferences("app", MODE_PRIVATE)

        enterButton.setOnClickListener {
            val pin = pref.getInt("PIN", -1)
            if (pinText.text.toString().toInt() == pin)
                startActivity(Intent(this, MainActivity::class.java))
            else
                Snackbar.make(enterButton, "Wrong PIN!", Snackbar.LENGTH_LONG).show()
        }
    }

    private lateinit var pref: SharedPreferences

    override fun onResume() {
        super.onResume()
        if (pref.getBoolean("firstLaunch", true)) {
            InitPinDialogFragment().show(supportFragmentManager, InitPinDialogFragment.TAG)
            pref.edit().putBoolean("firstLaunch", false).apply()
        }
    }
}