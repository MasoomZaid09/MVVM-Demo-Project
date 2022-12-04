package com.kanabix.ui.fragment.payments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.adapters.DeleviryAddNewAdderessAdapter
import com.kanabix.api.response.AddressListResponse
import com.kanabix.customclicks.*
import com.kanabix.databinding.FragmentDeleviryAddNewAddressBinding
import com.kanabix.dialogs.DeleteDialog
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.addressDeleteListener
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.CartSummeryActivity
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.DeliveryAddAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.FieldPosition

@AndroidEntryPoint
class DeleviryAddNewAddress : Fragment(), editAddressListener , getAddressIdListener ,
    ChooseDeliveryAddressDelete ,addressDeleteListener ,sessionExpiredListener{


    private var _binding: FragmentDeleviryAddNewAddressBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: DeleviryAddNewAdderessAdapter

    var data : ArrayList<AddressListResponse.AddressListResult.AddressListDoc> = ArrayList()
    var auxilaryArray : ArrayList<AddressListResponse.AddressListResult.AddressListDoc> = ArrayList()

    var flag = ""
    var position = -1

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
    lateinit var Add_address : Button

    var amount = ""
    var token = ""

//    var pages = 0
//    var page = 1
//    var limit = 10
//    var dataLoadFlag =  false
//    var loaderFlag = true

    private val viewModel: DeliveryAddAddressViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        addressListApi()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDeleviryAddNewAddressBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()
        Back.setOnClickListener {
            findNavController().popBackStack()
        }

//        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
//        addressListApi()

        Add_address.setOnClickListener {

            flag = "Add"
            val bundle = bundleOf("flag" to flag, "_id" to "")
            findNavController().navigate(R.id.addAddressFragment, bundle)
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
//                        addressListApi(page)
//                    }
//                }
//            }
//        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OBSERVERESPONSE()
        ObserveDeleteAddressResponse()
    }


    fun addressListApi(page:Int = 1) {
        val jsonObject = JsonObject().apply {
            addProperty("page",page)
            addProperty("limit",10)
        }
        viewModel.listAddressApi(token, jsonObject)
    }

    private fun OBSERVERESPONSE() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                viewModel._listAddressStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

//                                loaderFlag = false

//                                if (loaderFlag){
//                                    auxilaryArray.clear()
//                                }
                                binding.txtNoData.visibility = View.GONE
                                data = response.data.result.docs
//                                pages = response.data.result.pages



                                setupAdapter(data)

                            }
                            else if(response.data?.responseCode == 440){
                                fragmentManager?.let {
                                    LogOutDialog(this@DeleviryAddNewAddress).show(it, "MyCustomFragment")
                                }
                            }
                        }

                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            data.clear()
                            auxilaryArray.clear()
                            setupAdapter(data)
//                            if (loaderFlag){
                                binding.txtNoData.visibility = View.VISIBLE
//                            }
                            binding.progressBarPagination.visibility = View.GONE
                        }

                        is Resource.Loading -> {
//                            if (loaderFlag){
                                ProgressBar.showProgress(requireContext())
//                            }
                        }

                        is Resource.Empty -> {
                            ProgressBar.hideProgress()
                        }
                    }
                }
        }

    }

    private fun ObserveDeleteAddressResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._deleteAddressStateFlow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

//                                auxilaryArray.removeAt(position)

//                                if (auxilaryArray.isNotEmpty()){
//                                    data.clear()
//                                    setupAdapter(data)
//                                    binding.txtNoData.visibility = View.GONE
//                                }
//                                else{
//                                    data.clear()
//                                    setupAdapter(data)
//                                    binding.txtNoData.visibility = View.VISIBLE
//                                }

                                addressListApi()
                            }
                            else if(response.data?.responseCode == 440){
                                fragmentManager?.let {
                                    LogOutDialog(this@DeleviryAddNewAddress).show(it, "MyCustomFragment")
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
                        }

                        is Resource.Empty -> {
                            ProgressBar.hideProgress()
                        }

                    }
                }
        }
    }


    private fun setupAdapter(data: List<AddressListResponse.AddressListResult.AddressListDoc>) {

//        auxilaryArray.addAll(data)
        binding.addressRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = DeleviryAddNewAdderessAdapter(requireContext(), data, this,this,this)
        binding.addressRecycler.adapter = adapter
    }


    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        Add_address = activity?.findViewById(R.id.Add_address)!!
        Add_address.visibility = View.VISIBLE

        PreLoginTitle_TextView2.setText("Address")
        PreLoginTitle_TextView2.visibility = View.VISIBLE
        iconsLayout.visibility=View.GONE
        profile_ll.visibility=View.GONE
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

    override fun editAddress(_id: String) {
        flag = "Edit"
        val bundle = bundleOf("flag" to flag, "_id" to _id)
        findNavController().navigate(R.id.addAddressFragment, bundle)
    }

    override fun getAddressIdClick(_id: String) {

        Intent(requireContext(), CartSummeryActivity::class.java).also{
            it.putExtra("_id", _id)
            startActivity(it)
        }
    }

    override fun deleteClick(id: String,position: Int) {

        fragmentManager?.let {
            DeleteDialog(
                this,
                id,position
            ).show(it, "MyCustomFragment")
        }
    }

    override fun deleteAdd(id: String,position: Int) {
        ProgressBar.showProgress(requireContext())
        viewModel.deleteAddressApi(token, id)
//        this.position = position
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
