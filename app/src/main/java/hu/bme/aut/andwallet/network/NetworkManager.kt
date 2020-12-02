package hu.bme.aut.andwallet.network

import android.os.Handler
import hu.bme.aut.andwallet.data.MoneyResult
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

object NetworkManager {
    private const val SERVICE_URL = "https://api.exchangeratesapi.io"

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
        thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }
    }

    fun getRates(
        currency: String,
        onSuccess: (MoneyResult) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val getRateRequest = exchangeApi.getRates(currency)
        runCallOnBackgroundThread(getRateRequest, onSuccess, onError)
    }

}