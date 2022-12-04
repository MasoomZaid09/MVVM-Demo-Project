package com.kanabix.ui.fragment.toolbar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.exobe.Utils.CommonFunctions
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.adapters.CartAdapter
import com.kanabix.api.response.CartListResponse
import com.kanabix.customclicks.adaptorCartItemListener
import com.kanabix.customclicks.cartUpdateListener
import com.kanabix.customclicks.deleteCartItemListener
import com.kanabix.databinding.FragmentCartBinding
import com.kanabix.dialogs.CartDeleteDialog
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.dialogs.NotificationDeleteDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.extensions.androidExtension.initLoader
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(), adaptorCartItemListener, deleteCartItemListener,
    cartUpdateListener ,sessionExpiredListener{

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    var flag = ""

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
    var price: ArrayList<Double> = ArrayList()
    var token = ""
    var BAG_AMOUNT = 0.0
    var FINAL_AMOUNT = 0.0
    var counter = 1
    var PER_PRODUCT_DELIVERY_FEE = 20
    var shippingPrice = 0.0

    var fcount: Int? = null
    var fprice: Number? = null
    var fquantity: String? = null
    var ftvQuantity: TextView? = null
    var fIncAndDecLL: LinearLayout? = null
    var floader: LottieAnimationView? = null
    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()

        Back.setOnClickListener {
            greyCart.visibility = View.VISIBLE
            greenCart.visibility = View.GONE
            findNavController().popBackStack(R.id.customerHomePage, false)
        }

        binding.btnCartContinue.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_deleviryAddNewAddress)
        }
        cartListApi()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveCartListResponse()
        ObserveDeleteCartItemResponse()
        ObserveUpdateCartItemResponse()
    }

    private fun cartListApi() {
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        viewModel.cartListApi(token)
    }

    private fun deleteCartItemApi(_id: String) {
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        viewModel.deleteCartItemApi(token, _id)
    }

    private fun UpdateQuantityApi(
        count: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView,
        fIncAndDecLL: LinearLayout?,
        floader: LottieAnimationView,
        flag: String
    ) {
        var jsonObject = JsonObject()
        var finalCount = 0
        if (flag.equals("INCREMENT")) {
            if (!quantity.equals("")) {
                var countQty = Integer.parseInt(quantity)
                finalCount = countQty + 1
                jsonObject.addProperty("quantity", finalCount)
            } else {
                finalCount = 0
                jsonObject.addProperty("quantity", finalCount)

            }
        } else {
            if (!quantity.equals("")) {
                var countQty = Integer.parseInt(quantity)
                finalCount = countQty - 1
                jsonObject.addProperty("quantity", finalCount)

            } else {
                finalCount = 0
                jsonObject.addProperty("quantity", finalCount)
            }
        }
        fIncAndDecLL!!.visibility = View.GONE
        floader.initLoader(true)
        viewModel.updateCartItemApi(token, id!!, jsonObject)

    }

    private fun ObserveCartListResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel._cartListStateFlow.collectLatest { response ->

                    delay(100L)

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {
                                var cartListData = response.data.result

                                if (cartListData.size > 0) {
                                    binding.cartListLayout.isVisible = true
                                    binding.noCartItem.isVisible = false

                                    var priceValue = 0.0
                                    shippingPrice = 0.0
                                    for (i in cartListData.indices) {
                                        priceValue += cartListData.get(i).totalPrice.toDouble() * cartListData.get(
                                            i
                                        ).quantity!!
                                        shippingPrice += PER_PRODUCT_DELIVERY_FEE
                                    }
                                    BAG_AMOUNT = priceValue
                                    FINAL_AMOUNT = BAG_AMOUNT + shippingPrice

                                    if (cartListData.size == 1){
                                        binding.priceDetail.text =
                                            "Price Details (${cartListData.size} Item)"
                                    }else if(cartListData.size > 1){
                                        binding.priceDetail.text =
                                            "Price Details (${cartListData.size} Items)"
                                    }
                                    binding.bagPrice.text =
                                        CommonFunctions.currencyFormatter(BAG_AMOUNT)
                                    binding.shippingPrice.text =
                                        CommonFunctions.currencyFormatter(shippingPrice)
                                    binding.finalPrice.text =
                                        CommonFunctions.currencyFormatter(FINAL_AMOUNT)
                                    setCartListAdaptor(cartListData)
                                } else {
                                    binding.cartListLayout.isVisible = false
                                    binding.noCartItem.isVisible = true
                                }
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@CartFragment).show(it, "MyCustomFragment")
                                }
                            }


                            else if (response.data?.responseCode == 201) {

                            }
                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 404) {

                            } else {
                                response.message?.let { message ->
                                    androidExtension.alertBox(message, requireContext())
                                }
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

    private fun ObserveDeleteCartItemResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel._deleteCartItemStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                var qty = Integer.parseInt(fquantity)
                                BAG_AMOUNT -= (fprice!!.toDouble() * qty)
                                FINAL_AMOUNT = BAG_AMOUNT + shippingPrice
                                _binding!!.bagPrice.text =
                                    CommonFunctions.currencyFormatter(BAG_AMOUNT)
                                _binding!!.shippingPrice.text =
                                    CommonFunctions.currencyFormatter(shippingPrice.toDouble())
                                _binding!!.finalPrice.text =
                                    CommonFunctions.currencyFormatter(FINAL_AMOUNT)

                                cartListApi()
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@CartFragment).show(it, "MyCustomFragment")
                                }
                            } else if (response.data?.responseCode == 201) {

                            }
                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
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
                            ProgressBar.hideProgress()
                        }

                    }

                }
            }
        }
    }

    private fun ObserveUpdateCartItemResponse() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel._updateCartItemStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {

                                try {

                                    fIncAndDecLL!!.visibility = View.VISIBLE
                                    floader!!.initLoader(false)
                                    ftvQuantity!!.text = fcount.toString()
                                    if (flag.equals("INCREMENT")) {
                                        BAG_AMOUNT += fprice!!.toDouble()

                                        FINAL_AMOUNT = BAG_AMOUNT + shippingPrice
                                        _binding!!.bagPrice.text =
                                            CommonFunctions.currencyFormatter(BAG_AMOUNT)
                                        _binding!!.shippingPrice.text =
                                            CommonFunctions.currencyFormatter(shippingPrice.toDouble())
                                        _binding!!.finalPrice.text =
                                            CommonFunctions.currencyFormatter(FINAL_AMOUNT)
                                    } else {

                                        BAG_AMOUNT -= fprice!!.toDouble()
                                        FINAL_AMOUNT = BAG_AMOUNT + shippingPrice
                                        _binding!!.bagPrice.text =
                                            CommonFunctions.currencyFormatter(BAG_AMOUNT)
                                        _binding!!.shippingPrice.text =
                                            CommonFunctions.currencyFormatter(shippingPrice.toDouble())
                                        _binding!!.finalPrice.text =
                                            CommonFunctions.currencyFormatter(FINAL_AMOUNT)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@CartFragment).show(it, "MyCustomFragment")
                                }
                            }
                            else if (response.data?.responseCode == 201) {

                            }
                        }
                        is Resource.Error -> {

                            fIncAndDecLL!!.visibility = View.VISIBLE
                            floader!!.initLoader(false)
                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 404) {

                            } else {
                                response.message?.let { message ->
                                    androidExtension.alertBox(message, requireContext())
                                }
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
    }

    private fun setCartListAdaptor(cartListData: List<CartListResponse.CartListResult>) {
        binding.CartRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.CartRecycler.adapter = CartAdapter(requireContext(), cartListData, this, this)
    }


    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.setText("Cart")
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


        greenCart.visibility = View.VISIBLE
        greyCart.visibility = View.GONE
        greenBell_ImageView.visibility = View.GONE
        greyBell_ImageView.visibility = View.VISIBLE
        greyHeart.visibility = View.VISIBLE
        greenHeart.visibility = View.GONE
    }

    override fun deleteItemClick(
        count: Int?,
        price: Number?,
        _id: String?,
        quantity: String,
        tvQuantity: TextView
    ) {
        fcount = count
        fprice = price
        fquantity = quantity
        ftvQuantity = tvQuantity
        deleteCartItemApi(_id!!)
    }

    override fun adaptorItemDeleteClick(
        count: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView
    ) {
        fcount = count
        fprice = price
        fquantity = quantity
        ftvQuantity = tvQuantity
        fragmentManager?.let {
            CartDeleteDialog(
                this,
                getString(R.string.cart), count, price, id, quantity, tvQuantity
            ).show(it, "MyCustomFragment")
        }
    }

    override fun incrementAmount(
        count: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView,
        IncAndDecLL: LinearLayout,
        loader: LottieAnimationView
    ) {
        if (count != null) {
            counter = count
        }
        if (counter!! > 0) {
            fcount = count
            fprice = price
            fquantity = quantity
            ftvQuantity = tvQuantity
            fIncAndDecLL = IncAndDecLL
            floader = loader

            UpdateQuantityApi(
                count,
                price,
                id,
                quantity,
                tvQuantity,
                fIncAndDecLL,
                floader!!,
                "INCREMENT"
            )
            flag = "INCREMENT"
        }
    }

    override fun decrementAmount(
        count: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView,
        IncAndDecLL: LinearLayout,
        loader: LottieAnimationView
    ) {
        if (counter!! > 0) {
            fcount = count
            fprice = price
            fquantity = quantity
            ftvQuantity = tvQuantity
            fIncAndDecLL = IncAndDecLL
            floader = loader
            UpdateQuantityApi(
                count,
                price,
                id,
                quantity,
                tvQuantity,
                fIncAndDecLL,
                floader!!,
                "DECREMENT"
            )
            flag = "DECREMENT"
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