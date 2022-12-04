package com.kanabix.ui.fragment.Tabs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanabix.R
import com.kanabix.adapters.MyOrderAdapter
import com.kanabix.api.response.OrderListDoc
import com.kanabix.databinding.FragmentMyOrdersBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.interfaces.OrderManagementClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.OrderManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_internal_builders_FragmentComponentBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MyOrdersFragment : Fragment() , OrderManagementClick ,sessionExpiredListener {

    lateinit var ordersAdapter: MyOrderAdapter
    private lateinit var binding: FragmentMyOrdersBinding

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

    var token = ""
    var data : ArrayList<OrderListDoc> = ArrayList()
    var isSearching = false
    var flag = ""

    private val viewModel : OrderManagementViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        viewModel.orderListApi(token,"")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOrdersBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()

        arguments?.getString("flag")?.let {
            flag = it
        }

        Back.setOnClickListener {

            if (flag.equals("Notification")){
                findNavController().navigate(R.id.action_myOrdersFragment_to_notification)
            }else{
                findNavController().popBackStack()
            }
        }

        searchApi()

        return view
    }


    private fun searchApi() {

        var job: Job? = null
        binding.DFsearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        isSearching = true
                        viewModel.orderListApi(token,editable.toString())
                    } else {
                        isSearching = true
                        viewModel.orderListApi(token,"")
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (flag.equals("Notification")){
                        findNavController().navigate(R.id.action_myOrdersFragment_to_notification)
                    }else{
                        findNavController().popBackStack()
                    }
                }
            })

        ObserveResponse()
    }

    private fun ObserveResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel._OrderListStateFlow.collect{ response ->

                    when(response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200){

                                binding.txtNoData.visibility = View.GONE
                                data = response.data.result.docs
                                setProductsAdapter(data)

                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@MyOrdersFragment).show(it, "MyCustomFragment")
                                }
                            }


                        }
                        is Resource.Error -> {

                            data.clear()
                            setProductsAdapter(data)
                            binding.txtNoData.visibility = View.VISIBLE
                            ProgressBar.hideProgress()
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

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setProductsAdapter(data : ArrayList<OrderListDoc>){
        ordersAdapter = MyOrderAdapter(requireContext(),data,this)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.storeRecyclerView.layoutManager = layoutManager

//        val dividerItemDecoration = DividerItemDecoration(
//            binding.storeRecyclerView.getContext(),
//            layoutManager.getOrientation()
//        )
//        binding.storeRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.storeRecyclerView.adapter = ordersAdapter
    }

    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.setText("My Orders")
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

    override fun orderManagementClick(orderId: String) {
        val amount = orderId
        val bundle = bundleOf("orderId" to amount,"flag" to "OrderManagement")
        findNavController().navigate(R.id.orderDescription, bundle)
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