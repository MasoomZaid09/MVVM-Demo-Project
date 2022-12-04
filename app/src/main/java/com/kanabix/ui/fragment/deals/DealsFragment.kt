package com.kanabix.ui.fragment.deals

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exobe.Adapter.CategoryAdpter
import com.google.android.gms.location.LocationServices
import com.kanabix.R
import com.kanabix.databinding.FragmentDealsBinding
import com.kanabix.interfaces.DealProductClick
import com.kanabix.adapters.DealsAdapter
import com.kanabix.api.Constants
import com.kanabix.api.response.DealListDoc
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.popupItemClickListner
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.paging.PageListener
import com.kanabix.paging.PaginationUtils
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.LocationClass
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.DealListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DealsFragment : Fragment(), DealProductClick ,sessionExpiredListener {

    private lateinit var binding: FragmentDealsBinding
    private lateinit var adapter: DealsAdapter
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

    //    var categoryvalue = ""
    var latitute: Double = 0.0
    var logitute: Double = 0.0

    var categoryData = ""
    var isSearching = false

    var dealData: ArrayList<DealListDoc> = ArrayList()

//    var data: ArrayList<CategoryManagementDoc> = ArrayList()
//    private lateinit var dialog: Dialog
//    private lateinit var recyclerView: RecyclerView

    // paginations

    var isScrolling = false
    var currentItems = 0
    var totalItems = 0
    var scrollOutItems = 0
    var page = 1
    var search = ""

    private val viewModel: DealListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationpermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDealsBinding.inflate(layoutInflater, container, false)
        val view = binding.root

//        setDealAdapter(dealData)

        searchApi()
        setToolBar()

        Back.setOnClickListener {
            findNavController().popBackStack()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponce()
    }

    // Api calls
    private fun searchApi() {

        var job: Job? = null
        binding.DFsearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        isSearching = true
                        search = editable.toString()
                        callApi(latitute,logitute,search)
                    } else {
                        isSearching = true
                        search = ""
                        callApi(latitute,logitute,search)
                    }
                }
            }
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
            isSearching = false
            callApi(latitute,logitute,search)

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

    private fun callApi(lat: Double,log:Double,search: String,page: Int = 1){
        viewModel.dealListApi(lat, log, search, "", page, 10)
    }

    //Observers
    private fun ObserveResponce() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._dealListStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {
                                binding.txtNoData.visibility = View.GONE
                                dealData = response.data.result.docs
                                setDealAdapter(dealData)

                            }else if(response.data?.responseCode == 201){
                                binding.txtTitleHuray.visibility = View.GONE
                                binding.txtNoData.text = response.data.responseMessage
                                binding.txtNoData.visibility = View.VISIBLE
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@DealsFragment).show(it, "MyCustomFragment")
                                }
                            }
                        }

                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            dealData.clear()
                            setDealAdapter(dealData)
                            binding.txtNoData.visibility = View.VISIBLE
                            binding.txtTitleHuray.visibility = View.GONE
                        }

                        is Resource.Loading -> {

                            if (!isSearching){
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

        PreLoginTitle_TextView2.setText("Deals")
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


    fun setDealAdapter(data: ArrayList<DealListDoc>) {

        layoutManager = GridLayoutManager(requireContext(),2)
        binding.recyclerviewDeals.layoutManager = layoutManager
        adapter = DealsAdapter(requireContext(), data, this)
        binding.recyclerviewDeals.adapter = adapter


    }




    override fun DealProductClick(dealId: String) {
        val amount = dealId
        val bundle = bundleOf("dealId" to amount, "flag" to "dealFragment")
        findNavController().navigate(R.id.viewDealScreen, bundle)
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
