package com.example.wallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet.databinding.ActivityMainBinding
import com.example.wallet.formatCurrency
import com.example.wallet.model.Wallet

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToConverter.setOnClickListener {
            startActivity(Intent(this, ActivityConverter::class.java))
        }
    }

    override fun onResume(){
        super.onResume()
        updateWalletUi()
    }

    private fun updateWalletUi() {
        binding.tvBrlBalance.text = formatCurrency("BRL", Wallet.getBalance("BRL"))
        binding.tvUsdBalance.text = formatCurrency("USD", Wallet.getBalance("USD"))
        binding.tvBtcBalance.text = formatCurrency("BTC", Wallet.getBalance("BTC"))
    }

}