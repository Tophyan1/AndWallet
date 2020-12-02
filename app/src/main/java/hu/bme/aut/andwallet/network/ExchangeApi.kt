package hu.bme.aut.andwallet.network

import hu.bme.aut.andwallet.data.MoneyResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApi {
    @GET("/latest")
    fun getRates(
        @Query("base") base: String
    ): Call<MoneyResult>
}