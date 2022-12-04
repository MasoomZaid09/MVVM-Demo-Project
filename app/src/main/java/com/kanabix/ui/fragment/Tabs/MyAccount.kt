package com.kanabix.ui.fragment.Tabs

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.request.AddRatingRequest
import com.kanabix.databinding.FragmentMyAccountBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.LogOutListener
import com.kanabix.interfaces.PermissionDeniedListener
import com.kanabix.interfaces.RateAppClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.MyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.io.File


@AndroidEntryPoint
class MyAccount : Fragment(), RateAppClick, PermissionDeniedListener, LogOutListener ,sessionExpiredListener {

    private lateinit var binding: FragmentMyAccountBinding

    // toolbar
    lateinit var iconsLayout: LinearLayout
    lateinit var profile_ll: LinearLayout
    lateinit var Back: LinearLayout
    lateinit var PreLoginTitle_TextView2: TextView

    lateinit var iconsLayout1: LinearLayout
    lateinit var Back_Delivery: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView

    var roleFlag = ""
    var token = ""
    var fileURL = ""
    var deviceToken = ""

    val deviceType = "Android"

    private val viewModel: MyProfileViewModel by activityViewModels()
    private val ratingViewModel: MyProfileViewModel by viewModels()

    private var isPermission = 1

    // get permission write external storage
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

            isPermission = if (it) {
                1
            } else {
                0
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyAccountBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.ROLE)?.let {
            roleFlag = it
        }

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        deviceToken = SavedPrefManager.getStringPreferences(
            requireContext(),
            SavedPrefManager.KEY_DEVICE_TOKEN
        ).toString()
        viewModel.myProfile(token)

        handleClicks()

        setToolBar(roleFlag)

        return view
    }

    private fun handleClicks() {

        if (roleFlag.equals("Customer")) {

            binding.uploadDocumentLayout.visibility = View.GONE
            binding.settings.visibility = View.GONE
            binding.changePassword.visibility = View.VISIBLE

            binding.changePassword.setOnClickListener {
                findNavController().navigate(R.id.action_myAccount_to_changepassword)
            }

            binding.refer.setOnClickListener {
                findNavController().navigate(R.id.action_myAccount_to_rerferId)
            }

            binding.logOut.setOnClickListener {
                androidExtension.logOutDialog(requireActivity(), roleFlag, this)
            }

            binding.rateUs.setOnClickListener {
                androidExtension.rateThisApp(requireContext(), this)
            }

            binding.btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_myAccount_to_editprofile)
            }

        } else {

            binding.uploadDocumentLayout.visibility = View.VISIBLE
            binding.settings.visibility = View.VISIBLE
            binding.changePassword.visibility = View.GONE

            binding.settings.setOnClickListener {
                findNavController().navigate(R.id.action_myAccount2_to_settingsFragment2)
            }

            binding.refer.setOnClickListener {
                findNavController().navigate(R.id.action_myAccount2_to_rerferId2)
            }

            binding.logOut.setOnClickListener {
                androidExtension.logOutDialog(requireActivity(), roleFlag, this)
            }

            binding.rateUs.setOnClickListener {
                androidExtension.rateThisApp(requireContext(), this)
            }

            binding.btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_myAccount2_to_editprofile2)
            }

            binding.uploadDocumentLayout.setOnClickListener {

                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (isPermission == 1) {
                    DownloadPdf(fileURL, binding.txtUploadedDocument.text.toString())
                } else {
                    androidExtension.filePermission(
                        "Permission Denied Please give access..",
                        requireContext(),
                        this
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponce()
        ObserveRatingResponce()
        ObserveLogOutResponse()
    }

    private fun ObserveResponce() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._myProfileFlow.collectLatest { response ->

                    delay(200L)
                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {

                                response.data.result.apply {

                                    Glide.with(requireContext()).load(profilePic)
                                        .error(R.drawable.place_holder).into(binding.imgProfile)
                                    binding.txtFullName.text = name
                                    binding.txtMobileNumber.text = mobileNumber
                                    binding.txtEmail.text = email
                                    binding.txtZipCode.text = zipCode.toString()
                                    binding.txtCity.text = city
                                    binding.txtState.text = state
                                    binding.txtAddress.text = address
                                    binding.txtUploadedDocument.text = fileName
                                    fileURL = govtDocument
                                }
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@MyAccount).show(it, "MyCustomFragment")
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

    private fun ObserveRatingResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            ratingViewModel._RateAppFlow.collect { response ->

                when (response) {
                    is Resource.Success -> {

                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            Toast.makeText(
                                requireContext(),
                                response.data.responseMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@MyAccount).show(it, "MyCustomFragment")
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

    private fun ObserveLogOutResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._LogOutFlow.collect { response ->

                when (response) {
                    is Resource.Success -> {

                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            if (response.data.result.loggedOut) {

                                if (roleFlag.equals("Customer")) {
                                    SavedPrefManager.savePreferenceBoolean(
                                        activity,
                                        SavedPrefManager.loggedIn,
                                        false
                                    )

                                    Intent(requireContext(), RoleSelectScreen::class.java).also {
                                        it.putExtra("roleFlag", roleFlag)
                                        startActivity(it)
                                        requireActivity().finishAffinity()
                                    }

                                } else {
                                    SavedPrefManager.savePreferenceBoolean(
                                        activity,
                                        SavedPrefManager.loggedIn,
                                        false
                                    )

                                    Intent(requireContext(), RoleSelectScreen::class.java).also {
                                        it.putExtra("roleFlag", roleFlag)
                                        startActivity(it)
                                        requireActivity().finishAffinity()
                                    }

                                }
                            }
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@MyAccount).show(it, "MyCustomFragment")
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

    private fun DownloadPdf(fileURL: String, fileName: String) {

        try {
            val downloadManager =
                requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val pdfLink = Uri.parse(fileURL)
            val request = DownloadManager.Request(pdfLink)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("file/pdf")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    File.separator + fileName + ".pdf"
                )

            downloadManager.enqueue(request)

            Toast.makeText(requireContext(), "Download Successfully", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setToolBar(roleFlag: String) {

        try {
            if (roleFlag.equals("Customer")) {

                PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
                profile_ll = activity?.findViewById(R.id.profile_ll)!!
                iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
                Back = activity?.findViewById(R.id.imageView_back)!!

                val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
                Add_address.visibility = View.GONE

                PreLoginTitle_TextView2.setText("My Account")
                PreLoginTitle_TextView2.visibility = View.VISIBLE
                iconsLayout.visibility = View.GONE
                profile_ll.visibility = View.GONE
                Back.visibility = View.GONE

            } else {

                PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
                iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
                Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

                PreLoginTitle_TextView21.setText("My Account")
                PreLoginTitle_TextView21.visibility = View.VISIBLE
                iconsLayout1.visibility = View.GONE
                Back_Delivery.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun rateThisApp(rating: Int) {

        val request = AddRatingRequest()
        request.ratingCount = rating
        ratingViewModel.MyRateApi(token, request)
    }

    override fun permissionDeniedListener() {

        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (isPermission == 1) {
            DownloadPdf(fileURL, binding.txtUploadedDocument.text.toString())
        } else {
            androidExtension.filePermission(
                "Permission Denied Please give access..",
                requireContext(),
                this
            )
        }
    }

    override fun logOutListener(roleFlag: String) {

        if (roleFlag.equals("Customer")) {
            viewModel.logOutApi(token, deviceToken, deviceType)
        } else {
            viewModel.logOutApi(token, "", "")
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