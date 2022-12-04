package com.kanabix.ui.fragment.account

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kanabix.R
import com.kanabix.databinding.FragmentChangepasswordBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation
import com.kanabix.validations.FormValidation.ChangePassword
import com.kanabix.viewModel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Changepassword : Fragment() ,sessionExpiredListener {

    private var odlpassword = 0
    private var newpassword = 0
    private var confimrpassword = 0
    private var token = ""
    private var _binding: FragmentChangepasswordBinding? = null
    private val binding get() = _binding!!


    // toolbar
    lateinit var iconsLayout: LinearLayout
    lateinit var profile_ll: LinearLayout
    lateinit var Back: LinearLayout
    lateinit var PreLoginTitle_TextView2: TextView

    // delivery toolbar
    lateinit var Back_Delivery: LinearLayout
    lateinit var iconsLayout1: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChangepasswordBinding.inflate(layoutInflater, container, false)
        val view = binding.root


        // added textwatchers on editable fields
        binding.etOldPassword.addTextChangedListener(textWatcher)
        binding.etNewPassword.addTextChangedListener(textWatcher)
        binding.confirmPassword.addTextChangedListener(textWatcher)


        if (SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.ROLE)
                .equals("Customer")
        ) {
            setToolBar()

            Back.setOnClickListener {
                findNavController().popBackStack()
            }
        } else {
            setToolBarDeliveryPartner()

            Back_Delivery.setOnClickListener {
                findNavController().popBackStack()
            }
        }


        // added back
        CLICKEVENT()


        binding.btnSave.setOnClickListener {

            if (ChangePassword(
                    binding.etOldPassword,
                    binding.tvOldPassword,
                    binding.etNewPassword,
                    binding.tvNewPassword,
                    binding.confirmPassword,
                    binding.tvConfirmPassword
                )
            ) {
                token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()

                viewModel.ChangePasswordApi(
                    token,
                    binding.etOldPassword.text.toString(),
                    binding.etNewPassword.text.toString(),
                    binding.confirmPassword.text.toString()
                )
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponce()
    }

    private fun ObserveResponce() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel._ChangePasswordStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {
                                androidExtension.updateBox(
                                    response.data.responseMessage,
                                    requireContext(),
                                    findNavController()
                                )
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@Changepassword).show(it, "MyCustomFragment")
                                }
                            }
                        }


                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            ProgressBar.showProgress(requireContext())
                        }

                        is Resource.Empty -> {
                            ProgressBar.hideProgress()
                        }

                    }

                }
            }
        }
    }

    private fun CLICKEVENT() {
        binding.imgEye.setOnClickListener {
            if (odlpassword == 0) {
                binding.etOldPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etOldPassword.setSelection(binding.etOldPassword.length())
                odlpassword = 1

            } else if (odlpassword == 1) {
                binding.etOldPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic__cross_eye))
                binding.etOldPassword.setSelection(binding.etOldPassword.length())
                odlpassword = 0
            } else {
                binding.etOldPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.imgEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etOldPassword.setSelection(binding.etOldPassword.length())
                odlpassword = 1
            }

        }
        binding.neweye.setOnClickListener {
            if (newpassword == 0) {
                binding.etNewPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.neweye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etNewPassword.setSelection(binding.etNewPassword.length())
                newpassword = 1

            } else if (newpassword == 1) {
                binding.etNewPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.neweye.setImageDrawable(resources.getDrawable(R.drawable.ic__cross_eye))
                binding.etNewPassword.setSelection(binding.etNewPassword.length())
                newpassword = 0
            } else {
                binding.etNewPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.neweye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etNewPassword.setSelection(binding.etNewPassword.length())
                newpassword = 1
            }

        }
        binding.confirmeye.setOnClickListener {
            if (confimrpassword == 0) {
                binding.confirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.confirmeye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.confirmPassword.setSelection(binding.confirmPassword.length())
                confimrpassword = 1

            } else if (confimrpassword == 1) {
                binding.confirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.confirmeye.setImageDrawable(resources.getDrawable(R.drawable.ic__cross_eye))
                binding.confirmPassword.setSelection(binding.confirmPassword.length())
                confimrpassword = 0
            } else {
                binding.confirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.confirmeye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.confirmPassword.setSelection(binding.confirmPassword.length())
                confimrpassword = 1
            }

        }
    }

    fun setToolBar() {

        try {
            PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
            profile_ll = activity?.findViewById(R.id.profile_ll)!!
            iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
            Back = activity?.findViewById(R.id.imageView_back)!!
            val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
            Add_address.visibility = View.GONE

            PreLoginTitle_TextView2.setText("Change Password")
            PreLoginTitle_TextView2.visibility = View.VISIBLE
            iconsLayout.visibility = View.GONE
            profile_ll.visibility = View.GONE
            Back.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setToolBarDeliveryPartner() {

        try {

            PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
            iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
            Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

            PreLoginTitle_TextView21.setText("Change Password")
            PreLoginTitle_TextView21.visibility = View.VISIBLE
            iconsLayout1.visibility = View.GONE
            Back_Delivery.visibility = View.VISIBLE

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    // added textwatcher for validations
    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            ChangePassword(
                binding.etOldPassword,
                binding.tvOldPassword,
                binding.etNewPassword,
                binding.tvNewPassword,
                binding.confirmPassword,
                binding.tvConfirmPassword

            )

        }

        override fun afterTextChanged(p0: Editable?) {

        }
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

