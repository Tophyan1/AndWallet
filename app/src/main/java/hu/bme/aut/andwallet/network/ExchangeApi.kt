package hu.bme.aut.andwallet.network

import hu.bme.aut.andwallet.data.ExchangeRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApi {
    @GET("/query")
    fun getRates(
        @Query("function") function: String?,
        @Query("from_currency") fromCurrency: String?,
        @Query("to_currency") toCurrency: String?,
        @Query("apikey") apiKey: String?
    ): Call<ExchangeRates>
}