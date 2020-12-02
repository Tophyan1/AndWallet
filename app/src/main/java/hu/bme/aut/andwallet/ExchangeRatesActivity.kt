package hu.bme.aut.andwallet

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.andwallet.data.MoneyResult
import hu.bme.aut.andwallet.network.NetworkManager
import kotlinx.android.synthetic.main.activity_exchange_rates.*
import kotlinx.android.synthetic.main.rate_row.view.*

class ExchangeRatesActivity : AppCompatActivity() {

    private val currencies = listOf("USD", "EUR", "GBP")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rates)
        supportActionBar?.title = getString(R.string.exchange_rates)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        loadExchangeData()
    }

    private fun loadExchangeData() {
        for (currency in currencies) {
            NetworkManager.getRates(currency, ::displayRateData, ::showError)
        }
    }

    private fun displayRateData(exchangeRates: MoneyResult) {
        val rowItem = LayoutInflater.from(this).inflate(R.layout.rate_row, list_of_rates, false)
        rowItem.currency_code.text = exchangeRates.base
        rowItem.rate.text =
            getString(R.string.number_decimal_ft, exchangeRates.rates?.HUF?.toFloat())
        rowItem.date.text = exchangeRates.date
        list_of_rates.addView(rowItem)

    }

    private fun showError(throwable: Throwable) {
        throwable.printStackTrace()
        Toast.makeText(
            this,
            getString(R.string.network_request_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}