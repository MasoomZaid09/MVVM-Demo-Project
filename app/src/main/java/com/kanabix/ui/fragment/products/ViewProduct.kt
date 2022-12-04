package com.kanabix.ui.fragment.products

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.adapters.ImageSliderAdapter
import com.kanabix.databinding.FragmentViewProductBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.LoginActivity
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.bottomSheets.LoginScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.SharedLoginViewModel
import com.kanabix.viewModel.SharedWishListViewModel
import com.kanabix.viewModel.ViewProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ViewProduct : Fragment() ,sessionExpiredListener {

    private var _binding: FragmentViewProductBinding? = null
    private val binding get() = _binding!!


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

    var storeId = ""
    var storeClick = ""
    private var token = ""
    private var productId = ""
    private var totalPrice: Number = 0.0
    var navigateScreen = ""
    var isAddedInCart = false
    var isAgain = false

    private val sharedViewModel: SharedWishListViewModel by activityViewModels()
    private val viewModel: ViewProductViewModel by viewModels()
    private val sharedLoginScreen: SharedLoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        productId = arguments?.getString("productId").toString()
        viewModel.viewProductApi(token, productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewProductBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        bindProgressButton(binding.btnGoTOcart)
        bindProgressButton(binding.btnBuyNow)
        binding.btnBuyNow.attachTextChangeAnimator()
        binding.btnGoTOcart.attachTextChangeAnimator()


        storeClick = arguments?.getString("storeClick").toString()


        if (storeClick.equals("storeClick")){
            binding.llAddress.visibility = View.GONE
        }else{
            binding.llAddress.visibility = View.VISIBLE
        }

        setToolBar()
        clicksListeners()

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveLoginResponse()
        ObserveProductResponse()
        ObserveWishListResponse()
        ObserveAddToCartResponse()
    }

    fun ObserveLoginResponse() {

        sharedLoginScreen._sharedTokenState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { result ->
                token = result
                isAgain = true
                viewModel.viewProductApi(token, productId)
            })
    }


    private fun ObserveWishListResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel._addWishListStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {

                                if (response.data.result.isLike) {

                                    binding.AddToWishListFill.visibility = View.VISIBLE
                                    binding.AddToWishListImageView.visibility = View.GONE
                                } else {
                                    binding.AddToWishListFill.visibility = View.GONE
                                    binding.AddToWishListImageView.visibility = View.VISIBLE
                                }
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewProduct).show(it, "MyCustomFragment")
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
    }

    private fun ObserveAddToCartResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._productAddToCartStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                if (navigateScreen.equals("BUYNOW")) {
                                    delay(1000L)
                                    binding.btnBuyNow.hideProgress("Buy Now")
                                    binding.btnBuyNow.isClickable = true
                                    navigateScreen = ""
                                    findNavController().navigate(R.id.action_viewProduct_to_cartFragment)

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

                                    viewModel.viewProductApi(token, productId)

                                } else {

                                }
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewProduct).show(it, "MyCustomFragment")
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

                        }

                        is Resource.Empty -> {

                        }

                    }

                }
            }
        }
    }

    private fun ObserveProductResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._viewProductStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {

                                try {
                                    response.data.result.apply {

                                        if (quantity.equals("0")){
                                            binding.txtCurrently.visibility = View.VISIBLE
                                            binding.tvPrice.visibility = View.GONE
                                            binding.btnBuyNow.visibility = View.GONE
                                            binding.btnGoTOcart.visibility = View.GONE
                                        }else{
                                            binding.txtCurrently.visibility = View.GONE
                                            binding.tvPrice.visibility = View.VISIBLE
                                            binding.btnBuyNow.visibility = View.VISIBLE
                                            binding.btnGoTOcart.visibility = View.VISIBLE
                                        }

                                        totalPrice = price.toDouble()
                                        binding.tvHeading.text = productName
                                        binding.tvDiscription.text = categoryId.categoryName
                                        binding.tvAboutProductDiscription.text = description
                                        binding.tvPrice.text =
                                            "${CommonFunctions.currencyFormatter(price.toDouble())}/10 g"

                                        // take store id to open store view
                                        storeId = userId.id.toString()

                                        // store data
                                        userId.store?.apply {
                                            binding.storeName.text = storeName
                                            binding.storeAddress.text = storeAddress
                                        }
                                        binding.storePinCode.text = "Pincode - ${userId.zipCode}"
                                        binding.storeNumber.text =
                                            "${userId.countryCode}-${userId.mobileNumber}"

                                        binding.noOfLikes.text = noOfLikes.toString()
                                        binding.noOfProducts.text = noOfProducts.toString()

                                        if (isLike) {
                                            binding.AddToWishListFill.visibility = View.VISIBLE
                                            binding.AddToWishListImageView.visibility = View.GONE
                                        } else {
                                            binding.AddToWishListFill.visibility = View.GONE
                                            binding.AddToWishListImageView.visibility = View.VISIBLE
                                        }

                                        //get add to cart key
                                        isAddedInCart = isAddedToCart

                                        // added images
                                        sliderList.clear()
                                        for (i in productImage) {
                                            sliderList.add(SlideModel(i))
                                        }
                                        binding.imageSlider.setImageList(sliderList)

                                        if (productImage.size == 1){
                                            binding.imageSlider.stopSliding()
                                        }else{
                                            binding.imageSlider.startSliding()
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@ViewProduct).show(it, "MyCustomFragment")
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
                            if (!isAgain){
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

    private fun clicksListeners() {

        Back.setOnClickListener {
            findNavController().popBackStack()
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
                        findNavController().navigate(R.id.action_viewProduct_to_cartFragment)
                    }

                } else {
                    navigateScreen = "BUYNOW"
                    val jsonObject = JsonObject().apply {
                        addProperty("productId", productId)
                        addProperty("quantity", 1)
                        addProperty("totalPrice", totalPrice)
                        addProperty("addType", "PRODUCT")
                    }

                    lifecycleScope.async(Dispatchers.IO) {

                        viewModel.productAddToCartApi(token, jsonObject)
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

            }else {

                navigateScreen = "ADDTOCART"
                val jsonObject = JsonObject().apply {
                    addProperty("productId", productId)
                    addProperty("quantity", 1)
                    addProperty("totalPrice", totalPrice)
                    addProperty("addType", "PRODUCT")
                }
                lifecycleScope.async(Dispatchers.IO) {

                    viewModel.productAddToCartApi(token, jsonObject)
                    withContext(Dispatchers.Main) {
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
            findNavController().navigate(
                R.id.action_viewProduct_to_storeViewProductFragment,
                bundle
            )
        }

        binding.AddToWishListImageView.setOnClickListener {

            if(!SavedPrefManager.getBooleanPreferences(requireContext(),SavedPrefManager.loggedIn)){

                activity?.supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }
            }else{
                sharedViewModel.addWishListApi(token, productId)
                ObserveWishListResponse()
            }
        }

        binding.AddToWishListFill.setOnClickListener {
            sharedViewModel.addWishListApi(token, productId)
            ObserveWishListResponse()
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