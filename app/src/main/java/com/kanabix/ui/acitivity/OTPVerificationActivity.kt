package com.kanabix.ui.acitivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kanabix.R
import com.kanabix.api.request.OTPRequest
import com.kanabix.api.request.ResendOTPRequest
import com.kanabix.databinding.ActivityOtpverificationBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.utils.*
import com.kanabix.validations.FormValidation
import com.kanabix.validations.FormValidation.otpValidation
import com.kanabix.viewModel.OTPViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OTPVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpverificationBinding

    var roleFlag = ""
    var signInFlag = ""

    var flag = false

    var emailID = ""
    var mobileNo = ""
    var otp = ""

    private val viewModel : OTPViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntents()
        handleTitle()
        countDown()
        ObserveResponce()
        OTPTextWatchers()
        OTPClickListeners()
        
    }

    fun handleTitle(){
        if (roleFlag.equals("Customer")){

            if (flag){
                binding.subHeaderTitle.text = getString(R.string.please_enter_the_4_digits_otp_sent_on_your_registered_mobile_number)
            }else{
                binding.subHeaderTitle.text = getString(R.string.please_enter_the_4_digits_otp_sent_on_your_registered_email_address)
            }

        }else if(signInFlag.equals("SignIN")) {

            binding.subHeaderTitle.text = getString(R.string.please_enter_the_4_digits_otp_sent_on_your_registered_mobile_number)
        }
        else if(roleFlag.equals("DeliveryPartner")){

            binding.subHeaderTitle.text = getString(R.string.please_enter_the_4_digits_otp_sent_on_your_registered_mobile_number)
        }

    }

    fun getIntents(){
        intent.getStringExtra("roleFlag")?.let {
            roleFlag = it
        }
        intent.getBooleanExtra("phoneOrEmail", false)?.let{
            flag = it
        }
        intent.getStringExtra("SignIN")?.let {
            signInFlag = it
        }
    }

    private fun OTPClickListeners(){

        binding.btnSubmit.setOnClickListener {

            if (roleFlag.equals("Customer")){

                if (otpValidation(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.tvOtp)){

                    if (flag){

                        mobileNo = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.MobileNo).toString()

                        otp = binding.etOtp1.text.toString() + binding.etOtp2.text.toString() + binding.etOtp3.text.toString() + binding.etOtp4.text.toString()

                        val request = OTPRequest()
                        request.email = mobileNo
                        request.otp = otp
                        request.userType = "CUSTOMER"

                        viewModel.OtpApi(request)
                    }
                    else{

                        emailID = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.EmailId).toString()

                        otp = binding.etOtp1.text.toString() + binding.etOtp2.text.toString() + binding.etOtp3.text.toString() + binding.etOtp4.text.toString()

                        val request = OTPRequest()
                        request.email = emailID
                        request.otp = otp
                        request.userType = "CUSTOMER"

                        viewModel.OtpApi(request)
                    }
                }
            }

            else if(signInFlag.equals("SignIN")) {

                if (otpValidation(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.tvOtp)){

                    emailID = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.EmailId).toString()

                    otp = binding.etOtp1.text.toString() + binding.etOtp2.text.toString() + binding.etOtp3.text.toString() + binding.etOtp4.text.toString()

                    val request = OTPRequest()
                    request.email = emailID
                    request.otp = otp
                    request.userType = "CUSTOMER"

                    viewModel.OtpApi(request)
                }
            }

            else if(roleFlag.equals("DeliveryPartner")){

                if (otpValidation(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.tvOtp)){

                    mobileNo = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.MobileNo).toString()

                    otp = binding.etOtp1.text.toString() + binding.etOtp2.text.toString() + binding.etOtp3.text.toString() + binding.etOtp4.text.toString()

                    val request = OTPRequest()
                    request.email = mobileNo
                    request.otp = otp
                    request.userType = "DELIVERY_PARTNER"

                    viewModel.OtpApi(request)
                }
            }
        }

        binding.txtResendCode.setOnClickListener {

            binding.txtResendCode.isClickable = false
            binding.txtResendCode.text = "Sending..."
            countDown()

            val request = ResendOTPRequest()

            if (roleFlag.equals("Customer")){

                if (flag){

                    mobileNo = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.MobileNo).toString()

                    request.email = mobileNo
                    request.userType = "CUSTOMER"

                    viewModel.resendOtpApi(request)

                }else{

                    emailID = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.EmailId).toString()

                    request.email = emailID
                    request.userType = "CUSTOMER"

                    viewModel.resendOtpApi(request)
                }

            }
            else if(signInFlag.equals("SignIN")) {

                emailID = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.EmailId).toString()

                request.email = emailID
                request.userType = "CUSTOMER"

                viewModel.resendOtpApi(request)

            }
            else if(roleFlag.equals("DeliveryPartner")){

                mobileNo = SavedPrefManager.getStringPreferences(this@OTPVerificationActivity, SavedPrefManager.MobileNo).toString()

                request.email = mobileNo
                request.userType = "DELIVERY_PARTNER"

                viewModel.resendOtpApi(request)
            }
        }
    }

    private fun OTPTextWatchers(){

        binding.etOtp1.addTextChangedListener(textWatcher)
        binding.etOtp2.addTextChangedListener(textWatcher)
        binding.etOtp3.addTextChangedListener(textWatcher)
        binding.etOtp4.addTextChangedListener(textWatcher)

        binding.etOtp1.addTextChangedListener(GenericTextWatcher(binding.etOtp1, binding.etOtp2))
        binding.etOtp2.addTextChangedListener(GenericTextWatcher(binding.etOtp2, binding.etOtp3))
        binding.etOtp3.addTextChangedListener(GenericTextWatcher(binding.etOtp3, binding.etOtp4))
        binding.etOtp4.addTextChangedListener(GenericTextWatcher(binding.etOtp4, null))

        binding.etOtp1.setOnKeyListener(GenericKeyEvent(binding.etOtp1, null))
        binding.etOtp2.setOnKeyListener(GenericKeyEvent(binding.etOtp2, binding.etOtp1))
        binding.etOtp3.setOnKeyListener(GenericKeyEvent(binding.etOtp3, binding.etOtp2))
        binding.etOtp4.setOnKeyListener(GenericKeyEvent(binding.etOtp4, binding.etOtp3))

    }

    private fun ObserveResponce() {

        lifecycleScope.launch{
            viewModel._OTPStateFlow.collect{ response ->

                when(response){

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200){

                            delay(2000L)

                            SavedPrefManager.saveStringPreferences(this@OTPVerificationActivity, SavedPrefManager.TOKEN, response.data.result.token)

                            if (roleFlag.equals("Customer")){

                                Intent(this@OTPVerificationActivity, ResetPassword::class.java).also {
                                    it.putExtra("roleFlag", roleFlag)
                                    it.putExtra("phoneOrEmail", flag)
                                    startActivity(it)
                                    finish()
                                }

                            }else if(roleFlag.equals("DeliveryPartner")){
                                Intent(this@OTPVerificationActivity, ResetPassword::class.java).also {
                                    it.putExtra("roleFlag", roleFlag)
                                    startActivity(it)
                                    finish()
                                }

                            }else if(signInFlag.equals("SignIN")){

                                Intent(this@OTPVerificationActivity, LocationActivity::class.java).also {
                                    startActivity(it)
                                }
                                finish()
                            }


                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message,this@OTPVerificationActivity)
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

        lifecycleScope.launch{
            viewModel._ResendOtpStateFlow.collect{ response ->

                when(response){

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200){
                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message,this@OTPVerificationActivity)
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

//    private fun showProgressBar(){
//        binding.progressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar(){
//        binding.progressBar.visibility = View.GONE
//    }

    private fun showProgressBar(){
        ProgressBar.showProgress(this@OTPVerificationActivity)
    }

    private fun hideProgressBar(){
        ProgressBar.hideProgress()
    }

    private fun countDown() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished / 1000 < 10) {
                    binding.txtTimer.setText("00:0" + millisUntilFinished / 1000)

                } else {
                    binding.txtTimer.setText("00:" + millisUntilFinished / 1000)
                }
                binding.txtResendCode.isClickable = false
            }

            override fun onFinish() {
                binding.txtTimer.setText("00:00")
                binding.txtResendCode.isClickable = true
                binding.txtResendCode.text = "Resend Code"
            }
        }.start()
    }

    private val textWatcher = object  : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            FormValidation.otpValidation(
                binding.etOtp1,
                binding.etOtp2,
                binding.etOtp3,
                binding.etOtp4,
                binding.tvOtp
            )
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

}

