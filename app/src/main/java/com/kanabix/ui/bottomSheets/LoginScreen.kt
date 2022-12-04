package com.kanabix.ui.bottomSheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kanabix.R
import com.kanabix.databinding.FragmentChangepasswordBinding
import com.kanabix.databinding.FragmentLoginScreenBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.ui.acitivity.*
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation
import com.kanabix.validations.FormValidation.getString
import com.kanabix.validations.FormValidation.startActivity
import com.kanabix.viewModel.CustomerLoginViewModel
import com.kanabix.viewModel.SharedLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginScreen : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!

    private var passwordNotVisible = 0
    private var passwordNotVisible1 = 0

    var flag = false
    var roleFlag = ""
    var guestFlag = ""
    var FCMtoken = ""

    private val viewModel: CustomerLoginViewModel by viewModels()
    private val sharedLoginViewModel: SharedLoginViewModel by activityViewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginScreenBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        Firebase.messaging.isAutoInitEnabled = true

        getFCMToken()
        clicksTerms()
        passwordShow()

        binding.btnCancel.setOnClickListener {
            dismiss()
        }


        if (!SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.userEmail)
                .toString().equals("")
        ) {
            binding.etEmail.setText(
                SavedPrefManager.getStringPreferences(
                    requireContext(),
                    SavedPrefManager.userEmail
                ).toString()
            )
            binding.etPassword.setText(
                SavedPrefManager.getStringPreferences(
                    requireContext(),
                    SavedPrefManager.customerEmailPassword
                ).toString()
            )

            binding.cbRemember.isChecked = true
        }else{
            binding.cbRemember.isChecked = false
            binding.etPassword.text.clear()
        }

        // handle email and phone number tab
        binding.txtEmail.setOnClickListener {

            flag = false

            binding.viewEmail.visibility = View.VISIBLE
            binding.phoneNumberView.visibility = View.GONE

            binding.emailLayout.visibility = View.VISIBLE
            binding.phoneLayout.visibility = View.GONE

            binding.tvEmail.visibility = View.GONE
            binding.tvPassword.visibility = View.GONE
            binding.tvPhone.visibility = View.GONE

            binding.etEmail.addTextChangedListener(textWatcher)
            binding.etPassword.addTextChangedListener(textWatcher)


            if (!SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.userEmail)
                    .toString().equals("")
            ) {
                binding.etEmail.setText(
                    SavedPrefManager.getStringPreferences(
                        requireContext(),
                        SavedPrefManager.userEmail
                    ).toString()
                )
                binding.etPassword.setText(
                    SavedPrefManager.getStringPreferences(
                        requireContext(),
                        SavedPrefManager.customerEmailPassword
                    ).toString()
                )

                binding.cbRemember.isChecked = true
            }else{
                binding.cbRemember.isChecked = false
                binding.etPassword.text.clear()
            }

        }

        binding.txtPhone.setOnClickListener {

            flag = true

            binding.viewEmail.visibility = View.GONE
            binding.phoneNumberView.visibility = View.VISIBLE

            binding.emailLayout.visibility = View.GONE
            binding.phoneLayout.visibility = View.VISIBLE

            binding.tvEmail.visibility = View.GONE
            binding.tvPassword.visibility = View.GONE
            binding.tvPhone.visibility = View.GONE

            binding.txtEmail.setTextColor(resources.getColor(R.color.Heading_Color))
            binding.txtPhone.setTextColor(resources.getColor(R.color.HomeThemeColor))

            binding.etPhone.addTextChangedListener(textWatcher)
            binding.etPassword.addTextChangedListener(textWatcher)

            if (!SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.userPhone)
                    .toString().equals("")
            ) {
                binding.etPhone.setText(
                    SavedPrefManager.getStringPreferences(
                        requireContext(),
                        SavedPrefManager.userPhone
                    ).toString()
                )
                binding.etPassword.setText(
                    SavedPrefManager.getStringPreferences(
                        requireContext(),
                        SavedPrefManager.customerPhonePassword
                    ).toString()
                )

                binding.cbRemember.isChecked = true
            }else{
                binding.cbRemember.isChecked = false
                binding.etPassword.text.clear()
            }

        }

        val text1 = this.resources.getString(R.string.don_t_have_an_account_signup)
        binding.txtSignUp.setText(Html.fromHtml(text1))


        // added clicks
        binding.txtSignUp.setOnClickListener(this)
        binding.txtForgetPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponce()
    }


    private fun ObserveResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._CustomerLoginStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            hideProgressBar()

                            if (response.data?.responseCode == 200) {

                                SavedPrefManager.saveStringPreferences(
                                    requireContext(),
                                    SavedPrefManager.TOKEN,
                                    response.data.result.token
                                )

                                SavedPrefManager.saveStringPreferences(
                                    requireContext(),
                                    SavedPrefManager.flow,
                                    "LogFlow"
                                )

                                if (!response.data.result.otpVerification) {

                                    SavedPrefManager.saveStringPreferences(
                                        requireContext(),
                                        SavedPrefManager.EmailId,
                                        response.data.result.email
                                    )
                                    Intent(requireContext(), OTPVerificationActivity::class.java).also {
                                        it.putExtra("SignIN", "SignIN")
                                        startActivity(it)
                                    }
                                }

                                else if (!response.data.result.locationSet) {

                                    val intent = Intent(requireContext(), LocationActivity::class.java)
                                    intent.putExtra("flag","Login")
                                    startActivity(intent)
                                }
                                else {
                                    SavedPrefManager.savePreferenceBoolean(
                                        requireContext(),
                                        SavedPrefManager.loggedIn,
                                        true
                                    )

                                    sharedLoginViewModel.saveToken(response.data.result.token)
                                    dismiss()
                                }
                            }
                        }

                        is Resource.Error -> {

                            hideProgressBar()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
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

                Intent(requireContext(), RegisterActivity::class.java).also {
                    it.putExtra("roleFlag", "Customer")
                    startActivity(it)
                }
            }


            R.id.txtForgetPassword -> {

                if (flag) {

                    val intent = Intent(requireContext(), ForgetPasswordActivity::class.java)
                    intent.putExtra("flag", true)
                    intent.putExtra("roleFlag", "Customer")
                    startActivity(intent)

                } else {
                    val intent = Intent(requireContext(), ForgetPasswordActivity::class.java)
                    intent.putExtra("flag", false)
                    intent.putExtra("roleFlag", "Customer")
                    startActivity(intent)
                }
            }

            R.id.btnLogin -> {

                if (!flag) {
                    if (FormValidation.EmailValidations(
                            binding.etEmail,
                            binding.tvEmail,
                            binding.etPassword,
                            binding.tvPassword
                        )
                    ) {

                        if (binding.cbRemember.isChecked) {
                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.userEmail,
                                binding.etEmail.text.toString()
                            )
                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.customerEmailPassword,
                                binding.etPassword.text.toString()
                            )
                        } else {
                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.userEmail,
                                ""
                            )
                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.customerEmailPassword,
                                ""
                            )
                        }


                        viewModel.LoginApi(
                            "CUSTOMER",
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString(),
                            "Android", FCMtoken
                        )
                    }
                } else {
                    if (FormValidation.PhoneValidations(
                            binding.etPhone,
                            binding.tvPhone,
                            binding.tvEmail,
                            binding.etPassword,
                            binding.tvPassword
                        )
                    ) {
                        if (binding.cbRemember.isChecked) {
                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.userPhone,
                                binding.etPhone.text.toString()
                            )
                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.customerPhonePassword,
                                binding.etPassword.text.toString()
                            )
                        } else {

                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.userPhone,
                                ""
                            )
                            SavedPrefManager.saveStringPreferences(
                                requireContext(),
                                SavedPrefManager.customerPhonePassword,
                                ""
                            )
                        }

                        viewModel.LoginApi(
                            "CUSTOMER",
                            binding.etPhone.text.toString(),
                            binding.etPassword.text.toString(),
                            "Android", FCMtoken
                        )
                    }
                }

            }
        }
    }




    private fun clicksTerms() {
        val ss =
            SpannableString(getString(R.string.by_accessing_this_site_you_accept_the_terms_conditions_and_privacy_policy))
        val terms: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Intent(requireContext(), TermsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        val privacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Intent(requireContext(), PrivacyPolicyActivity::class.java).also {
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


    private val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (!flag) {
                FormValidation.EmailValidations(
                    binding.etEmail,
                    binding.tvEmail,
                    binding.etPassword,
                    binding.tvPassword
                )
            } else {
                FormValidation.PhoneValidations(
                    binding.etPhone,
                    binding.tvPhone,
                    binding.tvEmail,
                    binding.etPassword,
                    binding.tvPassword
                )
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }



    @SuppressLint("StringFormatInvalid")
    fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            FCMtoken = token
        })
    }

}