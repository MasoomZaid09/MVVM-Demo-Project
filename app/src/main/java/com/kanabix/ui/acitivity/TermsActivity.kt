package com.kanabix.ui.acitivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kanabix.databinding.ActivityTermsBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.viewModel.SharedStaticContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsBinding

    private val viewModel: SharedStaticContentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
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

                            binding.txtTerms.text = response.data.result.description

                        }

                    }
                    is Resource.Error -> {
                        ProgressBar.hideProgress()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@TermsActivity)
                        }


                    }
                    is Resource.Loading -> {
                        ProgressBar.showProgress(
                            this@TermsActivity
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