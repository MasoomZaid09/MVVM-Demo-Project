package com.kanabix.ui.fragment.deals

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.models.SlideModel
import com.exobe.Utils.CommonFunctions
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.adapters.ImageSliderAdapter
import com.kanabix.databinding.FragmentViewDealScreenBinding
import com.kanabix.adapters.DealsSuggestedAdapter
import com.kanabix.api.request.ProductListRequest
import com.kanabix.api.response.ProductListDoc
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.ProductClick
import com.kanabix.interfaces.WishListClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.LoginActivity
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.bottomSheets.LoginScreen
import com.kanabix.utils.LocationClass
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.SharedLoginViewModel
import com.kanabix.viewModel.SharedWishListViewModel
import com.kanabix.viewModel.ViewDealViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ViewDealScreen : Fragment(), ProductClick, WishListClick ,sessionExpiredListener {

    private lateinit var binding: FragmentViewDealScreenBinding
    lateinit var mContext: Context
    private lateinit var adapter: DealsSuggestedAdapter
    var sliderList: ArrayList<SlideModel> = ArrayList()

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


    var data: ArrayList<ProductListDoc> = ArrayList()

    var storeId = ""
    var navigateScreen = ""
    var dealId = ""
    var productID = ""
    var categoryId = ""
    var token = ""
    var latitute: Double = 0.0
    var logitute: Double = 0.0
    var dealAmount: Number = 0.0
    var totalPrice: Number = 0.0
    var categoryListRequest: ArrayList<String> = ArrayList()
    var isAddedInCart = false
    var isAgain = false
    var isSeaching = false
    var flag = ""

    private val viewModel: ViewDealViewModel by viewModels()
    private val sharedViewModel: SharedWishListViewModel by activityViewModels()
    private val sharedLoginScreen: SharedLoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        dealId = arguments?.getString("dealId").toString()
        flag = arguments?.getString("flag").toString()

        locationpermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewDealScreenBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        bindProgressButton(binding.btnGoTOcart)
        bindProgressButton(binding.btnBuyNow)
        binding.btnBuyNow.attachTextChangeAnimator()
        binding.btnGoTOcart.attachTextChangeAnimator()


        setToolBar()
        clicks()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (flag.equals("Notification")) {
                        findNavController().navigate(R.id.action_viewDealScreen_to_notification)
                    } else {
                        findNavController().popBackStack()
                    }
                }
            })

        ObserveLoginResponse()
        ObserveDealResponse()
        ObserveAddWishListResponce()
        ObserveAddToCartResponse()
        ObserveSuggestedProductListResponse()
    }

    private fun USERCURRENTLOCATION() {
        try {
            SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.LAT)?.let {
                latitute = it.toDouble()
            }
            SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.LONG)?.let {
                logitute = it.toDouble()
            }

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
            isSeaching = true
            callViewDealApi(token,dealId)

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

    fun ObserveLoginResponse() {

        sharedLoginScreen._sharedTokenState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { result ->
                token = result
                locationpermission()
            })
    }
    private fun ObserveAddWishListResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                sharedViewModel._addWishListStateFlow.collect() { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {
                                    callSuggestedProductsApi(categoryId)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            } else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewDealScreen).show(it, "MyCustomFragment")
                                }
                            }
                        }

                        is Resource.Error -> {

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

    private fun ObserveAddToCartResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._productAddToCartStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                if (navigateScreen.equals("BUYNOW")) {
                                    delay(1000L)
                                    binding.btnBuyNow.hideProgress("Buy Now")
                                    binding.btnBuyNow.isClickable = true
                                    navigateScreen = ""
                                    findNavController().navigate(R.id.action_viewDealScreen_to_cartFragment)


                                } else if (navigateScreen.equals("ADDTOCART")) {
                                    delay(1000L)
                                    binding.btnGoTOcart.hideProgress("Add To Cart")
                                    binding.btnGoTOcart.isClickable = true
                                    navigateScreen = ""
                                    Toast.makeText(
                                        requireContext(),
                                        "Add to cart successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    callViewDealApi(token,dealId)
                                }else{

                                }
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewDealScreen).show(it, "MyCustomFragment")
                                }
                            }
                        }

                        is Resource.Error -> {

                            binding.btnBuyNow.hideProgress("Buy Now")
                            binding.btnGoTOcart.hideProgress("Add To Cart")
                            binding.btnBuyNow.isClickable = true
                            binding.btnGoTOcart.isClickable = true

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

    private fun ObserveSuggestedProductListResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._productListStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {

                                try {
                                    data = response.data.result.docs
                                    setSuggestedProductList(data)

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewDealScreen).show(it, "MyCustomFragment")
                                }
                            }
                        }


                        is Resource.Error -> {

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

    private fun ObserveDealResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._viewDealStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                try {
                                    response.data.result.apply {

                                        totalPrice = productId.price.toDouble()
                                        dealAmount = dealPrice.toDouble()

                                        productID = productId.id
                                        binding.tvHeading.text = productId.productName
                                        binding.tvDiscription.text = productId.categoryId.categoryName
                                        binding.tvAboutProductDiscription.text = description
                                        binding.cutPrice.setText("${CommonFunctions.currencyFormatter(productId.price.toDouble())}/10 g")
                                        binding.cutPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                                        binding.tvPrice.setText("${CommonFunctions.currencyFormatter(dealPrice.toDouble())}/10 g")
                                        binding.discount.text = "${discount}% Off"

                                        // added images
                                        sliderList.clear()
                                        for (i in dealImage) {
                                            sliderList.add(SlideModel(i))
                                        }
                                        binding.imageSlider.setImageList(sliderList)

                                        if (dealImage.size == 1){
                                            binding.imageSlider.stopSliding()
                                        }else{
                                            binding.imageSlider.startSliding()
                                        }



                                        // take store id to open store view
                                        storeId = userId.Id.toString()

                                        // store data
                                        userId.store?.apply {
                                            binding.storeName.text = storeName
                                            binding.storeAddress.text = storeAddress
                                        }
                                        binding.storePinCode.text = "Pincode - ${userId.zipCode}"
                                        binding.storeNumber.text = "${userId.countryCode}-${userId.mobileNumber}"

                                        binding.noOfLikes.text = no_of_likes.toString()
                                        binding.noOfProducts.text = no_of_products.toString()

                                        // getting isAddToCartKey
                                        isAddedInCart = isAddedToCart

                                        categoryId = productId.categoryId.id
                                        callSuggestedProductsApi(productId.categoryId.id)
//                                        ObserveSuggestedProductListResponse()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewDealScreen).show(it, "MyCustomFragment")
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
                            if (!isSeaching){
                                ProgressBar.showProgress(requireContext())
                            }
                        }

                        is Resource.Empty -> {
                            ProgressBar.hideProgress()
                        }
                    }

                }
            }
    }

    private fun callViewDealApi(token : String,dealId :String) {
        lifecycleScope.async(Dispatchers.IO){
            viewModel.viewDealApi(token,dealId)
        }.onAwait
    }

    private fun callSuggestedProductsApi(categoryId: String) {

        val request = ProductListRequest()
        request.page = 1
        request.limit = 10
        request.lat = latitute
        request.lng = logitute
        categoryListRequest.add(categoryId)
        request.categoryId = categoryListRequest
        viewModel.productList(token, request)
    }

    fun setSuggestedProductList(data: ArrayList<ProductListDoc>) {
        adapter = DealsSuggestedAdapter(requireContext(), data, this, this)
        binding.recyclerviewDeals.adapter = adapter
    }

    override fun sendProductId(productId: String) {
        val amount = productId
        val bundle = bundleOf("productId" to amount)
        findNavController().navigate(R.id.viewProduct, bundle)
    }

    override fun wishListClick(productId: String) {

        if (!SavedPrefManager.getBooleanPreferences(requireContext(),SavedPrefManager.loggedIn)){
            activity?.supportFragmentManager?.let {
                LoginScreen().show(
                    it, "Login Customer"
                )
            }
        }else{
            sharedViewModel.addWishListApi(token, productId)
        }
    }

    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.visibility = View.GONE
        iconsLayout.visibility = View.VISIBLE
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

    private fun clicks() {
        Back.setOnClickListener {

            if (flag.equals("Notification")) {
                findNavController().navigate(R.id.action_viewDealScreen_to_notification)
            } else {
                findNavController().popBackStack()
            }
        }


        binding.btnBuyNow.setOnClickListener {

            if (!SavedPrefManager.getBooleanPreferences(requireContext(),SavedPrefManager.loggedIn)){

                activity?.supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }

            }else {

                if (isAddedInCart) {

                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.btnBuyNow.showProgress {
                            progressColor = Color.WHITE
                        }
                        binding.btnBuyNow.isClickable = false
                        delay(2000L)
                        binding.btnBuyNow.hideProgress("Buy Now")
                        binding.btnBuyNow.isClickable = true
                        findNavController().navigate(R.id.action_viewDealScreen_to_cartFragment)
                    }
                } else {
                    navigateScreen = "BUYNOW"
                    val jsonObject = JsonObject().apply {
                        addProperty("productId", productID)
                        addProperty("dealId", dealId)
                        addProperty("dealAmount", dealAmount)
                        addProperty("totalPrice", totalPrice)
                        addProperty("quantity", 1)
                        addProperty("addType", "DEAL")
                    }

                    lifecycleScope.async(Dispatchers.IO) {

                        viewModel.dealAddToCartApi(token, jsonObject)
                        withContext(Dispatchers.Main) {
                            binding.btnBuyNow.showProgress {
                                progressColor = Color.WHITE
                            }
                            binding.btnBuyNow.isClickable = false
                        }
                    }.onAwait

                }
            }
        }

        binding.btnGoTOcart.setOnClickListener {

            if (!SavedPrefManager.getBooleanPreferences(requireContext(),SavedPrefManager.loggedIn)){

                activity?.supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }

            }else{
                navigateScreen = "ADDTOCART"

                val jsonObject = JsonObject().apply {
                    addProperty("productId", productID)
                    addProperty("dealId", dealId)
                    addProperty("dealAmount", dealAmount)
                    addProperty("totalPrice", totalPrice)
                    addProperty("quantity", 1)
                    addProperty("addType", "DEAL")
                }

                lifecycleScope.async(Dispatchers.IO) {

                    viewModel.dealAddToCartApi(token, jsonObject)
                    withContext(Dispatchers.Main){
                        binding.btnGoTOcart.showProgress {
                            progressColor = Color.WHITE
                        }
                        binding.btnGoTOcart.isClickable = false
                    }
                }.onAwait
            }
        }

        binding.tvViewmore.setOnClickListener {
            val amount = storeId
            val bundle = bundleOf("storeId" to amount)
            findNavController().navigate(R.id.storeViewProductFragment, bundle)
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