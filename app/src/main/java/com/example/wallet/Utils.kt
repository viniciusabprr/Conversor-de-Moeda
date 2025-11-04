package com.example.wallet

import java.util.Locale

    public fun formatCurrency (currencyCode: String, amount: Double): String{
        val locale = Locale("pt", "BR")
        return when (currencyCode){
            "BRL" -> String.format(locale, "R$%.2f", amount)
            "USD" -> String.format(locale, "$%.2f", amount)
            "BTC" -> String.format(locale, "%.4f", amount)
            else -> String.format(locale, "%.2f %s", amount, currencyCode)
        }
    }