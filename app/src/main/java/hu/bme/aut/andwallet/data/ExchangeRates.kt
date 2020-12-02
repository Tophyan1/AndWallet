package hu.bme.aut.andwallet.data


data class ExchangeRates(val CurrencyExchangeRate: RealtimeCurrencyExchangeRate?)

data class RealtimeCurrencyExchangeRate(
    val FromCurrencyCode: String?,
    val FromCurrencyName: String?,
    val ToCurrencyCode: String?,
    val ToCurrencyName: String?,
    val ExchangeRate: String?,
    val LastRefreshed: String?,
    val TimeZone: String?,
    val BidPrice: String?,
    val AskPrice: String?
)