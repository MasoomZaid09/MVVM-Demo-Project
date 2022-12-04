package com.kanabix.ui.fragment.orderFlow


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kanabix.R
import com.kanabix.databinding.FragmentOtpOrderScreenBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.bottomSheetClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.*
import com.kanabix.validations.FormValidation
import com.kanabix.viewModel.OrderDetailsCommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpOrderScreen(var orderId: String, var click : bottomSheetClick) : BottomSheetDialogFragment() ,sessionExpiredListener {

    private var _binding: FragmentOtpOrderScreenBinding? = null
    private val binding get() = _binding!!


    var token = ""
    var otp = ""

    private val viewModel : OrderDetailsCommonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOtpOrderScreenBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        bindProgressButton(binding.btnSubmit)
        binding.btnSubmit.attachTextChangeAnimator()

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        countDown()
        OTPTextWatchers()
        OTPClickListeners()

        Glide.with(requireContext()).load("https://media.giphy.com/media/g6g8zIimkVy7WGw2zy/giphy.gif").into(binding.imgOtp)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveVerifyesponse()
        ObserveReSendResponse()
    }


    private fun ObserveVerifyesponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._OtpVerifyStateFlow.collectLatest { response ->
                    
                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                binding.btnSubmit.hideProgress("Submit")
                                binding.btnSubmit.isClickable = true

                                click.bottomSheetListener()
                                dismiss()
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@OtpOrderScreen).show(it, "MyCustomFragment")
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.btnSubmit.hideProgress("Submit")
                            binding.btnSubmit.isClickable = true

                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                        }

                        is Resource.Empty -> {
                        }
                    }
                }
            }
        }
    }

    private fun ObserveReSendResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._OtpResendStateFlow.collectLatest { response ->


                    when (response) {

                        is Resource.Success -> {

                            hideProgressBar()

                            if (response.data?.responseCode == 200) {

                                Toast.makeText(requireContext(), response.data.responseMessage, Toast.LENGTH_SHORT).show()
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@OtpOrderScreen).show(it, "MyCustomFragment")
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
    }

    private fun OTPClickListeners(){

        binding.btnSubmit.setOnClickListener {

                if(FormValidation.otpValidation(
                        binding.etOtp1,
                        binding.etOtp2,
                        binding.etOtp3,
                        binding.etOtp4,
                        binding.tvOtp
                    )){

                    otp = binding.etOtp1.text.toString() + binding.etOtp2.text.toString() + binding.etOtp3.text.toString() + binding.etOtp4.text.toString()
                    viewModel.OtpVerifyApi(token,orderId,otp)

                    binding.btnSubmit.showProgress {
                        progressColor = Color.WHITE
                    }
                    binding.btnSubmit.isClickable = false

                }
        }

        binding.txtResendCode.setOnClickListener {

            binding.txtResendCode.isClickable = false
            binding.txtResendCode.text = "Sending..."
            countDown()

            viewModel.OtpResendApi(token,orderId)
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

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
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

    override fun sessionExpiredClick() {

        SavedPrefManager.savePreferenceBoolean(
            requireContext(),
            SavedPrefManager.loggedIn,
            false
        )

        Intent(requireContext(), RoleSelectScreen::class.java).also {
            startActivity(it)
            requireActivity().finishAffinity()
        }
    }
}