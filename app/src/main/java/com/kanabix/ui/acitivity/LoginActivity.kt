package com.kanabix.ui.acitivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.complyany.entity.permission.RequestPermission
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kanabix.R
import com.kanabix.databinding.ActivityLoginBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation
import com.kanabix.viewModel.CustomerLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private var passwordNotVisible = 0
    var flag = false
    var roleFlag = ""
    var guestFlag = ""
    var FCMtoken = ""

    private val viewModels: CustomerLoginViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        getFCMToken()

        intent.getStringExtra("roleFlag")?.let {
            roleFlag = it
        }

            binding.customerTitleLayout.visibility = View.GONE
            binding.emailLayout.visibility = View.GONE
            binding.phoneLayout.visibility = View.GONE

            binding.deliveryPartnerLayout.visibility = View.VISIBLE
            binding.deliveryTitleLayout.visibility = View.VISIBLE

            binding.etDelivery.addTextChangedListener(textWatcher)
            binding.etPassword.addTextChangedListener(textWatcher)

            if (!SavedPrefManager.getStringPreferences(this@LoginActivity, SavedPrefManager.deliveryPartnerId).toString().equals("")){
                binding.etDelivery.setText(SavedPrefManager.getStringPreferences(this@LoginActivity,SavedPrefManager.deliveryPartnerId).toString())

                if(!SavedPrefManager.getStringPreferences(this@LoginActivity, SavedPrefManager.deliveryPartnerPassword).toString().equals("")){
                    binding.etPassword.setText(SavedPrefManager.getStringPreferences(this@LoginActivity,SavedPrefManager.deliveryPartnerPassword).toString())
                }

                binding.cbRemember.isChecked = true
            }else{
                binding.cbRemember.isChecked = false
            }




        ObserveResponce()
        passwordShow()
        val text1 = this.resources.getString(R.string.don_t_have_an_account_signup)
        binding.txtSignUp.setText(Html.fromHtml(text1))


        // added clicks
        binding.txtSignUp.setOnClickListener(this)
        binding.txtSwitchRoll.setOnClickListener(this)
        binding.txtForgetPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        clicksTerms()

    }

    private fun clicksTerms(){
        val ss =
            SpannableString(getString(R.string.by_accessing_this_site_you_accept_the_terms_conditions_and_privacy_policy))
        val terms: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Intent(this@LoginActivity, TermsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        val privacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Intent(this@LoginActivity, PrivacyPolicyActivity::class.java).also {
                    startActivity(it)
                }
            }

        }

        // It is used to set the span to the string
        ss.setSpan(terms, 38, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(privacy, 61, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtTerms.text = ss
        binding.txtTerms.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun ObserveResponce() {

        lifecycleScope.launch {
            viewModels._CustomerLoginStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200) {

                            SavedPrefManager.saveStringPreferences(
                                this@LoginActivity,
                                SavedPrefManager.TOKEN,
                                response.data.result.token
                            )


                                response.data.result.apply {

                                    if (userRequestStatus.equals("PENDING") && flag == 0) {
                                        Intent(this@LoginActivity, BankDetailsActivity::class.java).also {
                                            startActivity(it)
                                        }
                                    } else if (userRequestStatus.equals("PENDING") && !locationSet) {
                                        Intent(this@LoginActivity, LocationDeliveryActivity::class.java).also {
                                            startActivity(it)
                                        }
                                    } else {

                                        if (userRequestStatus.equals("PENDING")) {

                                            androidExtension.alertBox("Thanks For Connecting With Us. We will verify your account and will send your approval soon.", this@LoginActivity)

                                        } else if (userRequestStatus.equals("REJECTED")) {

                                            androidExtension.alertBox("Approval is declined by the admin please check your email to get more info.", this@LoginActivity)

                                        } else if (userRequestStatus.equals("APPROVED")) {

                                            SavedPrefManager.savePreferenceBoolean(this@LoginActivity, SavedPrefManager.loggedIn, true)

                                            val intent = Intent(this@LoginActivity, FragmentContainerDeliveryActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            startActivity(intent)
                                            finishAffinity()
                                        }
                                    }
                                }
//                            }
                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@LoginActivity)
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

    override fun onClick(itemId: View?) {

        when (itemId?.id) {

            R.id.txtSignUp -> {

                    Intent(this, RegisterActivity::class.java).also {
                        it.putExtra("roleFlag", "DeliveryPartner")
                        startActivity(it)
                    }
            }

            R.id.txtSwitchRoll -> {
                finish()
            }

            R.id.txtForgetPassword -> {

                    val intent = Intent(this, ForgetPasswordActivity::class.java)
                    intent.putExtra("special", "DeliveryPartner")
                    intent.putExtra("roleFlag", roleFlag)
                    startActivity(intent)
            }

            R.id.btnLogin -> {
                    if (FormValidation.DeliveryValidations(
                            binding.etDelivery,
                            binding.tvDelivery,
                            binding.etPassword,
                            binding.tvPassword
                        )
                    ) {
                        if (binding.cbRemember.isChecked) {
                            SavedPrefManager.saveStringPreferences(
                                this@LoginActivity,
                                SavedPrefManager.deliveryPartnerId,
                                binding.etDelivery.text.toString()
                            )
                            SavedPrefManager.saveStringPreferences(
                                this@LoginActivity,
                                SavedPrefManager.deliveryPartnerPassword,
                                binding.etPassword.text.toString()
                            )
                        }else {
                            SavedPrefManager.saveStringPreferences(
                                this@LoginActivity,
                                SavedPrefManager.deliveryPartnerId,
                                ""
                            )
                            SavedPrefManager.saveStringPreferences(
                                this@LoginActivity,
                                SavedPrefManager.deliveryPartnerPassword,
                                ""
                            )
                        }

                        viewModels.LoginApi(
                            "DELIVERY_PARTNER",
                            binding.etDelivery.text.toString(),
                            binding.etPassword.text.toString(),
                        "Android",""
                        )
//                    }
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

    fun passwordShow() {
        binding.imgEye.setOnClickListener {
            if (passwordNotVisible == 0) {
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etPassword.setSelection(binding.etPassword.length())
                passwordNotVisible = 1

            } else if (passwordNotVisible == 1) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic__cross_eye))
                binding.etPassword.setSelection(binding.etPassword.length())
                passwordNotVisible = 0
            } else {
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etPassword.setSelection(binding.etPassword.length())
                passwordNotVisible = 1
            }
        }
    }


    private val textWatcher = object  : TextWatcher{

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            if (roleFlag.equals("Customer")) {
//
//                if (!flag) {
//                    FormValidation.EmailValidations(
//                            binding.etEmail,
//                            binding.tvEmail,
//                            binding.etPassword,
//                            binding.tvPassword
//                        )
//                } else {
//                    FormValidation.PhoneValidations(
//                            binding.etPhone,
//                            binding.tvPhone,
//                            binding.etPassword,
//                            binding.tvPassword
//                        )
//                }
//            }
//            else {
                FormValidation.DeliveryValidations(
                        binding.etDelivery,
                        binding.tvDelivery,
                        binding.etPassword,
                        binding.tvPassword
                    )
//            }

        }
        override fun afterTextChanged(p0: Editable?) {

        }
    }

}