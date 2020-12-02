package hu.bme.aut.andwallet.network

import android.os.Handler
import hu.bme.aut.andwallet.data.ExchangeRates
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val SERVICE_URL = "https://www.alphavantage.co"
    private const val FUNCTION = "CURRENCY_EXCHANGE_RATE"
    private const val API_KEY = "800S7H8QCQRK9NJB"

    private val exchangeApi: ExchangeApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        exchangeApi = retrofit.create(ExchangeApi::class.java)
    }

    private fun <T> runCallOnBackgroundThread(
        call: Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val handler = Handler()
        Thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }.start()
    }

    fun getRates(
        currency: String?,
        onSuccess: (ExchangeRates) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val getRateRequest = exchangeApi.getRates(FUNCTION, currency, "HUF", API_KEY)
        runCallOnBackgroundThread(getRateRequest, onSuccess, onError)
    }

}