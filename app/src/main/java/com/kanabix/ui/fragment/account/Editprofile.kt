package com.kanabix.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.adapters.OpenPopUp
import com.kanabix.api.response.CountryStateCityListResult
import com.kanabix.databinding.FragmentEditprofileBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.DialogUtils
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.popupItemClickListner
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.*
import com.kanabix.utils.CustomImageUtil.Companion.getBitmap
import com.kanabix.utils.ProgressBar
import com.kanabix.validations.FormValidation
import com.kanabix.viewModel.UpdateProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


@AndroidEntryPoint
class Editprofile : Fragment(), popupItemClickListner ,sessionExpiredListener {

    private var _binding: FragmentEditprofileBinding? = null
    private val binding get() = _binding!!
    var imagePath = ""
    var categoryvalue = ""
    var token = ""
    var countryCode = ""
    var stateCode = ""
    var context = this
    var imageFile: File? = null
    var photoURI: Uri? = null
    private val GALLERY = 1
    private var USER_IMAGE_UPLOADED: Boolean = false
    private var USER_DOCUMENT_UPLOADED: Boolean = false
    private var CAMERA: Int = 2
    var data: ArrayList<CountryStateCityListResult> = ArrayList()
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OpenPopUp

    // toolbar
    lateinit var iconsLayout: LinearLayout
    lateinit var profile_ll: LinearLayout
    lateinit var Back: LinearLayout
    lateinit var PreLoginTitle_TextView2: TextView

    lateinit var iconsLayout1: LinearLayout
    lateinit var Back_Delivery: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView
    var imageparts: MultipartBody.Part? = null
    var documentRequest: MultipartBody.Part? = null
    var urlLink = ""
    var uploadedDocumentLink = ""

    private val viewModel: UpdateProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditprofileBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        bindProgressButton(binding.btnUpdate)
        binding.btnUpdate.attachTextChangeAnimator()


