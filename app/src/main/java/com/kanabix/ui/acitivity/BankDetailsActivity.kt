package com.kanabix.ui.acitivity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kanabix.api.request.BankDetailsRequest
import com.kanabix.api.request.SignUpRequest
import com.kanabix.databinding.ActivityBankDetailsBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation
import com.kanabix.viewModel.CustomerSignInVIewModel
import com.kanabix.viewModel.FillBankDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BankDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBankDetailsBinding
    var typeofaccount = ""
    var userId = ""

    private val viewModel: FillBankDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // added text watcher
        binding.etBankName.addTextChangedListener(textWatcher)
        binding.etAccountNumber.addTextChangedListener(textWatcher)
        binding.etAccountName.addTextChangedListener(textWatcher)
        binding.etIFSC.addTextChangedListener(textWatcher)


        ObserveResponce()

        binding.typeOfAccount.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val data = parent.getItemAtPosition(pos).toString()
                typeofaccount = data
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.btnSubmit.setOnClickListener {

            if (FormValidation.bank_details_validation(
                    binding.etBankName,
                    binding.tvBankName,
                    binding.etAccountNumber,
                    binding.tvAccountNumber,
                    binding.etAccountName,
                    binding.tvAccountName,
                    binding.etIFSC,
                    binding.tvIFSC,
                    binding.typeOfAccount,
                    binding.tvAccountType
                )
            ) {

                userId = SavedPrefManager.getStringPreferences(this@BankDetailsActivity ,SavedPrefManager.userId).toString()
                val request = BankDetailsRequest()
                request.businessBankingDetails.bankName = binding.etBankName.text.toString().trim()
                request.businessBankingDetails.accountNumber =
                    binding.etAccountNumber.text.toString().trim()
                request.businessBankingDetails.accountHolderName =
                    binding.etAccountName.text.toString().trim()
                request.businessBankingDetails.bankIFSC = binding.etIFSC.text.toString().trim()
                request.businessBankingDetails.accountType = typeofaccount
                request.userId = userId
                request.flag = 1

                viewModel.FillformApi(request)

                }
            }

        }


    private fun ObserveResponce() {

        lifecycleScope.launch {
            viewModel._fillBankDetailsFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200) {

                            Intent(this@BankDetailsActivity, LocationDeliveryActivity::class.java).also {
                                startActivity(it)
                            }
                            finish()
                        }
                    }


                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@BankDetailsActivity)
                        }
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Empty -> {
                        hideProgressBar()
                    }

                }

            }
        }
    }
    private fun showProgressBar() {
        binding.progressBarBankdetails.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarBankdetails.visibility = View.GONE
    }


    // add text watcher

    private val textWatcher = object : TextWatcher{

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            FormValidation.bank_details_validation(
                binding.etBankName,
                binding.tvBankName,
                binding.etAccountNumber,
                binding.tvAccountNumber,
                binding.etAccountName,
                binding.tvAccountName,
                binding.etIFSC,
                binding.tvIFSC,
                binding.typeOfAccount,
                binding.tvAccountType
            )

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }

}


