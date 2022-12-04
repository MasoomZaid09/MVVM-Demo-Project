package com.kanabix.ui.fragment.Tabs


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.fram.farmserv.utils.Currency_from_Location
import com.kanabix.R
import com.kanabix.adapters.CategoryImageAdapter
import com.kanabix.adapters.ProductsAdapter
import com.kanabix.api.request.ProductListRequest
import com.kanabix.api.response.*
import com.kanabix.databinding.FragmentCustomerHomePageBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.*
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.adapter.KanabixDealsAdapter
import com.kanabix.ui.bottomSheets.LoginScreen
import com.kanabix.utils.LocationClass
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.HomeViewModel
import com.kanabix.viewModel.SharedLoginViewModel
import com.kanabix.viewModel.SharedWishListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CustomerHomePage : Fragment(), ProductClick, CategoryClick, wishListClickUpdated,
    DealProductClick, sessionExpiredListener {

    private lateinit var binding: FragmentCustomerHomePageBinding
    lateinit var adapter: CategoryImageAdapter
    lateinit var products: ProductsAdapter
    lateinit var adapter1: KanabixDealsAdapter

    var productResponse: ArrayList<ProductListDoc> = ArrayList()
    var categoryResponse: ArrayList<CategoryManagementDoc> = ArrayList()
    var sliderList: ArrayList<SlideModel> = ArrayList()
    var dealResponse: ArrayList<DealListDoc> = ArrayList()

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

    private var token = ""
    var latitute: Double = 0.0
    var logitute: Double = 0.0
    var isSearching = false
    var isAgain = false
    var Address = ""

    var searchingSomething = false

    var LikeImage: ImageView? = null
    var UnLikeImage: ImageView? = null

    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedWishListViewModel by activityViewModels()
    private val sharedLoginScreen: SharedLoginViewModel by activityViewModels()

    private val LOCATION_PERMISSION_REQ_CODE = 1000;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN)
            .toString()
        locationpermission()
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerHomePageBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.txtLocation.text = Address
        binding.txtLocation.setSelected(true)


        clicks(view)
        setToolBar()
        searchApi()
        pullToRefresh()

        return view
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

            Address = Currency_from_Location(requireContext()).getonlyAddress(latitute, logitute)

            isSearching = false
            isAgain = true
            callHomeApi("")

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
                LOCATION_PERMISSION_REQ_CODE
            )
        } else {
            LocationClass.getCurrentLocation(requireContext())
            USERCURRENTLOCATION()
        }
    }

    fun pullToRefresh() {

        binding.swipe.setOnRefreshListener {
            binding.DFsearch.text.clear()
            locationpermission()
            binding.swipe.isRefreshing = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (SavedPrefManager.getBooleanPreferences(
                            requireContext(),
                            SavedPrefManager.loggedIn
                        )
                    ) {
                        activity?.finishAffinity()
                    } else {
                        activity?.finish()
                    }
                }
            })

        ObserveLoginResponse()
        ObserveCategoryResponce()
        ObserveDealResponce()
        ObserveProductResponce()
        ObserveBannerResponce()
        ObserveAddWishListResponce()
    }

    fun ObserveLoginResponse() {

        sharedLoginScreen._sharedTokenState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { result ->
                token =
                    SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN)
                        .toString()
                locationpermission()
            })
    }

    private fun searchApi() {

        binding.DFsearch.addTextChangedListener { editable ->
            editable?.let {
                if (editable.toString().isNotEmpty()) {
                    isSearching = true

                    binding.categoryTitleLayout.visibility = View.GONE
                    binding.CategoryRecycler.visibility = View.GONE
                        binding.bannerClick.visibility = View.GONE
                    binding.dealTitleLayout.visibility = View.GONE
                        binding.imageSlider.visibility = View.GONE
                    binding.DealsRecycler.visibility = View.GONE
                    binding.productTitleLayout.visibility = View.GONE
                    binding.ProductsRecyclerview.visibility = View.GONE
                    binding.txtLocation.visibility = View.GONE
                    binding.txtNoData.visibility = View.GONE

                    searchingSomething = true
                    callHomeApi(editable.toString())
                } else {
                    isSearching = true

                    binding.categoryTitleLayout.visibility = View.VISIBLE
                    binding.CategoryRecycler.visibility = View.VISIBLE
                    binding.bannerClick.visibility = View.VISIBLE
                    binding.imageSlider.visibility = View.VISIBLE
                    binding.dealTitleLayout.visibility = View.VISIBLE
                    binding.DealsRecycler.visibility = View.VISIBLE
                    binding.productTitleLayout.visibility = View.VISIBLE
                    binding.ProductsRecyclerview.visibility = View.VISIBLE
                    binding.txtLocation.visibility = View.VISIBLE
                    binding.txtNoData.visibility = View.GONE

                    searchingSomething = false
                    callHomeApi("")
                }
            }
        }
    }

    private fun callHomeApi(search: String) {

        categoryApi(search)
        dealApi()
        bannerListApi()
        productApi(search)
    }

    private fun ObserveCategoryResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._CategoryStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            try {
                                response.data.result.apply {

                                    categoryResponse = docs
                                    binding.txtNoData.visibility = View.GONE
                                    binding.categoryTitleLayout.visibility = View.VISIBLE
                                    binding.CategoryRecycler.visibility = View.VISIBLE
                                    binding.txtLocation.visibility = View.VISIBLE
                                    setCategoryAdapter(categoryResponse)

//                                        if (categoryResponse.size > 0){
//
//                                        }
//                                        else{
//                                            binding.txtNoData.visibility = View.GONE
//                                            binding.categoryTitleLayout.visibility = View.GONE
//                                            binding.CategoryRecycler.visibility = View.GONE
//                                            binding.txtLocation.visibility = View.GONE
//                                        }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@CustomerHomePage).show(it, "MyCustomFragment")
                            }
                        }
                    }
                    is Resource.Error -> {

                        ProgressBar.hideProgress()
                        categoryResponse.clear()

                        setCategoryAdapter(categoryResponse)
                        binding.categoryTitleLayout.visibility = View.GONE
                        binding.CategoryRecycler.visibility = View.GONE

                        if (!isSearching) {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }
                    }

                    is Resource.Loading -> {

                        if (!isSearching) {
//                                ProgressBar.showProgress(requireContext())
                        }
                    }

                    is Resource.Empty -> {

                        ProgressBar.hideProgress()
                    }

                }

            }
        }

    }

    private fun ObserveDealResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._dealListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            try {
                                response.data.result.apply {

                                    if (binding.DFsearch.text.toString().equals("")) {
                                        dealResponse = docs
                                        binding.dealTitleLayout.visibility = View.VISIBLE
                                        binding.DealsRecycler.visibility = View.VISIBLE
                                        setDealsAdapter(dealResponse)
                                    } else {
                                        binding.dealTitleLayout.visibility = View.GONE
                                        binding.DealsRecycler.visibility = View.GONE
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else if (response.data?.responseCode == 201) {
                            binding.dealTitleLayout.visibility = View.GONE
                            androidExtension.alertBox(
                                response.data.responseMessage,
                                requireContext()
                            )
                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@CustomerHomePage).show(it, "MyCustomFragment")
                            }
                        }
                    }
                    is Resource.Error -> {

                        ProgressBar.hideProgress()
                        dealResponse.clear()
                        setDealsAdapter(dealResponse)
                        binding.dealTitleLayout.visibility = View.GONE
                        binding.DealsRecycler.visibility = View.GONE
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Empty -> {

                        ProgressBar.hideProgress()
                    }
                }
            }
        }

    }

    private fun ObserveBannerResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._bannerListStateFlow.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            try {
                                response.data.apply {

                                    if (!searchingSomething){

                                        if (result.size > 0) {
                                            binding.bannerClick.visibility = View.VISIBLE
                                            binding.imageSlider.visibility = View.VISIBLE

                                            sliderList.clear()
                                            for (i in result) {
                                                sliderList.add(SlideModel(i.thumbNail))
                                            }
                                            binding.imageSlider.setImageList(sliderList)

                                            if (result.size == 1) {
                                                binding.imageSlider.stopSliding()
                                            } else {
                                                binding.imageSlider.startSliding()
                                            }
                                        } else {
                                            binding.bannerClick.visibility = View.VISIBLE
                                            binding.imageSlider.visibility = View.VISIBLE
                                        }
                                    } else {
                                        binding.bannerClick.visibility = View.GONE
                                        binding.imageSlider.visibility = View.GONE
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } else if (response.data?.responseCode == 201) {
//                            binding.bannerClick.visibility = View.GONE
                            androidExtension.alertBox(
                                response.data.responseMessage,
                                requireContext()
                            )
                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@CustomerHomePage).show(it, "MyCustomFragment")
                            }
                        }
                    }
                    is Resource.Error -> {

                        ProgressBar.hideProgress()
                        sliderList.clear()
                        binding.imageSlider.setImageList(sliderList)
                        binding.bannerClick.visibility = View.GONE
                        binding.imageSlider.visibility = View.GONE
                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Empty -> {
                        ProgressBar.hideProgress()
                    }
                }
            }
        }
    }

    private fun ObserveProductResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._productListStateFlow.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            try {
                                response.data.result.apply {

                                    productResponse = docs

                                    if (productResponse.size == 0 && categoryResponse.size == 0) {
                                        binding.mainLayout.visibility = View.GONE
                                        binding.txtNoData.visibility = View.VISIBLE
                                    }
                                    if (productResponse.size > 0) {
                                        binding.mainLayout.visibility = View.VISIBLE
                                        binding.txtNoData.visibility = View.GONE
                                        binding.productTitleLayout.visibility = View.VISIBLE
                                        binding.ProductsRecyclerview.visibility = View.VISIBLE
                                        setProductsAdapter(productResponse)
                                    }
                                    if (productResponse.size < 4) {
                                        binding.productTitleLayout.visibility = View.GONE
                                        binding.ProductsRecyclerview.visibility = View.GONE
                                        binding.viewAllProducts.visibility = View.GONE
                                        setProductsAdapter(productResponse)
                                    } else if (productResponse.size > 4) {
                                        binding.productTitleLayout.visibility = View.VISIBLE
                                        binding.ProductsRecyclerview.visibility = View.VISIBLE
                                        binding.viewAllProducts.visibility = View.VISIBLE
                                        setProductsAdapter(productResponse)
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else if (response.data?.responseCode == 201) {
                            binding.productTitleLayout.visibility = View.GONE
                            androidExtension.alertBox(
                                response.data.responseMessage,
                                requireContext()
                            )
                        } else if (response.data?.responseCode == 440) {
                            fragmentManager?.let {
                                LogOutDialog(this@CustomerHomePage).show(it, "MyCustomFragment")
                            }
                        }
                    }
                    is Resource.Error -> {

                        ProgressBar.hideProgress()
                        productResponse.clear()
                        setProductsAdapter(productResponse)
                        binding.productTitleLayout.visibility = View.GONE
                        binding.ProductsRecyclerview.visibility = View.GONE
                    }

                    is Resource.Loading -> {

                        if (!isSearching) {
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

    private fun ObserveAddWishListResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel._addWishListStateFlow.collect() { response ->

                when (response) {

                    is Resource.Success -> {


                        ProgressBar.hideProgress()

                        if (response.data?.responseCode == 200) {

                            try {
                                if (response.data.result.isLike) {
                                    UnLikeImage?.visibility = View.GONE
                                    LikeImage?.visibility = View.VISIBLE
                                } else {
                                    UnLikeImage?.visibility = View.VISIBLE
                                    LikeImage?.visibility = View.GONE
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
//                                productApi("")
                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@CustomerHomePage).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

                        ProgressBar.hideProgress()

                    }

                    is Resource.Loading -> {
//                        showProgressBar()
                    }

                    is Resource.Empty -> {
                        ProgressBar.hideProgress()
                    }
                }
            }
        }
    }

    // Setup Adapters
    fun setCategoryAdapter(data: ArrayList<CategoryManagementDoc>) {
        binding.CategoryRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter = CategoryImageAdapter(requireContext(), data, this, "Home")
        binding.CategoryRecycler.adapter = adapter
    }

    fun setDealsAdapter(data: ArrayList<DealListDoc>) {
        binding.DealsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        adapter1 = KanabixDealsAdapter(requireContext(), data, this, "Home")
        binding.DealsRecycler.adapter = adapter1
    }

    fun setProductsAdapter(productResponse: ArrayList<ProductListDoc>) {
        binding.ProductsRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        products = ProductsAdapter(requireContext(), productResponse, this, this, "Home")
        binding.ProductsRecyclerview.adapter = products
    }

    fun setToolBar() {

        try {
            PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
            profile_ll = activity?.findViewById(R.id.profile_ll)!!
            iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
            Back = activity?.findViewById(R.id.imageView_back)!!

            val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
            Add_address.visibility = View.GONE
//
            PreLoginTitle_TextView2.visibility = View.GONE
            iconsLayout.visibility = View.VISIBLE
            profile_ll.visibility = View.VISIBLE
            Back.visibility = View.GONE

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

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Custom Clicks
    override fun sendProductId(productId: String) {
        val amount = productId
        val bundle = bundleOf("productId" to amount)
        findNavController().navigate(R.id.viewProduct, bundle)
    }

    override fun categoryClick(categoryId: String) {
        val amount = categoryId
        val bundle = bundleOf("categoryId" to amount)
        findNavController().navigate(R.id.productsList, bundle)
    }

    override fun DealProductClick(dealId: String) {
        val amount = dealId
        val bundle = bundleOf("dealId" to amount)
        findNavController().navigate(R.id.viewDealScreen, bundle)
    }

    override fun wishListClick(productId: String, view: ImageView, view1: ImageView) {

        if (SavedPrefManager.getBooleanPreferences(requireContext(), SavedPrefManager.loggedIn)) {
            com.kanabix.utils.ProgressBar.showProgress(requireContext())
            sharedViewModel.addWishListApi(token, productId)
            LikeImage = view1
            UnLikeImage = view
        } else {
            activity?.supportFragmentManager?.let {
                LoginScreen().show(
                    it, "Login Customer"
                )
            }
        }
    }

    // all clicks
    private fun clicks(view: View) {

        binding.SeeAllCategoriesTextView.setOnClickListener {
            val amount = binding.DFsearch.text.toString()
            val bundle = bundleOf("search" to amount)
            view.findNavController().navigate(R.id.action_customerHomePage_to_categoryList, bundle)
        }

        binding.viewAlloffers.setOnClickListener {

            view.findNavController().navigate(R.id.action_customerHomePage_to_dealsFragment)
        }

        binding.viewAllProducts.setOnClickListener {
            val amount = binding.DFsearch.text.toString()
            val bundle = bundleOf("search" to amount)
            view.findNavController().navigate(R.id.action_customerHomePage_to_productsList, bundle)
        }
    }

    // Apis for Home
    fun productApi(search: String) {

        val request = ProductListRequest()
        request.page = 1
        request.limit = 10
        request.lat = latitute
        request.lng = logitute
        request.search = search
        viewModel.ProductListApi(token, request)
    }

    fun categoryApi(search: String) {
        viewModel.CategoryManagmentApi(search, 1, 10)
    }

    fun dealApi() {
        viewModel.DealListApi(latitute, logitute, "", "", 1, 10)
    }

    fun bannerListApi() {
        viewModel.BannerListApi()
    }

    override fun sessionExpiredClick() {
        SavedPrefManager.savePreferenceBoolean(requireContext(), SavedPrefManager.loggedIn, false)
        Intent(requireContext(), RoleSelectScreen::class.java).also {
            startActivity(it)
            requireActivity().finishAffinity()
        }
    }


}