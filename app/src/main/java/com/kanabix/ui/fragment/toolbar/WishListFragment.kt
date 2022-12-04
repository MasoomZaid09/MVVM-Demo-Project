package com.kanabix.ui.fragment.toolbar

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kanabix.R
import com.kanabix.adapters.StoreWishListAdapter
import com.kanabix.adapters.WishListAdapters
import com.kanabix.api.response.StoreLikeListDoc
import com.kanabix.api.response.WishListDoc
import com.kanabix.databinding.FragmentWishListBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.*
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar.showProgress
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.SharedStoreLikeViewModel
import com.kanabix.viewModel.SharedWishListViewModel
import com.kanabix.viewModel.WishListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WishListFragment : Fragment(), ProductClick, StoreClick, WishListClick, WishListUpdateClick,
    StoreWishListClick ,sessionExpiredListener{

    private lateinit var binding: FragmentWishListBinding
    lateinit var products: WishListAdapters
    lateinit var stores: StoreWishListAdapter
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


    private var token = ""
    var productData: ArrayList<WishListDoc> = ArrayList()
    var storeData: ArrayList<StoreLikeListDoc> = ArrayList()

    private val viewModel: WishListViewModel by viewModels()
    private val sharedViewModel: SharedWishListViewModel by viewModels()
    private val storeLikedViewModel: SharedStoreLikeViewModel by activityViewModels()

    // paginations

    var isScrolling = false
    var currentItems = 0
    var totalItems = 0
    var scrollOutItems = 0
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        callStoreListApi(page)
        viewModel.wishListApi(token)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishListBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()
        Back.setOnClickListener {
            findNavController().popBackStack(R.id.customerHomePage, false)
        }

        // click on switching tab

        binding.productCard.setOnClickListener {

            binding.productCard.setCardBackgroundColor(resources.getColor(R.color.HomeThemeColor))
            binding.txtProduct.setTextColor(resources.getColor(R.color.white))

            binding.storeCard.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.txtStore.setTextColor(resources.getColor(R.color.black))

            binding.productWishlistRecyclerView.visibility = View.VISIBLE
            binding.storeWishListRecyclerView.visibility = View.GONE

            if (productData.size == 0){
                binding.txtNoData.visibility = View.VISIBLE
            }else{
                binding.txtNoData.visibility = View.GONE
            }
        }

        binding.storeCard.setOnClickListener {

            binding.storeCard.setCardBackgroundColor(resources.getColor(R.color.HomeThemeColor))
            binding.txtStore.setTextColor(resources.getColor(R.color.white))

            binding.productCard.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.txtProduct.setTextColor(resources.getColor(R.color.black))

            binding.productWishlistRecyclerView.visibility = View.GONE
            binding.storeWishListRecyclerView.visibility = View.VISIBLE

            if (storeData.size == 0){
                binding.txtNoData.visibility = View.VISIBLE
            }else{
                binding.txtNoData.visibility = View.GONE
            }
        }

        return view

    }

    private fun callStoreListApi(page: Int = 1) {
        viewModel.likedStoreList(token, page, 10)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveWishListResponse()
        ObserveAddWishListResponce()
        ObserveAddStoreLikedListResponce()
        ObserveLikeListResponse()
    }

    private fun ObserveAddWishListResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel._addWishListStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {
                            viewModel.wishListApi(token)
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@WishListFragment).show(it, "MyCustomFragment")
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

    private fun ObserveAddStoreLikedListResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            storeLikedViewModel._storelikeStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        if (response.data?.responseCode == 200) {
                            viewModel.likedStoreList(token, 1, 10)
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@WishListFragment).show(it, "MyCustomFragment")
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

    private fun ObserveWishListResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._wishListStateFlow.collect { response ->

                when (response) {
                    is Resource.Success -> {

                        com.kanabix.utils.ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {
                            binding.txtNoData.visibility = View.GONE
                            productData = response.data.result.docs
                            setProductsWishListAdapter(productData)
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@WishListFragment).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

                        com.kanabix.utils.ProgressBar.hideProgress()
                        productData.clear()
                        setProductsWishListAdapter(productData)
                        binding.txtNoData.visibility = View.VISIBLE

                    }

                    is Resource.Loading -> {
//                        com.kanabix.utils.ProgressBar.showProgress(requireContext())
                    }

                    is Resource.Empty -> {
                        com.kanabix.utils.ProgressBar.hideProgress()
                    }

                }

            }
        }
    }

    private fun ObserveLikeListResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._storeLikedListStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            com.kanabix.utils.ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                if (response.data.result.docs.size > 0) {

                                    binding.txtNoData.visibility = View.GONE
                                    storeData = response.data.result.docs
                                    setStoreWishListAdapter(storeData)

                                } else {
                                    storeData.clear()
                                    setStoreWishListAdapter(storeData)
                                    binding.txtNoData.visibility = View.VISIBLE
                                }
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@WishListFragment).show(it, "MyCustomFragment")
                                }
                            }


                        }

                        is Resource.Error -> {

                            com.kanabix.utils.ProgressBar.hideProgress()
                            storeData.clear()
                            setStoreWishListAdapter(storeData)
                            binding.txtNoData.visibility = View.VISIBLE

                        }

                        is Resource.Loading -> {
//                            com.kanabix.utils.ProgressBar.showProgress(requireContext())
                        }

                        is Resource.Empty -> {
                            com.kanabix.utils.ProgressBar.hideProgress()
                        }

                    }

                }
        }
    }


    fun setProductsWishListAdapter(data: ArrayList<WishListDoc>) {
        binding.productWishlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        products = WishListAdapters(requireContext(), data, this, this, this)
        binding.productWishlistRecyclerView.adapter = products
    }

    fun setStoreWishListAdapter(data: ArrayList<StoreLikeListDoc>) {
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.storeWishListRecyclerView.layoutManager = layoutManager
        stores = StoreWishListAdapter(requireContext(), data, this, this)
        binding.storeWishListRecyclerView.adapter = stores
    }

    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.setText("Wishlist")
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
        greyHeart.visibility = View.GONE
        greenHeart.visibility = View.VISIBLE
    }

    override fun sendStoreId(storeId: String) {
        val amount = storeId
        val bundle = bundleOf("storeId" to amount)
        findNavController().navigate(R.id.storeViewProductFragment, bundle)
    }

    override fun sendProductId(productId: String) {
        val amount = productId
        val bundle = bundleOf("productId" to amount)
        findNavController().navigate(R.id.viewProduct, bundle)
    }

    override fun wishListClick(productId: String) {
        sharedViewModel.addWishListApi(token, productId)
    }

    override fun updateWishList() {
        viewModel.wishListApi(token)
    }

    override fun storeWishListClick(StoreId: String) {
        storeLikedViewModel.AddStoreWishListApi(token, StoreId)
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
