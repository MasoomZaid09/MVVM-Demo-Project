package com.kanabix.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exobe.Utils.CommonFunctions
import com.fram.farmserv.utils.Currency_from_Location
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.adapters.OpenPopUp
import com.kanabix.api.response.CountryStateCityListResult
import com.kanabix.databinding.FragmentAddAddressBinding
import com.kanabix.databinding.FragmentCustomerHomePageBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.DialogUtils
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.popupItemClickListner
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.models.Popup
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.*
import com.kanabix.utils.ProgressBar
import com.kanabix.validations.FormValidation
import com.kanabix.viewModel.DeliveryAddAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

@AndroidEntryPoint
class AddAddressFragment : Fragment(), popupItemClickListner ,sessionExpiredListener {
    private lateinit var binding: FragmentAddAddressBinding
    private lateinit var adapter: OpenPopUp

    // toolbar
    lateinit var iconsLayout: LinearLayout
    lateinit var profile_ll: LinearLayout
    lateinit var Back: LinearLayout
    lateinit var PreLoginTitle_TextView2: TextView

    lateinit var greyBell_ImageView: ImageView
    lateinit var greenBell_ImageView: ImageView
    lateinit var greyCart: ImageView
    lateinit var greenCart: ImageView
    lateinit var greenHeart: ImageView
    lateinit var greyHeart: ImageView

    var Address = ""

    var AddressList : kotlin.collections.ArrayList<String> = ArrayList()

    var CountryCode = ""
    var stateCode = ""
    var stateName = ""

    var data: ArrayList<CountryStateCityListResult> = ArrayList()

    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    var categoryvalue = ""


    var flag = ""
    var addressId = ""

    var latitute = 0.0
    var logitute = 0.0


    // curent location
    var add = ""
    var countryName = ""
    var adminArea = ""
    var postalCode = ""
    var subAdminArea = ""
    var locality = ""

    var statefindFlag = ""

    private val viewModel: DeliveryAddAddressViewModel by viewModels()

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        val token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()

        binding.nameEt.addTextChangedListener(textWatcher)
        binding.MobileEt.addTextChangedListener(textWatcher)
        binding.AddressEt.addTextChangedListener(textWatcher)
        binding.pincodeEt.addTextChangedListener(textWatcher)
        binding.CountryEt.addTextChangedListener(textWatcher)
        binding.StateEt.addTextChangedListener(textWatcher)
        binding.CityEt.addTextChangedListener(textWatcher)
        binding.LandmarkEt.addTextChangedListener(textWatcher)



        arguments?.getString("flag")?.let {
            flag = it
        }
        arguments?.getString("_id")?.let {
            addressId = it
        }


        if (flag.equals("Edit")) {

            viewModel.viewAddressApi(token, addressId)
        }

        setToolBar()
        Controler()

        Back.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.updateButton.setOnClickListener {
            FormValidation.add_Address(
                binding.nameEt,
                binding.nameTv,
                binding.MobileEt,
                binding.MobileTv,
                binding.AddressEt,
                binding.AddressTv,
                binding.pincodeEt,
                binding.pincodeTv,
                binding.CountryEt,
                binding.CountryTv,
                binding.StateEt,
                binding.StateTv,
                binding.CityEt,
                binding.CityTv,
                binding.LandmarkEt,
                binding.landmarkTv

            )
            if (!binding.nameEt.text.isEmpty() && !binding.MobileEt.text.isEmpty() && !binding.AddressEt.text.isEmpty() && !binding.pincodeEt.text.isEmpty() && !binding.CountryEt.text.isEmpty() && !binding.StateEt.text.isEmpty() && !binding.CityEt.text.isEmpty() && !binding.LandmarkEt.text.isEmpty()) {
                var jsonObject = JsonObject().apply {

                    if (flag.equals("Edit")) {
                        addProperty("addressId", addressId)
                    }
                    addProperty("address", binding.AddressEt.text.toString())
                    addProperty("zipCode", binding.pincodeEt.text.toString())
                    addProperty("state", binding.StateEt.text.toString())
                    addProperty("countryIsoCode", CountryCode)
                    addProperty("stateIsoCode", stateCode)
                    addProperty("city", binding.CityEt.text.toString())
                    addProperty("country", binding.CountryEt.text.toString())
                    addProperty("name", binding.nameEt.text.toString())
                    addProperty("mobileNumber", binding.MobileEt.text.toString())
                    addProperty("countryCode", "+${binding.ccp.selectedCountryCode}")
                    addProperty("landmark", binding.LandmarkEt.text.toString())
                }
                if (flag.equals("Edit")) {
                    viewModel.editAddressApi(token, jsonObject)
                } else {
                    viewModel.addAddressApi(token, jsonObject)
                }
            }
        }