        if (SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.ROLE).equals("Customer")) {
            setToolBar()

            Back.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.etName.addTextChangedListener(textWatcher)
            binding.etPhone.addTextChangedListener(textWatcher)
            binding.etEmail.addTextChangedListener(textWatcher)
            binding.etPincode.addTextChangedListener(textWatcher)
            binding.etAddress.addTextChangedListener(textWatcher)
            binding.txtCountry.addTextChangedListener(textWatcher)
            binding.txtState.addTextChangedListener(textWatcher)
            binding.txtCity.addTextChangedListener(textWatcher)


            binding.llUploadDocument.visibility = View.GONE

            val constraintLayout: ConstraintLayout = view.findViewById(R.id.parentLayout)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(binding.btnUpdate.id, ConstraintSet.TOP, binding.tvCity.id, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(constraintLayout)

        }
        else {

            setToolBarDelivery()

            Back_Delivery.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.etName.addTextChangedListener(textWatcher)
            binding.etPhone.addTextChangedListener(textWatcher)
            binding.etEmail.addTextChangedListener(textWatcher)
            binding.etPincode.addTextChangedListener(textWatcher)
            binding.etAddress.addTextChangedListener(textWatcher)
            binding.txtCountry.addTextChangedListener(textWatcher)
            binding.txtState.addTextChangedListener(textWatcher)
            binding.txtCity.addTextChangedListener(textWatcher)
            binding.txtEditUploadedDocument.addTextChangedListener(textWatcher)


            binding.txtEditUploadedDocument.text = ""
            binding.llUploadDocument.visibility = View.VISIBLE

            binding.btnUploadDocument.setOnClickListener {
                selectPDF()
            }
        }

        clickListsData()

        binding.btnUpdate.setOnClickListener {

            token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()

            if (SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.ROLE).equals("Customer")) {


                binding.txtEditUploadedDocument.text = "asd"

                if(FormValidation.editProfile(
                        binding.etName,
                        binding.tvName,
                        binding.etPhone,
                        binding.tvMobile,
                        binding.etEmail,
                        binding.tvEmail,
                        binding.etPincode,
                        binding.tvPincode,
                        binding.etAddress, binding.tvAddress,
                        binding.txtCountry, binding.tvCountry,
                        binding.txtState, binding.tvState,
                        binding.txtCity, binding.tvCity,
                        binding.txtEditUploadedDocument,
                        binding.tvUploadDocument
                    ))
                {

                    val jsonObject = JsonObject().apply {
                        addProperty("name", binding.etName.text.toString())
                        addProperty("mobileNumber", binding.etPhone.text.toString())
                        addProperty("countryCode", "+${binding.ccp.selectedCountryCode}")
                        addProperty("email", binding.etEmail.text.toString())
                        addProperty("zipCode", binding.etPincode.text.toString())
                        addProperty("address", binding.etAddress.text.toString())
                        addProperty("country", binding.txtCountry.text.toString())
                        addProperty("state", binding.txtState.text.toString())
                        addProperty("city", binding.txtCity.text.toString())
                        addProperty("countryIsoCode", countryCode)
                        addProperty("stateIsoCode", stateCode)

                        if (USER_IMAGE_UPLOADED) {
                            addProperty("profilePic", urlLink)
                        }
                    }
                    viewModel.updateProfile(token, jsonObject)
                }

            }else{

                if(FormValidation.editProfile(
                        binding.etName,
                        binding.tvName,
                        binding.etPhone,
                        binding.tvMobile,
                        binding.etEmail,
                        binding.tvEmail,
                        binding.etPincode,
                        binding.tvPincode,
                        binding.etAddress, binding.tvAddress,
                        binding.txtCountry, binding.tvCountry,
                        binding.txtState, binding.tvState,
                        binding.txtCity, binding.tvCity,
                        binding.txtEditUploadedDocument,
                        binding.tvUploadDocument
                    ))
                {
                    val jsonObject = JsonObject().apply {
                        addProperty("name", binding.etName.text.toString())
                        addProperty("mobileNumber", binding.etPhone.text.toString())
                        addProperty("countryCode", binding.ccp.selectedCountryCode.toString())
                        addProperty("email", binding.etEmail.text.toString())
                        addProperty("zipCode", binding.etPincode.text.toString())
                        addProperty("address", binding.etAddress.text.toString())
                        addProperty("country", binding.txtCountry.text.toString())
                        addProperty("state", binding.txtState.text.toString())
                        addProperty("city", binding.txtCity.text.toString())
                        addProperty("countryIsoCode", countryCode)
                        addProperty("stateIsoCode", stateCode)
                        addProperty("fileName", binding.txtEditUploadedDocument.text.toString())

                        if (USER_DOCUMENT_UPLOADED){
                            addProperty("govtDocument", uploadedDocumentLink)
                        }
                        if (USER_IMAGE_UPLOADED) {
                            addProperty("profilePic", urlLink)
                        }
                    }
                    viewModel.updateProfile(token, jsonObject)
                }
            }

        }

        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveProfileResponse()
        ObserveUploadFileResponce()
        ObserveUploadDocumentResponce()
        ObserveResponce()
        ObserveCountryListResponce()
        ObserveStateListResponce()
        ObserveCityListResponce()

    }

    private fun ObserveResponce() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._UpdateProfileFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {

                                androidExtension.updateBox(
                                    response.data.responseMessage,
                                    requireContext(),
                                    findNavController()
                                )
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@Editprofile).show(it, "MyCustomFragment")
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

    private fun ObserveProfileResponse(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel._getprofile.collect { response ->


                    when (response) {

                        is Resource.Success -> {

                           ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {

                                try {

                                    response.data.result.apply {

                                        try {
                                            Glide.with(context).load(profilePic).error(R.drawable.place_holder).into(binding.imgProfile)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }

                                        binding.etName.setText(name)
                                        binding.etPhone.setText(mobileNumber)
                                        binding.etEmail.setText(email)
                                        binding.etAddress.setText(address)

                                        if (!countryCode.equals("")){
                                            binding.ccp.setCountryForPhoneCode(Integer.parseInt(countryCode))

                                        }
                                        binding.txtCity.setText(city)
                                        binding.txtState.setText(state)
                                        binding.txtCountry.setText(country)
                                        binding.etPincode.setText(zipCode)
                                        binding.txtEditUploadedDocument.text = fileName
                                        stateCode = stateIsoCode
                                        this@Editprofile.countryCode = countryIsoCode
                                    }



                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@Editprofile).show(it, "MyCustomFragment")
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

    private fun ObserveUploadFileResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._UploadFileStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {


                            if (response.data?.responseCode == 200) {

//                                binding.btnUpdate.hideProgress("Update")
//                                binding.btnUpdate.isClickable = true
//
//                                binding.btnUploadDocument.hideProgress(getString(R.string.upload_documents))
//                                binding.btnUploadDocument.isClickable = true
//
//                                binding.imgSelector.visibility = View.VISIBLE
//                                binding.imageProgressBar.visibility = View.GONE

                                ProgressBar.hideProgress()

                                urlLink = response.data.result.mediaUrl
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@Editprofile).show(it, "MyCustomFragment")
                                }
                            }
                        }


                        is Resource.Error -> {

//                            binding.btnUpdate.hideProgress("Update")
//                            binding.btnUpdate.isClickable = true
//
//                            binding.btnUploadDocument.hideProgress(getString(R.string.upload_documents))
//                            binding.btnUploadDocument.isClickable = true
//
//                            binding.imgSelector.visibility = View.VISIBLE
//                            binding.imageProgressBar.visibility = View.GONE

                            ProgressBar.hideProgress()

                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
//                        showProgressBar()
                        }

                        is Resource.Empty -> {
//                        hideProgressBar()
                        }

                    }

                }
            }
        }
    }

    private fun ObserveUploadDocumentResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._UploadDocumentStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {

//                                binding.btnUpdate.hideProgress("Update")
//                                binding.btnUpdate.isClickable = true
//
//                                binding.btnUploadDocument.hideProgress(getString(R.string.upload_documents))
//                                binding.btnUploadDocument.isClickable = true
//
//                                binding.imgSelector.visibility = View.VISIBLE

                                ProgressBar.hideProgress()

                                uploadedDocumentLink = response.data.result.mediaUrl
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@Editprofile).show(it, "MyCustomFragment")
                                }
                            }
                        }


                        is Resource.Error -> {

//                            binding.btnUpdate.hideProgress("Update")
//                            binding.btnUpdate.isClickable = true
//
//                            binding.btnUploadDocument.hideProgress(getString(R.string.upload_documents))
//                            binding.btnUploadDocument.isClickable = true
//
//                            binding.imgSelector.visibility = View.VISIBLE

                            ProgressBar.hideProgress()

                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
//                        showProgressBar()
                        }

                        is Resource.Empty -> {
//                        hideProgressBar()
                        }

                    }

                }
            }
        }
    }

    private fun ObserveCountryListResponce() {

        lifecycleScope.launch {
            viewModel._CountryListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

//                        hideProgressBar()

                        if (response.data?.responseCode == 200) {

                            data = response.data.result
                            setAdapter(data)
                        }
                        else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@Editprofile).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

//                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {
//                        showProgressBar()
                    }

                    is Resource.Empty -> {
//                        hideProgressBar()
                    }

                }

            }
        }
    }

    private fun ObserveStateListResponce() {

        lifecycleScope.launch {
            viewModel._StateListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

//                        hideProgressBar()

                        if (response.data?.responseCode == 200) {
                            data = response.data.result
                            setAdapter(data)
                        }
                        else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@Editprofile).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

//                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {
//                        showProgressBar()
                    }

                    is Resource.Empty -> {
//                        hideProgressBar()
                    }

                }

            }
        }

    }

    private fun ObserveCityListResponce() {

        lifecycleScope.launch {
            viewModel._CityListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

//                        hideProgressBar()

                        if (response.data?.responseCode == 200) {
                            data = response.data.result
                            setAdapter(data)
                        }
                        else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@Editprofile).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

//                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {
//                        showProgressBar()
                    }

                    is Resource.Empty -> {
//                        hideProgressBar()
                    }

                }

            }
        }

    }






    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireContext(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            val popUpSearch = binding.findViewById<EditText>(R.id.popUpSearch)

            popUpSearch.addTextChangedListener { editable ->
                editable?.let {
                    LocalFilter.filter(editable.toString(), data, adapter)
                }
            }

            dialougbackButton.setOnClickListener {
                dialog.dismiss()
            }

            if (flag.equals("Country")) {
                dialougTitle.setText("Country")
                data.clear()
                viewModel.countryListApi()

            } else if (flag.equals("State")) {
                dialougTitle.setText("State")
                data.clear()
                viewModel.stateListApi(countryCode,"")

            } else if (flag.equals("City")) {
                dialougTitle.setText("City")
                data.clear()
                viewModel.cityListApi(countryCode, stateCode)
            }

            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setAdapter(data: ArrayList<CountryStateCityListResult>) {
        adapter = this?.let { OpenPopUp(requireContext(), data, categoryvalue, this) }!!
        recyclerView.adapter = adapter
    }

    override fun getData(data: String, flag: String, isoCode: String) {
        if (flag.equals("Country")) {
            countryCode = isoCode
            binding.txtCountry.setText(data)
            dialog.dismiss()
        } else if (flag.equals("State")) {
            stateCode = isoCode
            binding.txtState.setText(data)
            dialog.dismiss()
        } else if (flag.equals("City")) {
            binding.txtCity.setText(data)
            dialog.dismiss()
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

            PreLoginTitle_TextView2.setText("Edit Profile")
            PreLoginTitle_TextView2.visibility = View.VISIBLE
            iconsLayout.visibility = View.GONE
            profile_ll.visibility = View.GONE
            Back.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun setToolBarDelivery() {

        try {
            PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
            iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
            Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

            PreLoginTitle_TextView21.setText("Edit Profile")
            PreLoginTitle_TextView21.visibility = View.VISIBLE
            iconsLayout1.visibility = View.GONE
            Back_Delivery.visibility = View.VISIBLE

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickListsData() {

        binding.txtCountry.setOnClickListener {
            categoryvalue = "Country"
            openPopUp(categoryvalue)
        }

        binding.txtState.setOnClickListener {
            if (binding.txtCountry.text.isEmpty()) {

                binding.tvCountry.visibility = View.VISIBLE
                binding.tvCountry.text = "*Please select country."
            } else {
                binding.tvCountry.visibility = View.GONE
                binding.tvCountry.text = ""

                categoryvalue = "State"
                openPopUp(categoryvalue)
            }
        }

        binding.imgSelector.setOnClickListener {
            selectImage()
        }

        binding.txtCity.setOnClickListener {

            if (binding.txtCountry.text.isEmpty()) {

                binding.tvCountry.visibility = View.VISIBLE
                binding.tvCountry.text = "*Please select your country."

            } else if (binding.txtState.text.isEmpty()) {
                binding.tvCountry.visibility = View.GONE
                binding.tvCountry.text = ""

                binding.tvState.visibility = View.VISIBLE
                binding.tvState.text = "*Please select your state."

            } else {
                binding.tvCountry.visibility = View.GONE
                binding.tvState.visibility = View.GONE
                binding.tvCountry.text = ""
                binding.tvState.text = ""

                categoryvalue = "City"
                openPopUp(categoryvalue)
            }
        }
    }

    private fun selectImage() {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(requireContext())

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)

        val CameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        CameraButton.setOnClickListener {
////            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////            startActivityForResult(intent, CAMERA)
//
//            dialog.dismiss()
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                try {
//                    imageFile = Current_location(requireContext()).createImageFile()!!
                    imagePath = imageFile!!.absolutePath
                } catch (ex: IOException) {
                }
                // Continue only if the File was successfully created
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.kanabix.fileprovider",
                        imageFile!!
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA)
                    dialog.dismiss()
                }
            }
        }

        val GalleryButton = view.findViewById<ImageView>(R.id.choose_from_gallery)
        GalleryButton.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
            dialog.dismiss()
        }

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    private fun selectPDF() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission
                    .READ_EXTERNAL_STORAGE
            )
            != PackageManager
                .PERMISSION_GRANTED
        ) {
            // When permission is not granted
            // Result permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/pdf"
            startActivityForResult(intent, 100)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI: Uri? = data.data

                val path = Current_location(requireContext()).getPathFromURI(contentURI)

                if (path != null) {
                    imageFile = File(path)

                    try {
                        Glide.with(requireContext()).load(imageFile).into(binding.imgProfile)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                    val requestGalleryImageFile: RequestBody =
                        RequestBody.create("image/*".toMediaTypeOrNull(), imageFile!!)
                    imageparts = MultipartBody.Part.createFormData(
                        "uploaded_file",
                        imageFile!!.getName(),
                        requestGalleryImageFile
                    )

                    lifecycleScope.async(Dispatchers.IO) {
                        viewModel.UploadFileApi(imageparts)

                        withContext(Dispatchers.Main) {
//                            binding.btnUpdate.showProgress {
//                                progressColor = Color.WHITE
//                            }
//                            binding.btnUpdate.isClickable = false
//
//                            binding.imageProgressBar.visibility = View.VISIBLE
//
//                            binding.btnUploadDocument.showProgress {
//                                progressColor = Color.WHITE
//                            }
//                            binding.btnUploadDocument.isClickable = false
//                            binding.imgSelector.visibility = View.GONE

                            ProgressBar.showProgress(requireContext())
                        }
                    }.onAwait

                }
                USER_IMAGE_UPLOADED = true
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            contentURI
                        )

                    binding.imgProfile.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        else {

            if (requestCode == CAMERA) {
                try {
                    CustomImageUtil.getStreamByteFromImage(imageFile!!)
                    try {
                        Glide.with(requireContext()).load(imageFile).into(binding.imgProfile)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    val finalBitmap =
                        CustomImageUtil.modifyOrientation(getBitmap(imagePath)!!, imagePath)
                    val imageUri = CustomImageUtil.getImageUri(requireContext(), finalBitmap!!)
                    val getRealPath =
                        imageUri?.let { CustomImageUtil.getRealPathFromURI(requireContext(), it) }

                    imageFile = File(getRealPath)

                    val requestGalleryImageFile: RequestBody =
                        RequestBody.create("image/*".toMediaTypeOrNull(), imageFile!!)
                    imageparts =
                        MultipartBody.Part.createFormData(
                            "uploaded_file",
                            imageFile!!.getName(),
                            requestGalleryImageFile
                        )
                    lifecycleScope.async(Dispatchers.IO) {
                        viewModel.UploadFileApi(imageparts)

                        withContext(Dispatchers.Main) {

                            ProgressBar.showProgress(requireContext())
//                            binding.btnUpdate.showProgress {
//                                progressColor = Color.WHITE
//                            }
//                            binding.btnUpdate.isClickable = false
//
//                            binding.imageProgressBar.visibility = View.VISIBLE
//
//                            binding.btnUploadDocument.showProgress {
//                                progressColor = Color.WHITE
//                            }
//                            binding.btnUploadDocument.isClickable = false
//                            binding.imgSelector.visibility = View.GONE
                        }
                    }.onAwait


                    USER_IMAGE_UPLOADED = true


                } catch (e: java.lang.Exception) {

                }
            }

            if (requestCode == 100) {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    try {
                        if (data != null) {

                            val sUri: Uri? = data.data
                            val sPath: String = getPDFPath(sUri)!!

                            val document = (System.currentTimeMillis() / 1000).toString()
                            try {
                                val inputStream: InputStream =
                                    requireActivity().contentResolver.openInputStream(data.data!!)!!
                                val fileOutputStream = FileOutputStream(
                                    File(
                                        requireActivity().getExternalFilesDir("temp"),
                                        "$document.pdf"
                                    )
                                )
                                TakePictureUtils.copyStream(
                                    inputStream,
                                    fileOutputStream
                                )
                                fileOutputStream.close()
                                inputStream.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                            val imageFile: File = File(
                                requireActivity().getExternalFilesDir("temp"),
                                "$document.pdf"
                            )

                            binding.txtEditUploadedDocument.text = sPath
                            var fileName = sPath

                            val requestGalleryImageFile: RequestBody =
                                RequestBody.create("application/pdf".toMediaTypeOrNull(), imageFile)

                            documentRequest = MultipartBody.Part.createFormData(
                                "uploaded_file",
                                imageFile.name,
                                requestGalleryImageFile
                            )

                            lifecycleScope.launch(Dispatchers.IO) {

                                async {
                                    viewModel.UploadDocumentApi(documentRequest)

                                    withContext(Dispatchers.Main) {

                                        ProgressBar.showProgress(requireContext())


//                                        binding.btnUploadDocument.showProgress {
//                                            progressColor = Color.WHITE
//                                        }
//                                        binding.btnUploadDocument.isClickable = false
//
//                                        binding.btnUpdate.showProgress {
//                                            progressColor = Color.WHITE
//                                        }
//                                        binding.btnUpdate.isClickable = false
//                                        binding.imgSelector.visibility = View.GONE
                                    }
                                }.await()
                            }

                            USER_DOCUMENT_UPLOADED = true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.size > 0 && (grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            selectPDF()
        } else {
            selectPDF()
            Toast
                .makeText(
                    requireContext(),
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    @SuppressLint("Range")
    fun getPDFPath(uri: Uri?): String? {
        val uriString: String = uri.toString()
        val myFile = File(uriString)
        val path = myFile.absolutePath
        var displayName: String? = null

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = requireActivity().contentResolver
                    .query(uri!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))

                    val finalDisplayName = displayName
                }
            } finally {
                cursor!!.close()
            }
        }
        else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        return displayName
    }



    // added text watchers

    private val textWatcher = object  : TextWatcher{

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.ROLE).equals("Customer")){

                binding.txtEditUploadedDocument.text = "asd"

                FormValidation.editProfile(
                    binding.etName,
                    binding.tvName,
                    binding.etPhone,
                    binding.tvMobile,
                    binding.etEmail,
                    binding.tvEmail,
                    binding.etPincode,
                    binding.tvPincode,
                    binding.etAddress, binding.tvAddress,
                    binding.txtCountry, binding.tvCountry,
                    binding.txtState, binding.tvState,
                    binding.txtCity, binding.tvCity,
                    binding.txtEditUploadedDocument,
                    binding.tvUploadDocument
                )
            }else{
                FormValidation.editProfile(
                    binding.etName,
                    binding.tvName,
                    binding.etPhone,
                    binding.tvMobile,
                    binding.etEmail,
                    binding.tvEmail,
                    binding.etPincode,
                    binding.tvPincode,
                    binding.etAddress, binding.tvAddress,
                    binding.txtCountry, binding.tvCountry,
                    binding.txtState, binding.tvState,
                    binding.txtCity, binding.tvCity,
                    binding.txtEditUploadedDocument,
                    binding.tvUploadDocument
                )
            }


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