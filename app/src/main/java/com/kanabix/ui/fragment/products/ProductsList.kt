package com.kanabix.ui.fragment.products

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.kanabix.R
import com.kanabix.adapters.FilterAdapter
import com.kanabix.adapters.ProductsAdapter
import com.kanabix.api.request.ProductListRequest
import com.kanabix.api.response.CategoryManagementDoc
import com.kanabix.api.response.ProductListDoc
import com.kanabix.databinding.FragmentProductsListBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.DialogUtils
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.*
import com.kanabix.ui.acitivity.LoginActivity
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.bottomSheets.LoginScreen
import com.kanabix.utils.*
import com.kanabix.utils.ProgressBar
import com.kanabix.viewModel.ProductListViewModel
import com.kanabix.viewModel.SharedLoginViewModel
import com.kanabix.viewModel.SharedWishListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ceil


@AndroidEntryPoint
class ProductsList : Fragment(), ProductClick, popupItemClickListner, wishListClickUpdated,sessionExpiredListener {

    private lateinit var binding: FragmentProductsListBinding
    private lateinit var products: ProductsAdapter
    lateinit var layoutManagmer : GridLayoutManager
    lateinit var mContext: Context

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

    var categoryvalue = ""
    var categoryId = ""
    var token = ""
    var latitute: Double = 0.0
    var logitute: Double = 0.0

    var isSearching = false

    var data: ArrayList<CategoryManagementDoc> = ArrayList()
    var productResponse: ArrayList<ProductListDoc> = ArrayList()
    var AuxilaryProductResponse: ArrayList<ProductListDoc> = ArrayList()
    var categoryListRequest: ArrayList<String> = ArrayList()

    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: FilterAdapter

    private val viewModel: ProductListViewModel by viewModels()
    private val sharedViewModel: SharedWishListViewModel by activityViewModels()
    private val sharedLoginScreen: SharedLoginViewModel by activityViewModels()

    // paginations

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    var search = ""
    var isAgain = false

    var searchingFlag = ""

    var LikeImage :ImageView? = null
    var UnLikeImage :ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString("categoryId")?.let {
            categoryId = it
        }

        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.TOKEN).toString()
        locationpermission()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsListBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        mContext = activity?.applicationContext!!

        arguments?.getString("search")?.let {
            search = it
        }

        binding.DFsearch.setText(search)
        setToolBar()
        searchApi()

        Back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgFilter.setOnClickListener {
            categoryvalue = "Filter"
            openPopUp(categoryvalue)
        }

//        binding.nestedScrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
//            override fun onScrollChange(
//                v: NestedScrollView,
//                scrollX: Int,
//                scrollY: Int,
//                oldScrollX: Int,
//                oldScrollY: Int
//            ) {
//                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//
//                    dataLoadFlag = true
//                    page++
//                    binding.progressBarPagination.visibility = View.VISIBLE
//                    if (page > pages) {
//                        binding.progressBarPagination.visibility = View.GONE
//                    } else {
//                        callApi(search, categoryId,page,limit)
//                    }
//                }
//            }
//        })

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
            isSearching = false
            callApi(search, categoryId,page)

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
                isAgain = true
                locationpermission()
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveLoginResponse()
        ObserveResponce()
        ObserveAddWishListResponce()
        ObserveCategoryResponce()
    }

    // Api Calls
    private fun searchApi() {

        var job: Job? = null
        binding.DFsearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        isSearching = true
//                        searchingFlag = "searching"
                        search = editable.toString()
                        callApi(search, categoryId,page)
                    } else {
                        isSearching = true
//                        searchingFlag = "notSearching"
                        search = ""
                        page = 1
                        callApi(search, categoryId,page)
                    }
                }
            }
        }
    }

    private fun callApi(search: String, categoryId: String, page: Int = 1,limit : Int = 10) {

        val request = ProductListRequest()
        request.page = page
        request.limit = limit
        request.lat = latitute
        request.lng = logitute
        request.search = search

        if (!categoryId.equals("")){
            categoryListRequest.clear()
            categoryListRequest.add(categoryId)
            request.categoryId = categoryListRequest
        }else{
            categoryListRequest.clear()
        }
        viewModel.productList(token, request)
    }


    // Observers
    private fun ObserveResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._productListStateFlow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

