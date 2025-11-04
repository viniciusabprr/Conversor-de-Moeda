package com.example.wallet.model

import java.util.concurrent.ConcurrentHashMap

object Wallet {
    private val balances = ConcurrentHashMap<String, Double>()

    init{
        balances["BRL"] = 100000.00
        balances["USD"] = 1000.00
        balances["BTC"] = 0.5
    }

    fun getBalance (currency: String) : Double{
        return balances[currency] ?: 0.0;
    }

    fun performTransaction (fromCurrency: String, fromAmount: Double, toCurrency: String, amount: Double){
        val currentFrom = getBalance(fromCurrency);
        val currentTo = getBalance(toCurrency);

        balances[fromCurrency] = currentFrom - fromAmount;
        balances[toCurrency] = currentTo + amount;
    }
}