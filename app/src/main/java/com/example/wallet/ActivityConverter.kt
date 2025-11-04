package com.example.wallet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.databinding.ActivityConverterBinding
import com.example.wallet.formatCurrency
import com.example.wallet.model.Wallet
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Locale

class ActivityConverter : AppCompatActivity() {

    private lateinit var binding: ActivityConverterBinding
    private val apiService by lazy { RetrofitClient.instance }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinnerToCurrency.setSelection(1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnConvert.setOnClickListener {
            handleConversion()
        }
    }

    private fun handleConversion(){
        val fromCurrency = binding.spinnerFromCurrency.selectedItem.toString()
        val toCurrency = binding.spinnerToCurrency.selectedItem.toString()
        val amountStr = binding.etAmountToConvert.text.toString()

        if (amountStr.isBlank()) {
            binding.etAmountToConvert.error = "Insira um valor"
            return
        }

        if (fromCurrency == toCurrency) {
            showToast(getString(R.string.erro_mesma_moeda))
            return
        }

        val amountToConvert = amountStr.toDoubleOrNull()
        if (amountToConvert == null || amountToConvert <= 0) {
            binding.etAmountToConvert.error = "Valor inválido"
            return
        }

        val currentBalance = Wallet.getBalance(fromCurrency)
        if (currentBalance < amountToConvert) {
            showToast(getString(R.string.erro_saldo_insuficiente))
            return
        }

        fetchConversionRate(fromCurrency, toCurrency, amountToConvert)
    }

    private fun fetchConversionRate(from: String, to: String, amount: Double){
        setLoadingState(true)

        val apiPair = "$from - $to"

        lifecycleScope.launch {
            try{
                val response = apiService.getConversionRate(apiPair)

                if(response.isSuccessful){
                    val rateInfo = response.body()?.get(apiPair.replace("-", ""))
                    val rate = rateInfo?.bid?.toDoubleOrNull()

                    if (rate != null && rate > 0){
                        val convertedAmount = amount * rate
                        Wallet.performTransaction(from, amount, to, convertedAmount)

                        val resultText = formatCurrency(to, convertedAmount)
                        binding.tvConversionResult.text = getString(R.string.resultado_da_conversao) + "\n" + resultText

                    }else{
                        showToast(getString(R.string.erro_api))
                    }
                }else{
                    showToast(getString(R.string.erro_api))
                }
            }catch(e: Exception){
                e.printStackTrace()
                showToast(getString(R.string.erro_api))
            }finally{
                setLoadingState(false)
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean){
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        binding.tvConversionResult.visibility = if(isLoading) View.VISIBLE else View.GONE
        binding.btnConvert.isEnabled = !isLoading
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}