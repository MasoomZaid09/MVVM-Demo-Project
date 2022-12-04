package com.kanabix.ui.acitivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.kanabix.R
import com.kanabix.adapters.OpenPopUp
import com.kanabix.api.request.SignUpRequest
import com.kanabix.api.response.CountryStateCityListResult
import com.kanabix.databinding.ActivityRegisterBinding
import com.kanabix.extensions.DialogUtils
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.popupItemClickListner
import com.kanabix.utils.LocalFilter
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.utils.TakePictureUtils
import com.kanabix.validations.FormValidation.SignUpCustomer
import com.kanabix.validations.FormValidation.SignUpDeliveryPartner
import com.kanabix.viewModel.CustomerSignInVIewModel
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
class RegisterActivity : AppCompatActivity(), popupItemClickListner {

    private lateinit var adapter: OpenPopUp
    private lateinit var binding: ActivityRegisterBinding

    private var passwordNotVisible = 0
    var categoryvalue = ""

    var imageFile: File? = null

    var roleFlag = ""
    var countryCode = ""
    var stateCode = ""
    var urlLink = ""
    var documentRequest: MultipartBody.Part? = null
    var data: ArrayList<CountryStateCityListResult> = ArrayList()
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView


    private val viewModel: CustomerSignInVIewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("roleFlag")?.let {
            roleFlag = it
        }

        ObserveResponce()

