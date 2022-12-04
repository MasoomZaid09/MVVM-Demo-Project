package com.kanabix.ui.acitivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.Saver
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kanabix.R
import com.kanabix.api.request.ResetPasswordRequest
import com.kanabix.databinding.ActivityResetPasswordBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation.resetPassword
import com.kanabix.viewModel.OTPViewModel
import com.kanabix.viewModel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPassword : AppCompatActivity() {

    private lateinit var binding : ActivityResetPasswordBinding
    private var passwordNotVisible = 0

    var roleFlag = ""
    var emailId = ""
    var mobileNo = ""
    var flag = false

    private val viewModel : ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passwordShow()

        intent.getStringExtra("roleFlag")?.let {
            roleFlag = it
        }

        intent.getBooleanExtra("phoneOrEmail", false)?.let {
            flag = it
        }


        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etConfirmPassword.addTextChangedListener(textWatcher)

        ObserveResponce()

        binding.btnSend.setOnClickListener {
            if (roleFlag == "Customer"){

                if (resetPassword(
                        binding.etPassword,
                        binding.tvPassword,
                        binding.etConfirmPassword,
                        binding.tvConfirmPassword
                    )){

                    if (flag){
                        mobileNo = SavedPrefManager.getStringPreferences(this@ResetPassword, SavedPrefManager.MobileNo).toString()

                        val request = ResetPasswordRequest()
                        request.email = mobileNo
                        request.newPassword = binding.etConfirmPassword.text.toString()
                        request.userType = "CUSTOMER"

                        viewModel.ResetPasswordApi(request)

                    }else {
                        emailId = SavedPrefManager.getStringPreferences(this@ResetPassword, SavedPrefManager.EmailId).toString()

                        val request = ResetPasswordRequest()
                        request.email = emailId
                        request.newPassword = binding.etConfirmPassword.text.toString()
                        request.userType = "CUSTOMER"

                        viewModel.ResetPasswordApi(request)
                    }
                }

            }else{

                if (resetPassword(
                        binding.etPassword,
                        binding.tvPassword,
                        binding.etConfirmPassword,
                        binding.tvConfirmPassword
                    )){

                    mobileNo = SavedPrefManager.getStringPreferences(this@ResetPassword, SavedPrefManager.MobileNo).toString()

                    val request = ResetPasswordRequest()
                    request.email = mobileNo
                    request.newPassword = binding.etConfirmPassword.text.toString()
                    request.userType = "DELIVERY_PARTNER"

                    viewModel.ResetPasswordApi(request)
                }
            }
        }
    }

    fun passwordShow() {
        binding.imgEye.setOnClickListener {
            if (passwordNotVisible == 0) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etPassword.setSelection(binding.etPassword.length())
                passwordNotVisible = 1


            } else if (passwordNotVisible == 1) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic__cross_eye))
                binding.etPassword.setSelection(binding.etPassword.length())
                passwordNotVisible = 0
            } else {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etPassword.setSelection(binding.etPassword.length())
                passwordNotVisible = 1
            }
        }
        binding.imgConfirmEye.setOnClickListener {
            if (passwordNotVisible == 0) {
                binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.imgConfirmEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                passwordNotVisible = 1

            } else if (passwordNotVisible == 1) {
                binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.imgConfirmEye.setImageDrawable(resources.getDrawable(R.drawable.ic__cross_eye))
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                passwordNotVisible = 0

            } else {
                binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.imgConfirmEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                passwordNotVisible = 1
            }

        }

    }

    private fun ObserveResponce() {

        lifecycleScope.launch{
            viewModel._ResetPasswordStateFlow.collect{ response ->

                when(response){

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200){

                            delay(2000L)

                            if (roleFlag.equals("Customer")){

//                                val intent = Intent(this@ResetPassword, LoginActivity::class.java)
//                                intent.putExtra("roleFlag", roleFlag)
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                startActivity(intent)
                                finish()

                            }else{
                                val intent = Intent(this@ResetPassword, LoginActivity::class.java)
                                intent.putExtra("roleFlag", roleFlag)
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message,this@ResetPassword)
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

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
    }


    private val textWatcher = object  : TextWatcher{

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            resetPassword(
                binding.etPassword,
                binding.tvPassword,
                binding.etConfirmPassword,
                binding.tvConfirmPassword
            )
        }
        override fun afterTextChanged(p0: Editable?) {

        }
    }

}