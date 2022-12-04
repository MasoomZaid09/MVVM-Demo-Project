package com.kanabix.ui.fragment.account

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kanabix.R
import com.kanabix.adapters.OpenPopUp
import com.kanabix.api.request.EditBankDetailsRequest
import com.kanabix.databinding.FragmentViewBankDetailsBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.DialogUtils
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.popupItemClickListner
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.models.Popup
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.fragment.AddAddressFragmentArgs
import com.kanabix.ui.fragment.Tabs.MyAccount
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation
import com.kanabix.viewModel.ViewBankDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewBankDetails : Fragment() ,sessionExpiredListener{

    private var _binding: FragmentViewBankDetailsBinding? = null
    private val binding get() = _binding!!

    // toolbar
    lateinit var iconsLayout1 : LinearLayout
    lateinit var Back_Delivery : LinearLayout
    lateinit var PreLoginTitle_TextView21 : TextView

    var flag = ""
    var amount = ""
    var token = ""

    private val viewModel : ViewBankDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViewBankDetailsBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.TOKEN).toString()
        flag = arguments?.getString("settings").toString()

        setToolBar()

        Back_Delivery.setOnClickListener{
            findNavController().popBackStack()
        }


        if (flag.equals("View")){

            viewModel.viewBankDetails(token)

            binding.editLayout.visibility = View.GONE
            binding.viewLayout.visibility = View.VISIBLE
            binding.btnUpdate.visibility = View.GONE
            binding.btnEdit.visibility = View.VISIBLE


            binding.btnEdit.setOnClickListener {

                amount = "Edit"
                val bundle = bundleOf("settings" to amount)
                findNavController().navigate(R.id.viewBankDetails2, bundle)
            }

        }
        else if(flag.equals("Edit")){


            // added text watcher on editable fields

            binding.etBankName.addTextChangedListener(textWatcher)
            binding.etAccountNumber.addTextChangedListener(textWatcher)
            binding.etAccountName.addTextChangedListener(textWatcher)
            binding.etIFSC.addTextChangedListener(textWatcher)


            viewModel.viewBankDetails(token)

            PreLoginTitle_TextView21.text = "Edit Bank Details"

            binding.editLayout.visibility = View.VISIBLE
            binding.viewLayout.visibility = View.GONE
            binding.btnUpdate.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.GONE

            binding.btnUpdate.setOnClickListener {

                if(FormValidation.bank_details_validation(
                        binding.etBankName,
                        binding.tvBankName,
                        binding.etAccountNumber,
                        binding.tvAccountNumber,
                        binding.etAccountName,
                        binding.tvAccountName,
                        binding.etIFSC,
                        binding.tvBankIFSC,
                        binding.typeOfAccountSpinner,
                        binding.tvSpinner
                    )
                ){

                    val request = EditBankDetailsRequest()
                    request.businessBankingDetails.bankName = binding.etBankName.text.toString().trim()
                    request.businessBankingDetails.accountNumber = binding.etAccountNumber.text.toString().trim()
                    request.businessBankingDetails.accountHolderName = binding.etAccountName.text.toString().trim()
                    request.businessBankingDetails.bankIFSC = binding.etIFSC.text.toString().trim()
                    request.businessBankingDetails.accountType = binding.typeOfAccountSpinner.selectedItem.toString().trim()

                    viewModel.editBankDetails(token, request)
                    ObserveEditBankDetailsResponse()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveViewBankDetailsResponse()
    }


    private fun ObserveViewBankDetailsResponse() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel._viewBankDetailsStateFlow.collectLatest { response ->


                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                response.data.result.businessBankingDetails.apply {

                                    binding.bankName.text = bankName
                                    binding.accountNumber.text = accountNumber
                                    binding.accountHolderName.text = accountHolderName
                                    binding.bankIfsc.text = bankIFSC
                                    binding.typeOfAccount.text = accountType

                                    binding.etBankName.setText(bankName)
                                    binding.etAccountNumber.setText(accountNumber)
                                    binding.etAccountName.setText(accountHolderName)
                                    binding.etIFSC.setText(bankIFSC)
                                }
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewBankDetails).show(it, "MyCustomFragment")
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

    private fun ObserveEditBankDetailsResponse() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel._editBankDetailsStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                Toast.makeText(requireContext(), response.data.responseMessage, Toast.LENGTH_SHORT).show()

                                findNavController().popBackStack()
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewBankDetails).show(it, "MyCustomFragment")
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

    fun setToolBar() {

        try{
            PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!

            iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
            Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

            PreLoginTitle_TextView21.visibility = View.VISIBLE
            PreLoginTitle_TextView21.text = "View Bank Details"
            iconsLayout1.visibility=View.GONE
            Back_Delivery.visibility = View.VISIBLE
        }catch (e:Exception){
            e.printStackTrace()
        }


    }



    // added text Watcher validations

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
                binding.tvBankIFSC,
                binding.typeOfAccountSpinner,
                binding.tvSpinner
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