        passwordShow()
        if (roleFlag.equals("Customer")) {

            binding.llUploadDocument.visibility = View.GONE

            binding.etName.addTextChangedListener(textWatcher)
            binding.etPhone.addTextChangedListener(textWatcher)
            binding.etEmail.addTextChangedListener(textWatcher)
            binding.etPincode.addTextChangedListener(textWatcher)
            binding.etAddress.addTextChangedListener(textWatcher)
            binding.txtCountry.addTextChangedListener(textWatcher)
            binding.txtState.addTextChangedListener(textWatcher)
            binding.txtCity.addTextChangedListener(textWatcher)
            binding.etPassword.addTextChangedListener(textWatcher)
            binding.etConfirmPassword.addTextChangedListener(textWatcher)

            binding.cbTerms.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked){
                    binding.tvTerms.visibility = View.GONE
                    binding.tvTerms.text = ""
                }else{
                    binding.tvTerms.visibility = View.VISIBLE
                    binding.tvTerms.text = "*Please accept terms and conditions"
                }
            })


        } else if (roleFlag.equals("DeliveryPartner")) {

            binding.etName.addTextChangedListener(textWatcher)
            binding.etPhone.addTextChangedListener(textWatcher)
            binding.etEmail.addTextChangedListener(textWatcher)
            binding.etPincode.addTextChangedListener(textWatcher)
            binding.etAddress.addTextChangedListener(textWatcher)
            binding.txtCountry.addTextChangedListener(textWatcher)
            binding.txtState.addTextChangedListener(textWatcher)
            binding.txtCity.addTextChangedListener(textWatcher)
            binding.txtUploadedFile.addTextChangedListener(textWatcher)
            binding.etPassword.addTextChangedListener(textWatcher)
            binding.etConfirmPassword.addTextChangedListener(textWatcher)
            binding.cbTerms.addTextChangedListener(textWatcher)

            binding.cbTerms.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked){
                    binding.tvTerms.visibility = View.GONE
                    binding.tvTerms.text = ""
                }else{
                    binding.tvTerms.visibility = View.VISIBLE
                    binding.tvTerms.text = "*Please accept terms and conditions"
                }
            })

            binding.llUploadDocument.visibility = View.VISIBLE

            binding.btnUploadDocument.setOnClickListener {
                selectPDF()
            }
        }

        bindProgressButton(binding.btnUploadDocument)
        binding.btnUploadDocument.attachTextChangeAnimator()

        ObserveUploadFileResponce()
        ObserveCountryListResponce()
        ObserveStateListResponce()
        ObserveCityListResponce()

        val text2 = this.resources.getString(R.string.already_have_an_account_login)
        binding.txtLogin.setText(Html.fromHtml(text2))

        // set colors and clicks on text view
        val ss = SpannableString(getString(R.string.i_agree_to_terms_and_conditions))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Intent(this@RegisterActivity, TermsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

        // It is used to set the span to the string
        ss.setSpan(clickableSpan, 11, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.cbTerms.text = ss
        binding.cbTerms.movementMethod = LinkMovementMethod.getInstance()


        // setup clicks

        binding.txtLogin.setOnClickListener {

            if (roleFlag.equals("Customer")) {

                finish()

            } else {

                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("roleFlag", "DeliveryPartner")
                startActivity(intent)
                finish()
            }

        }

        binding.txtCountry.setOnClickListener {
            categoryvalue = "Country"
            openPopUp(categoryvalue)
        }

        binding.txtState.setOnClickListener {
            if (binding.txtCountry.text.isEmpty()) {

                binding.tvCountry.visibility = View.VISIBLE
                binding.tvCountry.text = "*Please select your country."
            } else {
                binding.tvCountry.visibility = View.GONE
                binding.tvCountry.text = ""

                categoryvalue = "State"
                openPopUp(categoryvalue)
            }
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

        binding.btnSignUp.setOnClickListener {

            if (roleFlag.equals("Customer")) {
                if (SignUpCustomer(
                        binding.etName,
                        binding.tvName,
                        binding.etPhone,
                        binding.tvMobile,
                        binding.etEmail,
                        binding.tvEmail,
                        binding.etPincode,
                        binding.tvPincode,
                        binding.txtCountry,
                        binding.tvCountry,
                        binding.txtState,
                        binding.tvState,
                        binding.txtCity,
                        binding.tvCity,
                        binding.etAddress,
                        binding.tvAddress,
                        binding.etPassword,
                        binding.tvPassword,
                        binding.etConfirmPassword,
                        binding.tvConfirmPassword,
                        binding.cbTerms,
                        binding.tvTerms
                    )
                ) {

                    SavedPrefManager.saveStringPreferences(
                        this@RegisterActivity,
                        SavedPrefManager.MobileNo,
                        binding.etPhone.text.toString()
                    )

                    val request = SignUpRequest()
                    request.name = binding.etName.text.toString()
                    request.mobileNumber = binding.etPhone.text.toString()
                    request.email = binding.etEmail.text.toString()
                    request.zipCode = binding.etPincode.text.toString()
                    request.country = binding.txtCountry.text.toString()
                    request.countryIsoCode = countryCode
                    request.state = binding.txtState.text.toString()
                    request.stateIsoCode = stateCode
                    request.city = binding.txtCity.text.toString()
                    request.address = binding.etAddress.text.toString()
                    request.password = binding.etConfirmPassword.text.toString()
                    request.countryCode = "+" + binding.ccp.selectedCountryCode.toString()
                    request.userType = "CUSTOMER"

                    viewModel.signInApi(request)
                }
            } else {

                if (SignUpDeliveryPartner(
                        binding.etName,
                        binding.tvName,
                        binding.etPhone,
                        binding.tvMobile,
                        binding.etEmail,
                        binding.tvEmail,
                        binding.etPincode,
                        binding.tvPincode,
                        binding.txtCountry,
                        binding.tvCountry,
                        binding.txtState,
                        binding.tvState,
                        binding.txtCity,
                        binding.tvCity,
                        binding.etAddress,
                        binding.tvAddress,
                        binding.txtUploadedFile,
                        binding.tvUploadDocument,
                        binding.etPassword,
                        binding.tvPassword,
                        binding.etConfirmPassword,
                        binding.tvConfirmPassword,
                        binding.cbTerms,
                        binding.tvTerms
                    )
                ) {

                    SavedPrefManager.saveStringPreferences(
                        this@RegisterActivity,
                        SavedPrefManager.MobileNo,
                        binding.etPhone.text.toString()
                    )

                    val request = SignUpRequest()
                    request.name = binding.etName.text.toString()
                    request.mobileNumber = binding.etPhone.text.toString()
                    request.email = binding.etEmail.text.toString()
                    request.zipCode = binding.etPincode.text.toString()
                    request.country = binding.txtCountry.text.toString()
                    request.countryIsoCode = countryCode
                    request.state = binding.txtState.text.toString()
                    request.stateIsoCode = stateCode
                    request.city = binding.txtCity.text.toString()
                    request.address = binding.etAddress.text.toString()
                    request.password = binding.etConfirmPassword.text.toString()
                    request.countryCode = binding.ccp.selectedCountryCode.toString()
                    request.fileName = binding.txtUploadedFile.text.toString()
                    request.userType = "DELIVERY_PARTNER"
                    request.govtDocument = urlLink

                    viewModel.signInApi(request)
                }
            }
        }
    }

    private fun ObserveUploadFileResponce() {

        lifecycleScope.launch {

            viewModel._UploadFileStateFlow.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {

                            binding.btnUploadDocument.hideProgress(getString(R.string.upload_documents))
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            Toast.makeText(
                                this@RegisterActivity,
                                "Upload document successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                            urlLink = response.data.result.mediaUrl
                        }
                    }


                    is Resource.Error -> {

                        binding.btnUploadDocument.hideProgress(getString(R.string.upload_documents))
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@RegisterActivity)
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


    private fun selectPDF() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission
                    .READ_EXTERNAL_STORAGE
            )
            != PackageManager
                .PERMISSION_GRANTED
        ) {
            // When permission is not granted
            // Result permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            );
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/pdf"
            startActivityForResult(intent, 100)
        }
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_CANCELED) {

        } else {
            if (requestCode == 100) {
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {

                            val sUri: Uri? = data.data
                            val sPath: String = getPDFPath(sUri)!!
                            var file = File(sUri!!.path)
                            var finalPath = file.absolutePath

                            val document = (System.currentTimeMillis() / 1000).toString()
                            try {
                                val inputStream: InputStream =
                                    getContentResolver().openInputStream(data.data!!)!!
                                val fileOutputStream: FileOutputStream = FileOutputStream(
                                    File(
                                        this.getExternalFilesDir("temp"),
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
                                getExternalFilesDir("temp"),
                                "$document.pdf"
                            )

                            binding.txtUploadedFile.text = sPath
                            var requestGalleryImageFile: RequestBody =
                                RequestBody.create("application/pdf".toMediaTypeOrNull(), imageFile)

                            documentRequest = MultipartBody.Part.createFormData(
                                "uploaded_file",
                                imageFile.name,
                                requestGalleryImageFile
                            )

                            lifecycleScope.async(Dispatchers.IO) {
                                viewModel.UploadFileApi(documentRequest)

                                withContext(Dispatchers.Main) {
                                    binding.btnUploadDocument.showProgress {
                                        progressColor = Color.WHITE
                                    }
                                    getWindow().setFlags(
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                                    )
                                }
                            }.onAwait


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
                    applicationContext,
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
                cursor = contentResolver
                    .query(uri!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))

                    val finalDisplayName = displayName
                }
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        return displayName
    }


    private fun ObserveResponce() {

        lifecycleScope.launchWhenStarted {
            viewModel._SignInStateFlow.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200) {

                            if (roleFlag.equals("Customer")) {

                                SavedPrefManager.saveStringPreferences(
                                    this@RegisterActivity,
                                    SavedPrefManager.EmailId,
                                    binding.etEmail.text.toString()
                                )

                                SavedPrefManager.saveStringPreferences(
                                    this@RegisterActivity,
                                    SavedPrefManager.flow,
                                    "RegFlow"
                                )

                                Intent(
                                    this@RegisterActivity,
                                    OTPVerificationActivity::class.java
                                ).also {
                                    it.putExtra("SignIN", "SignIN")
                                    startActivity(it)
                                    finish()
                                }

                            } else {

                                SavedPrefManager.saveStringPreferences(
                                    this@RegisterActivity,
                                    SavedPrefManager.MobileNo,
                                    binding.etPhone.text.toString()
                                )


                                SavedPrefManager.saveStringPreferences(
                                    this@RegisterActivity,
                                    SavedPrefManager.userId,
                                    response.data.result.id
                                )

                                Intent(
                                    this@RegisterActivity,
                                    BankDetailsActivity::class.java
                                ).also {
                                    startActivity(it)
                                }
                                finish()
                            }
                        }

                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@RegisterActivity)
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

    private fun ObserveCountryListResponce() {

        lifecycleScope.launch {
            viewModel._CountryListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200) {

                            data = response.data.result
                            setAdapter(data)
                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@RegisterActivity)
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

    private fun ObserveStateListResponce() {

        lifecycleScope.launch {
            viewModel._StateListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200) {
                            data = response.data.result
                            setAdapter(data)
                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@RegisterActivity)
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

    private fun ObserveCityListResponce() {

        lifecycleScope.launch {
            viewModel._CityListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        hideProgressBar()

                        if (response.data?.responseCode == 200) {
                            data = response.data.result
                            setAdapter(data)
                        }
                    }

                    is Resource.Error -> {

                        hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@RegisterActivity)
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


    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)

            dialougbackButton.setOnClickListener { dialog.dismiss() }

            val popUpSearch = binding.findViewById<EditText>(R.id.popUpSearch)

            popUpSearch.addTextChangedListener { editable ->
                editable?.let {
                    LocalFilter.filter(editable.toString(), data, adapter)
                }
            }

            if (flag.equals("Country")) {
                dialougTitle.setText("Country")

                viewModel.countryListApi()

            } else if (flag.equals("State")) {
                dialougTitle.setText("State")

                viewModel.stateListApi(countryCode,"")

            } else if (flag.equals("City")) {
                dialougTitle.setText("City")

                viewModel.cityListApi(countryCode, stateCode)
            }

            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun setAdapter(data: ArrayList<CountryStateCityListResult>) {
        adapter = this?.let { OpenPopUp(this, data, categoryvalue, this) }!!
        recyclerView.adapter = adapter
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

        binding.imgConfirmEye.setOnClickListener {
            if (passwordNotVisible == 0) {
                binding.etConfirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.imgConfirmEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                passwordNotVisible = 1

            } else if (passwordNotVisible == 1) {
                binding.etConfirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgConfirmEye.setImageDrawable(resources.getDrawable(R.drawable.ic__cross_eye))
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                passwordNotVisible = 0

            } else {
                binding.etConfirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.imgConfirmEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                passwordNotVisible = 1
            }

        }

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


    // added validations

    private val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (roleFlag.equals("Customer")) {
                SignUpCustomer(
                    binding.etName,
                    binding.tvName,
                    binding.etPhone,
                    binding.tvMobile,
                    binding.etEmail,
                    binding.tvEmail,
                    binding.etPincode,
                    binding.tvPincode,
                    binding.txtCountry,
                    binding.tvCountry,
                    binding.txtState,
                    binding.tvState,
                    binding.txtCity,
                    binding.tvCity,
                    binding.etAddress,
                    binding.tvAddress,
                    binding.etPassword,
                    binding.tvPassword,
                    binding.etConfirmPassword,
                    binding.tvConfirmPassword,
                    binding.cbTerms,
                    binding.tvTerms
                )
            } else {
                SignUpDeliveryPartner(
                    binding.etName,
                    binding.tvName,
                    binding.etPhone,
                    binding.tvMobile,
                    binding.etEmail,
                    binding.tvEmail,
                    binding.etPincode,
                    binding.tvPincode,
                    binding.txtCountry,
                    binding.tvCountry,
                    binding.txtState,
                    binding.tvState,
                    binding.txtCity,
                    binding.tvCity,
                    binding.etAddress,
                    binding.tvAddress,
                    binding.txtUploadedFile,
                    binding.tvUploadDocument,
                    binding.etPassword,
                    binding.tvPassword,
                    binding.etConfirmPassword,
                    binding.tvConfirmPassword,
                    binding.cbTerms,
                    binding.tvTerms
                )

            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }

}