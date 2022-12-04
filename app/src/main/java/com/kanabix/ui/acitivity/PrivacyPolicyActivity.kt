package com.kanabix.ui.acitivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kanabix.databinding.ActivityPrivacyPolicyBinding
import com.kanabix.databinding.ActivityTermsBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.ui.fragment.products.ProductsListDirections
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.viewModel.SharedStaticContentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    private val viewModel : SharedStaticContentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.termsListApi()
        ObserverResponse()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun ObserverResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._termsListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            binding.txtPrivacyAndPolicy.text = response.data.result.description

                        }

                    }
                    is Resource.Error -> {
                        ProgressBar.hideProgress()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@PrivacyPolicyActivity)
                        }


                    }
                    is Resource.Loading -> {
                        ProgressBar.showProgress(
                            this@PrivacyPolicyActivity
                        )
                    }
                    is Resource.Empty -> {
                        ProgressBar.hideProgress()
                    }


                }


            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}