package com.kanabix.ui.fragment.Tabs

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.LocationServices
import com.kanabix.R
import com.kanabix.databinding.FragmentFindStoreNearYouBinding
import com.kanabix.adapters.StoreNearYouAdapter
import com.kanabix.api.request.StoreListRequest
import com.kanabix.api.response.StoreListDoc
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.StoreClick
import com.kanabix.interfaces.StoreWishListClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.bottomSheets.LoginScreen
import com.kanabix.utils.LocationClass
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.SharedLoginViewModel
import com.kanabix.viewModel.SharedStoreLikeViewModel
import com.kanabix.viewModel.StoreManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentFindStoreNearYou : Fragment() , StoreClick , StoreWishListClick ,sessionExpiredListener {

    private lateinit var binding:FragmentFindStoreNearYouBinding
    lateinit var layoutManagmer : GridLayoutManager

    // toolbar
    lateinit var iconsLayout : LinearLayout
    lateinit var profile_ll : LinearLayout
    lateinit var Back : LinearLayout
    lateinit var PreLoginTitle_TextView2 : TextView

    lateinit var greyBell_ImageView : ImageView
    lateinit var greenBell_ImageView : ImageView
    lateinit var greyCart : ImageView
    lateinit var greenCart : ImageView
    lateinit var greenHeart : ImageView
    lateinit var greyHeart : ImageView

    var latitute: Double = 0.0
    var logitute: Double = 0.0
    var token = ""
    lateinit var products: StoreNearYouAdapter
    var storeResponse : ArrayList<StoreListDoc> = ArrayList()

    private val viewModel : StoreManagementViewModel by viewModels()
    private val sharedViewModel : SharedStoreLikeViewModel by activityViewModels()
    private val sharedLoginScreen: SharedLoginViewModel by activityViewModels()
    // paginations

    var isScrolling = false
    var currentItems = 0
    var totalItems = 0
    var scrollOutItems = 0
    var page = 1

    var isAgain = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.TOKEN).toString()
        locationpermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFindStoreNearYouBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()

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
            callApi(page)

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

        ObserveStoreListResponce()
        ObserveLoginResponse()
        ObserveAddStoreLikeListResponce()
    }

    private fun ObserveStoreListResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._StoreManagementFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                storeResponse = response.data.result.docs
                                binding.txtTitle.visibility = View.VISIBLE
                                setStoreAdapter(storeResponse)

                            }else if(response.data?.responseCode == 201){
                                androidExtension.alertBox(response.data.responseMessage,requireContext())
                                binding.txtTitle.visibility = View.GONE

                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@FragmentFindStoreNearYou).show(it, "MyCustomFragment")
                                }
                            }
                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            storeResponse.clear()
                            setStoreAdapter(storeResponse)
                            binding.txtTitle.visibility = View.GONE
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

    private fun ObserveAddStoreLikeListResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                sharedViewModel._storelikeStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {
                                callApi()
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@FragmentFindStoreNearYou).show(it, "MyCustomFragment")
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

    private fun callApi(page:Int = 1) {
        val request = StoreListRequest()
        request.page = page
        request.limit = 500 // here we bind static
        request.lat = latitute
        request.lng = logitute
        viewModel.StoreManagementApi(token, request)
    }

    fun setStoreAdapter(data: ArrayList<StoreListDoc>){
        layoutManagmer = GridLayoutManager(requireContext(), 2)
        binding.storeRecyclerView.layoutManager = layoutManagmer
        products = StoreNearYouAdapter(requireContext(),data,this,this)
        binding.storeRecyclerView.adapter = products

//        binding.storeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                {
//                    isScrolling = true
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                currentItems = layoutManagmer.childCount
//                totalItems = layoutManagmer.itemCount
//                scrollOutItems = layoutManagmer.findFirstVisibleItemPosition()
//
//                if(isScrolling && (currentItems + scrollOutItems == totalItems))
//                {
//                    isScrolling = false
//                    callApi(page+1)
//                }
//            }
//        })

    }

    override fun sendStoreId(storeId: String) {
        val amount = storeId
        val bundle = bundleOf("storeId" to amount, "flag" to "ViewStore")
        findNavController().navigate(R.id.storeViewProductFragment, bundle)
    }

    override fun storeWishListClick(storeId: String) {

        if (!SavedPrefManager.getBooleanPreferences(requireContext(),SavedPrefManager.loggedIn)){
            activity?.supportFragmentManager?.let {
                LoginScreen().show(
                    it, "Login Customer"
                )
            }
        }else {
            sharedViewModel.AddStoreWishListApi(token, storeId)
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
        iconsLayout.visibility=View.VISIBLE
        profile_ll.visibility=View.GONE
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