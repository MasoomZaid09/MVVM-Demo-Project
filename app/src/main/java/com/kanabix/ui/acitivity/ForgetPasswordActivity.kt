package com.kanabix.ui.acitivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kanabix.R
import com.kanabix.api.request.ForgetPasswordRequest
import com.kanabix.databinding.ActivityForgetPasswordBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation.forgetPasswordEmail
import com.kanabix.validations.FormValidation.forgetPasswordPhone
import com.kanabix.viewModel.ForgetPasswordViewModel
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding

    var flag = false
    var roleFlag = ""
    var specialFlag = ""

    private val viewModel: ForgetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        handleTitle()
        ObserveResponce()


        if (flag) {
            binding.phoneLayout.visibility = View.VISIBLE
            binding.emailLayout.visibility = View.GONE

            binding.tvEmail.visibility = View.GONE
            binding.tvPhoneNumber.visibility = View.GONE

            binding.etPhone.addTextChangedListener(textWatcher)


        } else if (specialFlag.equals("DeliveryPartner")) {
            binding.phoneLayout.visibility = View.VISIBLE
            binding.emailLayout.visibility = View.GONE

            binding.tvEmail.visibility = View.GONE
            binding.tvPhoneNumber.visibility = View.GONE

            binding.etPhone.addTextChangedListener(textWatcher)

        } else if (!flag) {
            binding.phoneLayout.visibility = View.GONE
            binding.emailLayout.visibility = View.VISIBLE

            binding.tvEmail.visibility = View.GONE
            binding.tvPhoneNumber.visibility = View.GONE

            binding.etEmail.addTextChangedListener(textWatcher)
        }

        binding.txtBackToLogin.setOnClickListener {

            if (roleFlag.equals("Customer")) {

               finish()

            } else {

                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("roleFlag", roleFlag)
                startActivity(intent)
                finish()
            }
        }

        binding.btnSend.setOnClickListener {

            if (roleFlag.equals("Customer")) {

                if (flag) {

                    if (forgetPasswordPhone(binding.etPhone, binding.tvPhoneNumber)) {

                        SavedPrefManager.saveStringPreferences(
                            this@ForgetPasswordActivity,
                            SavedPrefManager.MobileNo,
                            binding.etPhone.text.toString()
                        )

                        val request = ForgetPasswordRequest()
                        request.email = binding.etPhone.text.toString()
                        request.userType = "CUSTOMER"

                        viewModel.forgetPasswordApi(request)
                    }

                } else {

                    if (forgetPasswordEmail(binding.etEmail, binding.tvEmail)) {

                        SavedPrefManager.saveStringPreferences(
                            this@ForgetPasswordActivity,
                            SavedPrefManager.EmailId,
                            binding.etEmail.text.toString()
                        )

                        val request = ForgetPasswordRequest()
                        request.email = binding.etEmail.text.toString()
                        request.userType = "CUSTOMER"

                        viewModel.forgetPasswordApi(request)
                    }
                }

            } else {
                if (forgetPasswordPhone(binding.etPhone, binding.tvPhoneNumber)) {

                    SavedPrefManager.saveStringPreferences(
                        this@ForgetPasswordActivity,
                        SavedPrefManager.MobileNo,
                        binding.etPhone.text.toString()
                    )

                    val request = ForgetPasswordRequest()
                    request.email = binding.etPhone.text.toString()
                    request.userType = "DELIVERY_PARTNER"

                    viewModel.forgetPasswordApi(request)
                }
            }
        }
    }

    fun handleTitle(){
        if (roleFlag.equals("Customer")){

            if (flag){
                binding.subHeaderTitle.text = getString(R.string.please_enter_the_phone_number_you_d_like_your_password_reset_information_sent_to)
            }else{
                binding.subHeaderTitle.text = getString(R.string.please_enter_the_email_address_you_d_like_your_password_reset_information_sent_to)
            }
        }
        else if(roleFlag.equals("DeliveryPartner")){
            binding.subHeaderTitle.text = getString(R.string.please_enter_the_phone_number_you_d_like_your_password_reset_information_sent_to)
        }

    }

    private fun getIntentData() {

        intent.getBooleanExtra("flag", flag)?.let {
            flag = it
        }

        intent.getStringExtra("special")?.let {
            specialFlag = it
        }

        intent.getStringExtra("roleFlag")?.let {
            roleFlag = it
        }
    }

    private fun ObserveResponce() {

        lifecycleScope.launch {
            viewModel._ForgetPasswordStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200) {

                            if (flag) {
                                Intent(
                                    this@ForgetPasswordActivity,
                                    OTPVerificationActivity::class.java
                                ).also {
                                    it.putExtra("roleFlag", roleFlag)
                                    it.putExtra("phoneOrEmail", flag)
                                    startActivity(it)
                                    finish()
                                }
                            } else {
                                Intent(
                                    this@ForgetPasswordActivity,
                                    OTPVerificationActivity::class.java
                                ).also {
                                    it.putExtra("roleFlag", roleFlag)
                                    it.putExtra("phoneOrEmail", flag)
                                    startActivity(it)
                                    finish()
                                }
                            }
                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@ForgetPasswordActivity)
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
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (roleFlag.equals("Customer")) {

                if (flag) {

                    forgetPasswordPhone(binding.etPhone, binding.tvPhoneNumber)

                } else {
                    forgetPasswordEmail(binding.etEmail, binding.tvEmail)
                }

            } else {
                forgetPasswordPhone(binding.etPhone, binding.tvPhoneNumber)
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }



}