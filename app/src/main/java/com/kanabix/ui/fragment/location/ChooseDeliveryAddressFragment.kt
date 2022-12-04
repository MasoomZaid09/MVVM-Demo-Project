package com.kanabix.ui.fragment.location

import android.content.Context
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.models.ChooseDeliveryAddress
import com.kanabix.adapters.ChooseDeliveryAddressAdapter
import com.kanabix.api.response.AddressListResponse
import com.kanabix.customclicks.ChooseDeliveryAddressDelete
import com.kanabix.customclicks.editAddressListener
import com.kanabix.databinding.FragmentChooseDeliveryAddressBinding
import com.kanabix.dialogs.DeleteDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.PaymentManagement
import com.kanabix.interfaces.addressDeleteListener
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.DeliveryAddAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChooseDeliveryAddressFragment : Fragment(), PaymentManagement, addressDeleteListener,
    ChooseDeliveryAddressDelete,
    editAddressListener {

    private lateinit var binding: FragmentChooseDeliveryAddressBinding
    lateinit var mContext : Context

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

    var amount = ""
    var flag = ""
    var token = ""

    var itemList= ArrayList<AddressListResponse.AddressListResult.AddressListDoc>()
    private val viewModel: DeliveryAddAddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseDeliveryAddressBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        mContext = activity?.applicationContext!!

        setToolBar()
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN)
            .toString()

        Back.setOnClickListener {
            findNavController().popBackStack()
        }

        addressListApi()

        binding.addNewAddress.setOnClickListener {

            flag = "Add"
            val bundle = bundleOf("flag" to flag, "_id" to "")
            findNavController().navigate(R.id.addAddressFragment, bundle)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveAddressListResponce()
        ObserveAddressDeleteResponce()
    }

    private fun ObserveAddressListResponce() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel._listAddressStateFlow.collectLatest { response ->

                    delay(200L)

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {

                                itemList = response.data.result.docs
                                setAdapter(itemList)
                            }
                        }

                        is Resource.Error -> {
                            ProgressBar.hideProgress()
                            itemList.clear()
                            setAdapter(itemList)
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

    private fun ObserveAddressDeleteResponce() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel._deleteAddressStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {
                                addressListApi()
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

    }


    fun addressListApi() {
        val jsonObject = JsonObject().apply {
//            addProperty("page",1)
//            addProperty("limit",10)

        }
        viewModel.listAddressApi(token, jsonObject)
    }

    override fun deleteClick(id: String,position: Int) {
        fragmentManager?.let { DeleteDialog(this,id,position).show(it, "MyCustomFragment") }
    }

    override fun deleteAdd(id: String,position: Int) {
            viewModel.deleteAddressApi(token, id)
    }

    override fun PaymentManagementClick(position: Int) {

        findNavController().popBackStack()
    }


    fun setAdapter(data: ArrayList<AddressListResponse.AddressListResult.AddressListDoc>) {
        binding.chooseDeliveryRecycler.layoutManager = LinearLayoutManager(mContext)
        binding.chooseDeliveryRecycler.adapter =
            ChooseDeliveryAddressAdapter(mContext,data, this, this, this)
    }

    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        iconsLayout.visibility=View.VISIBLE
        profile_ll.visibility=View.GONE
        Back.visibility = View.VISIBLE
        PreLoginTitle_TextView2.visibility = View.GONE

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


}