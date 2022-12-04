package com.kanabix.ui.fragment.toolbar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanabix.R
import com.kanabix.adapters.NotificationAdapter
import com.kanabix.api.response.NotificationListDoc
import com.kanabix.customclicks.NotificationDeleteListener
import com.kanabix.customclicks.NotificationViewListener
import com.kanabix.databinding.FragmentNotificationBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.dialogs.NotificationDeleteDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.addressDelete
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Notification : Fragment(), NotificationDeleteListener,
    NotificationViewListener ,sessionExpiredListener {

    lateinit var NotificationAdapter: NotificationAdapter

    var textView: TextView? = null

    private lateinit var binding: FragmentNotificationBinding

    lateinit var imageView_back: LinearLayout
    lateinit var profile_ll: LinearLayout

    lateinit var greyBell_ImageView: ImageView
    lateinit var greenBell_ImageView: ImageView
    lateinit var greyCart: ImageView
    lateinit var greenCart: ImageView
    lateinit var greenHeart: ImageView
    lateinit var greyHeart: ImageView
    private val viewModel: NotificationViewModel by viewModels()
    var notificationType = ""
    var notificationList = ArrayList<NotificationListDoc>()
    var token = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //call notification api
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        viewModel.notificationListApi(token)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()

        imageView_back.setOnClickListener {
            greyBell_ImageView.visibility = View.VISIBLE
            greenBell_ImageView.visibility = View.GONE
            findNavController().navigate(R.id.action_notification_to_customerHomePage)
        }

        binding.clearAll.setOnClickListener {
            viewModel.notificationClearAllApi(token)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_notification_to_customerHomePage)
                }
            })

        ObserveNotificationListResponse()
        ObserveNotificationDeleteResponse()
        ObserveNotificationClearResponse()
        ObserveNotificationViewResponse()
    }

    private fun ObserveNotificationListResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._notificationListStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {


                                notificationList =
                                    response.data.result.docs as ArrayList<NotificationListDoc>

                                if (notificationList.size > 0) {

                                    binding.RLNotification.visibility = View.VISIBLE
                                    binding.noNotificationItem.visibility = View.GONE
                                    setCategoryAdapter(notificationList)
                                } else {
                                    binding.RLNotification.visibility = View.GONE
                                    binding.noNotificationItem.visibility = View.VISIBLE
                                }
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@Notification).show(it, "MyCustomFragment")
                                }
                            } else if (response.data?.responseCode == 201) {

                            }
                        }
                        is Resource.Error -> {

                            if (response.data?.responseCode == 404) {

                                notificationList.clear()
                                setCategoryAdapter(notificationList)
                            } else {
                                response.message?.let { message ->
                                    androidExtension.alertBox(message, requireContext())
                                }
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

    private fun ObserveNotificationDeleteResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._notificationDeleteStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {
                            viewModel.notificationListApi(token)
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@Notification).show(it, "MyCustomFragment")
                            }
                        } else if (response.data?.responseCode == 201) {

                        }
                    }
                    is Resource.Error -> {

                        if (response.data?.responseCode == 404) {

                        } else {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
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

    private fun ObserveNotificationClearResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._notificationClearAllStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {
                            binding.RLNotification.visibility = View.GONE
                            binding.noNotificationItem.visibility = View.VISIBLE
                            notificationList.clear()
                            setCategoryAdapter(notificationList)
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@Notification).show(it, "MyCustomFragment")
                            }
                        } else if (response.data?.responseCode == 201) {

                        }
                    }
                    is Resource.Error -> {

                        if (response.data?.responseCode == 404) {

                        } else {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
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

    private fun ObserveNotificationViewResponse() {

        lifecycleScope.launch {
            viewModel._notificationViewStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {

                            try {

                                if (notificationType == "ORDER") {

                                    val bundle = bundleOf("flag" to "Notification")
                                    findNavController().navigate(
                                        R.id.action_notification_to_myOrdersFragment,
                                        bundle
                                    )
                                }
                                else if (notificationType == "DELIVER_ORDER" || notificationType == "ADD_TRACKING") {

                                    val amount = response.data.result.orderId.id
                                    val bundle =
                                        bundleOf("orderId" to amount, "flag" to "Notification")

                                    findNavController().navigate(
                                        R.id.action_notification_to_orderDescription,
                                        bundle
                                    )
                                }
                                else if (notificationType == "ADD_DEAL") {
                                    val amount = response.data.result.dealId._id
                                    val bundle =
                                        bundleOf("dealId" to amount, "flag" to "Notification")

                                    findNavController().navigate(
                                        R.id.action_notification_to_viewDealScreen,
                                        bundle
                                    )
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }else if(response.data?.responseCode == 440){

                            fragmentManager?.let {
                                LogOutDialog(this@Notification).show(it, "MyCustomFragment")
                            }
                        }
                    }
                    is Resource.Error -> {

                        if(response.data?.responseCode == 404) {

                        } else {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
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


    fun setCategoryAdapter(notificationList: ArrayList<NotificationListDoc>) {
        binding.notificationRv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        NotificationAdapter = NotificationAdapter(this, this, notificationList)
        binding.notificationRv.adapter = NotificationAdapter
    }

    fun setToolBar() {
        textView = activity?.findViewById(R.id.PreLoginTitle_TextView2)
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        imageView_back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        textView?.setText("Notification")
        textView?.visibility = View.VISIBLE
        imageView_back.visibility = View.VISIBLE
        profile_ll.visibility = View.GONE

        greenCart = activity?.findViewById(R.id.greenCart)!!
        greyCart = activity?.findViewById(R.id.greyCart)!!
        greenBell_ImageView = activity?.findViewById(R.id.greenBell_ImageView)!!
        greyBell_ImageView = activity?.findViewById(R.id.greyBell_ImageView)!!
        greenHeart = activity?.findViewById(R.id.greenHeart)!!
        greyHeart = activity?.findViewById(R.id.greyHeart)!!

        greenCart.visibility = View.GONE
        greyCart.visibility = View.VISIBLE
        greenBell_ImageView.visibility = View.VISIBLE
        greyBell_ImageView.visibility = View.GONE
        greyHeart.visibility = View.VISIBLE
        greenHeart.visibility = View.GONE

    }



    override fun notificationViewClick(type: String, _id: String) {

        notificationType = type
        viewModel.notificationViewApi(token, _id)
    }


    // notification delete
    override fun notificationOpenPopUp(_id: String) {

        fragmentManager?.let {
            NotificationDeleteDialog(
                this,
                _id,
                getString(R.string.notification)

            ).show(it, "MyNotificationFragment")
        }
    }

    override fun notificationDeleteClick(_id: String) {

        viewModel.notificationDeleteApi(token, _id)
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