        binding.CountryEt.setOnClickListener {
            categoryvalue = "Country"
            openPopUp(categoryvalue)
        }

        binding.StateEt.setOnClickListener {
            if (binding.CountryEt.text.isEmpty()) {

                binding.CountryTv.visibility = View.VISIBLE
                binding.CountryTv.text = "*Please select your country."
            } else {
                binding.CountryTv.visibility = View.GONE
                binding.CountryTv.text = ""

                categoryvalue = "State"
                openPopUp(categoryvalue)
            }
        }

        binding.CityEt.setOnClickListener {

            if (binding.CountryEt.text.isEmpty()) {

                binding.CountryTv.visibility = View.VISIBLE
                binding.CountryTv.text = "*Please select your country."

            } else if (binding.StateEt.text.isEmpty()) {
                binding.CountryTv.visibility = View.GONE
                binding.CountryTv.text = ""

                binding.StateTv.visibility = View.VISIBLE
                binding.StateTv.text = "*Please select your state."

            } else {
                binding.CountryTv.visibility = View.GONE
                binding.StateTv.visibility = View.GONE
                binding.CountryTv.text = ""
                binding.StateTv.text = ""

                categoryvalue = "City"
                openPopUp(categoryvalue)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveAddAdressResponse()
        ObserveCountryListResponce()
        ObserveStateListResponce()
        ObserveCityListResponce()
        ObserveViewAddressResponce()
        ObserveEditAdressResponse()
    }

    private fun ObserveAddAdressResponse() {

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._addAddressStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                findNavController().popBackStack()

                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@AddAddressFragment).show(it, "MyCustomFragment")
                                }
                            }
                            else if (response.data?.responseCode == 201) {

                            }
                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 404) {

                            } else {
                                response.message?.let { message ->
                                    androidExtension.alertBox(message, requireContext())
                                }
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

    private fun ObserveEditAdressResponse() {

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._editAddressStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {
//                                Toast.makeText(
//                                    requireContext(),
//                                    response.data?.responseMessage,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                                findNavController().popBackStack()

                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@AddAddressFragment).show(it, "MyCustomFragment")
                                }
                            } else if (response.data?.responseCode == 201) {

                            }
                        }
                        is Resource.Error -> {
                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 404) {

                            } else {
                                response.message?.let { message ->
                                    androidExtension.alertBox(message, requireContext())
                                }
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

    private fun ObserveCountryListResponce() {

        lifecycleScope.launchWhenCreated {
            viewModel._CountryListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {

                            data = response.data.result
                            setAdapter(data)
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@AddAddressFragment).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

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

    private fun ObserveStateListResponce() {

        lifecycleScope.launchWhenCreated {
            viewModel._StateListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {


                        if (response.data?.responseCode == 200) {

                            if (statefindFlag.equals("findState")){
                                for (i in 0 until response.data.result.size){
                                    if (response.data.result.get(i).name.equals(stateName)){
                                        stateCode = response.data.result.get(i).isoCode
                                    }
                                }
                                statefindFlag = ""
                            }else{
                                data = response.data.result
                                setAdapter(data)
                            }
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@AddAddressFragment).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

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

    private fun ObserveCityListResponce() {

        lifecycleScope.launchWhenCreated {
            viewModel._CityListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {
                            data = response.data.result
                            setAdapter(data)
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@AddAddressFragment).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {


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

    private fun ObserveViewAddressResponce() {

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._viewAddressStateFlow.collectLatest { response ->

                    delay(200L)

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                try {
                                    response.data.result.apply {

                                        binding.nameEt.setText(name)
                                        binding.MobileEt.setText(mobileNumber)

                                        binding.AddressEt.text =
                                            Editable.Factory.getInstance().newEditable(address)
                                        binding.pincodeEt.text =
                                            Editable.Factory.getInstance().newEditable(zipCode)
                                        CountryCode = countryIsoCode
                                        stateCode = stateIsoCode

                                        binding.StateEt.text = state
                                        binding.CityEt.text = city
                                        binding.CountryEt.text = country

                                        binding.LandmarkEt.setText(landmark)

                                        if (!CountryCode.equals("")) {
                                            binding.ccp.setCountryForPhoneCode(
                                                Integer.parseInt(
                                                    CountryCode
                                                )
                                            )
                                        }

                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@AddAddressFragment).show(it, "MyCustomFragment")
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

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!
        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        if (flag.equals("Edit")) {
            PreLoginTitle_TextView2.setText("Edit Address")
        } else {
            PreLoginTitle_TextView2.setText("Add Address")
        }

        PreLoginTitle_TextView2.visibility = View.VISIBLE
        iconsLayout.visibility = View.GONE
        profile_ll.visibility = View.GONE
        Back.visibility = View.VISIBLE

        greenCart = activity?.findViewById(R.id.greenCart)!!
        greyCart = activity?.findViewById(R.id.greyCart)!!
        greenBell_ImageView = activity?.findViewById(R.id.greenBell_ImageView)!!
        greyBell_ImageView = activity?.findViewById(R.id.greyBell_ImageView)!!
        greenHeart = activity?.findViewById(R.id.greenHeart)!!
        greyHeart = activity?.findViewById(R.id.greyHeart)!!


        greenCart.visibility = View.GONE
        greyCart.visibility = View.VISIBLE
        greenBell_ImageView.visibility = View.GONE
        greyBell_ImageView.visibility = View.VISIBLE
        greyHeart.visibility = View.VISIBLE
        greenHeart.visibility = View.GONE
    }

    override fun getData(data: String, flag: String, isoCode: String) {
        if (flag.equals("Country")) {
            CountryCode = isoCode
            binding.CountryEt.setText(data)
            dialog.dismiss()
        } else if (flag.equals("State")) {
            stateCode = isoCode
            binding.StateEt.setText(data)
            dialog.dismiss()
        } else if (flag.equals("City")) {
            binding.CityEt.setText(data)
            dialog.dismiss()
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

                viewModel.countryListApi()

            } else if (flag.equals("State")) {
                dialougTitle.setText("State")

                viewModel.stateListApi(CountryCode,"")

            } else if (flag.equals("City")) {
                dialougTitle.setText("City")

                viewModel.cityListApi(CountryCode, stateCode)
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

    private fun Controler() {
        binding.GetCurrentLocation.setOnClickListener {

            locationpermission()

            val full_address = Address.split(",")
            AddressList = Currency_from_Location(requireContext()).getLocality(latitute, logitute)

            binding.AddressEt.text = Editable.Factory.getInstance().newEditable(
                "${full_address.get(1)} ${full_address.get(2)} ${
                    full_address.get(3)
                } ${full_address.get(4)}".trim()
            )

//            var pincodeBuffer = StringBuffer()
//            var stateeBuffer = StringBuffer()
//            for (i in 0 until pinCode.length){
//                if (pinCode[i].isDigit()){
//                    pincodeBuffer.append(pinCode[i])
//                }else {
//                    stateeBuffer.append(pinCode[i])
//                }
//            }

            CountryCode = binding.ccp.defaultCountryNameCode.toString()
            binding.StateEt.setText(AddressList[2])
            binding.pincodeEt.setText(AddressList[3])
            binding.CountryEt.setText(AddressList[0])
            binding.CityEt.setText(AddressList[5])
            stateName = binding.StateEt.text.toString()
            statefindFlag = "findState"
            viewModel.stateListApi(CountryCode,stateName)
        }
    }

    private fun USERCURRENTLOCATION() {
        try {
            SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.LAT)?.let {
                latitute = it.toDouble()
            }
            SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.LONG)?.let {
                logitute = it.toDouble()
            }

            var fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            Address = Currency_from_Location(requireContext()).getonlyAddress(latitute, logitute)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun locationpermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        } else {
            LocationClass.getCurrentLocation(requireContext())
            USERCURRENTLOCATION()
        }
    }


    // added text watcher

    private val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            FormValidation.add_Address(
                binding.nameEt,
                binding.nameTv,
                binding.MobileEt,
                binding.MobileTv,
                binding.AddressEt,
                binding.AddressTv,
                binding.pincodeEt,
                binding.pincodeTv,
                binding.CountryEt,
                binding.CountryTv,
                binding.StateEt,
                binding.StateTv,
                binding.CityEt,
                binding.CityTv,
                binding.LandmarkEt,
                binding.landmarkTv
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




