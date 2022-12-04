package com.kanabix.ui.fragment.store

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.adapters.ProductsAdapter
import com.kanabix.databinding.FragmentStoreViewProductBinding
import com.kanabix.adapters.StoreNearYouAdapter
import com.kanabix.api.Constants
import com.kanabix.api.response.ProductListDoc
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.ProductClick
import com.kanabix.interfaces.WishListClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.interfaces.wishListClickUpdated
import com.kanabix.ui.acitivity.LoginActivity
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.bottomSheets.LoginScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.SharedLoginViewModel
import com.kanabix.viewModel.SharedWishListViewModel
import com.kanabix.viewModel.StoreProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class StoreViewProductFragment : Fragment(), ProductClick, wishListClickUpdated, sessionExpiredListener {
    private lateinit var binding: FragmentStoreViewProductBinding
    lateinit var products: StoreNearYouAdapter
    lateinit var layoutManager: GridLayoutManager

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

    var token = ""
    var storeId = ""
    var flag = ""
    var storeProductData: ArrayList<ProductListDoc> = ArrayList()
    var auxilaryProductData: ArrayList<ProductListDoc> = ArrayList()

    private val viewModel: StoreProductViewModel by viewModels()
    private val sharedViewModel: SharedWishListViewModel by activityViewModels()
    private val sharedLoginScreen: SharedLoginViewModel by activityViewModels()

    // paginations

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    var search = ""

    var isSearching = false

    var LikeImage :ImageView? = null
    var UnLikeImage :ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN)
            .toString()
        arguments?.getString("storeId").let { storeId = it.toString() }
        arguments?.getString("flag").let { flag = it.toString() }

        com.kanabix.utils.ProgressBar.showProgress(requireContext())
        callApi()
        callStoreProductApi(search)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreViewProductBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()

        if (flag.equals("ViewStore")) {
            Back.setOnClickListener { findNavController().navigate(R.id.action_storeViewProductFragment_to_fragmentFindStoreNearYou) }
        } else {
            Back.setOnClickListener { findNavController().popBackStack() }
        }
        searchApi()

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
//                        callStoreProductApi(search,page,limit)
//                    }
//                }
//            }
//        })

        return view
    }

    fun ObserveLoginResponse() {

        sharedLoginScreen._sharedTokenState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { result ->
                token = result
                callApi()
                callStoreProductApi(search)
            })
    }

    private fun callApi() {
        viewModel.StoreViewApi(token, storeId)
    }

    private fun callStoreProductApi(search: String, page: Int = 1, limit : Int = 10) {
        viewModel.StoreProductListApi(token, storeId, search, page, limit)
    }

    private fun searchApi() {
        binding.DFsearch.addTextChangedListener { editable ->
            editable?.let {
                if (editable.toString().isNotEmpty()) {
                    search = editable.toString()
                    isSearching = true
                    callStoreProductApi(search, page)
                } else {
                    search = ""
                    isSearching = true
                    callStoreProductApi(search, page)
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveLoginResponse()
        ObserveViewStoreResponce()
        ObserveAddWishListResponce()
        ObserveStoreProductListResponce()
    }

    private fun ObserveStoreProductListResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._StoreProductListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            storeProductData = response.data.result.docs
//                            if (!dataLoadFlag) {
//                                storeProductData.clear()
//                            }
                            pages = response.data.result.remainingItems % limit
                            setProductAdapter(storeProductData)

                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@StoreViewProductFragment).show(
                                    it,
                                    "MyCustomFragment"
                                )
                            }
                        }
                    }
                    is Resource.Error -> {

                        ProgressBar.hideProgress()
                        binding.progressBarPagination.visibility = View.GONE
                        storeProductData.clear()
                        setProductAdapter(storeProductData)
//                        response.message?.let { message ->
//                            androidExtension.alertBox(message, requireContext())
//                        }
                    }

                    is Resource.Loading -> {
//                        if (loaderFlag){
//                            Progresss.start(requireContext())
//                        }
                    }

                    is Resource.Empty -> {
                        ProgressBar.hideProgress()
                    }
                }

            }
        }
    }

    private fun ObserveViewStoreResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._StoreViewStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        ProgressBar.hideProgress()

                        if (response.data?.responseCode == 200) {

                            Glide.with(requireContext())
                                .load(response.data.result.store.storeImage)
                                .placeholder(R.drawable.store_view_placeholder).into(binding.storeImage)
                            binding.storeName.text = response.data.result.store.storeName
                            binding.tvAddress.text = response.data.result.store.storeAddress
                            binding.tvPincode.text = "Pincode - ${response.data.result.zipCode}"
                            binding.storeNumber.text =
                                "${response.data.result.countryCode} ${response.data.result.mobileNumber}"
                            binding.txtLikeNo.text = response.data.result.noOfLikes.toString()
                            binding.txtProductNo.text =
                                response.data.result.noOfProducts.toString()

                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@StoreViewProductFragment).show(
                                    it,
                                    "MyCustomFragment"
                                )
                            }
                        }

                    }
                    is Resource.Error -> {

                        ProgressBar.hideProgress()
                        storeProductData.clear()
                        setProductAdapter(storeProductData)
                        response.message?.let { message ->
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {
//                        if (!isSearching) {
//                            ProgressBar.showProgress(requireContext())
//                        }
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


                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@StoreViewProductFragment).show(
                                    it,
                                    "MyCustomFragment"
                                )
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
//                            ProgressBar.showProgress(requireContext())
                    }

                    is Resource.Empty -> {
                        ProgressBar.hideProgress()
                    }
                }
            }
        }
    }

    private fun setProductAdapter(data: ArrayList<ProductListDoc>) {

//        auxilaryProductData.addAll(data)
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productRecyclerView.layoutManager = layoutManager
        binding.productRecyclerView.adapter = ProductsAdapter(requireContext(), data, this, this, "")
    }

    override fun sendProductId(productId: String) {
        val amount = productId
        val bundle = bundleOf("productId" to amount, "storeClick" to "storeClick")
        findNavController().navigate(R.id.viewProduct, bundle)
    }


    override fun wishListClick(productId: String, view:ImageView,view1:ImageView) {

        if (!SavedPrefManager.getBooleanPreferences(requireContext(), SavedPrefManager.loggedIn)) {
            activity?.supportFragmentManager?.let {
                LoginScreen().show(
                    it, "Login Customer"
                )
            }
        } else {
            ProgressBar.showProgress(requireContext())
            sharedViewModel.addWishListApi(token, productId)
            LikeImage = view1
            UnLikeImage = view
        }
    }


    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.setText("Store")
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