//                                if(searchingFlag.equals("searching")){
//                                    AuxilaryProductResponse.clear()
//                                }

                                if (response.data.result.docs.size > 0){

                                    loaderFlag = false
                                    productResponse = response.data.result.docs

                                    pages = response.data.result.remainingItems % limit
                                    setProductsAdapter(productResponse)
                                    binding.txtNoData.visibility = View.GONE
                                }

                                else{
                                    productResponse.clear()
                                    AuxilaryProductResponse.clear()
                                    setProductsAdapter(productResponse)
                                    binding.txtNoData.visibility = View.VISIBLE
                                }

                            } else if(response.data?.responseCode == 440){
                                fragmentManager?.let {
                                    LogOutDialog(this@ProductsList).show(it, "MyCustomFragment")
                                }
                            }

                            else if(response.data?.responseCode == 201){
                                productResponse.clear()
                                setProductsAdapter(productResponse)
                                binding.txtNoData.text = response.data.responseMessage
                                binding.txtNoData.visibility = View.VISIBLE
                                binding.progressBarPagination.visibility = View.GONE
                            }
                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            productResponse.clear()
                            setProductsAdapter(productResponse)

                            if (loaderFlag){
                                binding.txtNoData.visibility = View.VISIBLE
                            }
                            binding.progressBarPagination.visibility = View.GONE
                        }

                        is Resource.Loading -> {

                            if(!isSearching){
                                if (!isAgain) {
                                    if (loaderFlag){
                                        ProgressBar.showProgress(requireContext())
                                    }
                                }
                            }
                        }

                        is Resource.Empty -> {
                            ProgressBar.hideProgress()
                        }

                    }

                }
            }
    }

    private fun ObserveCategoryResponce() {

        lifecycleScope.launch {
            viewModel._CartStateFlow.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {


                        if (response.data?.responseCode == 200) {

                            data = response.data.result.docs
                            setAdapter(data)
                        }
                        else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@ProductsList).show(it, "MyCustomFragment")
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

    private fun ObserveAddWishListResponce() {

        lifecycleScope.launchWhenStarted {
            sharedViewModel._addWishListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {


                        ProgressBar.hideProgress()

                        if (response.data?.responseCode == 200) {

                            try{
                                if (response.data.result.isLike){
                                    UnLikeImage?.visibility = View.GONE
                                    LikeImage?.visibility = View.VISIBLE
                                }else{
                                    UnLikeImage?.visibility = View.VISIBLE
                                    LikeImage?.visibility = View.GONE
                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }

                        }
                        else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@ProductsList).show(it, "MyCustomFragment")
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
//                        showProgressBar()
                    }

                    is Resource.Empty -> {
                        ProgressBar.hideProgress()
                    }
                }
            }
        }
    }


    // interfaces clicks
    override fun sendProductId(productId: String) {
        val amount = productId
        val bundle = bundleOf("productId" to amount)
        findNavController().navigate(R.id.viewProduct, bundle)
    }

    override fun getData(data: String, flag: String, isoCode: String) {
        if (flag.equals("Filter")) {
            categoryId = data
            callApi(search, categoryId,page)
            dialog.dismiss()
        }
    }

    override fun wishListClick(productId: String, view:ImageView,view1:ImageView) {

        if (!SavedPrefManager.getBooleanPreferences(requireContext(),SavedPrefManager.loggedIn)){

            activity?.supportFragmentManager?.let {
                LoginScreen().show(
                    it, "Login Customer"
                )
            }
        }else {
            sharedViewModel.addWishListApi(token, productId)
//            ObserveAddWishListResponce()
            LikeImage = view1
            UnLikeImage = view
        }
    }

    // Setup Adapter
    fun setProductsAdapter(data: ArrayList<ProductListDoc>) {
        layoutManagmer = GridLayoutManager(requireContext(), 2)
        binding.productsRecyclerView.layoutManager = layoutManagmer

//        AuxilaryProductResponse.addAll(data)
        products = ProductsAdapter(mContext, data, this, this, "productList")
        binding.productsRecyclerView.adapter = products

    }




    fun setAdapter(data: ArrayList<CategoryManagementDoc>) {
        adapter = this?.let { FilterAdapter(requireContext(), data, categoryvalue, this) }!!
        recyclerView.adapter = adapter
    }

    // extra materials

    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.setText("Products")
        PreLoginTitle_TextView2.visibility = View.VISIBLE
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

    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(
                requireContext(), binding.rootView, 0
            )!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            val popUpSearch = binding.findViewById<EditText>(R.id.popUpSearch)

            popUpSearch.addTextChangedListener { editable ->
                editable?.let {
                    LocalFilter.filterCategory(editable.toString(), data, adapter)
                }
            }

            dialougbackButton.setOnClickListener {
                dialog.dismiss()
            }

            if (flag.equals("Filter")) {
                dialougTitle.setText("Filter")
                data.clear()
                viewModel.CartManagmentApi("", 1, 10)
            }

            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
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