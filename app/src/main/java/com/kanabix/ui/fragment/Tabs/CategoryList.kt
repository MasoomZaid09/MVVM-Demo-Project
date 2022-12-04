package com.kanabix.ui.fragment.Tabs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exobe.Adapter.CategoryAdpter
import com.github.razir.progressbutton.bindProgressButton
import com.kanabix.R
import com.kanabix.api.Constants.SEARCH_TIME_DELAY
import com.kanabix.api.response.CategoryManagementDoc
import com.kanabix.customclicks.CommonInterface
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.CategoryClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.paging.PageListener
import com.kanabix.paging.PaginationUtils
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.CartManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_kanabix_viewModel_AniketViewModel_HiltModules_BindsModule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull


@AndroidEntryPoint
class CategoryList : Fragment(), CategoryClick ,sessionExpiredListener {
    lateinit var recyclerViewProduct: RecyclerView
    lateinit var ProductLatestAdapter: CategoryAdpter
    lateinit var DFsearch: EditText
    lateinit var txtNoData: TextView
    lateinit var searchLayout: RelativeLayout

    var isSearching = false

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
    var data: ArrayList<CategoryManagementDoc> = ArrayList()

    lateinit var title: TextView
    lateinit var cart: ImageView
    lateinit var filter: ImageView
    lateinit var back: ImageView
    lateinit var ProductImageView: ImageView
    lateinit var DealsImageView: ImageView
    lateinit var greyBellImageView: ImageView
    lateinit var MenuClick: ImageView
    lateinit var mainHeader: RelativeLayout
    private val viewModel: CartManagementViewModel by viewModels()

    // paginations

    var isScrolling = false
    var currentItems = 0
    var totalItems = 0
    var scrollOutItems = 0
    var page = 1
    var search = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString("search")?.let {
            search = it
        }
        callApi(search,page)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        searchLayout = view.findViewById(R.id.searchLayout)
        setToolBar()

        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct)
        mainHeader = activity?.findViewById(R.id.RelativeLayout)!!
        DFsearch = view.findViewById(R.id.DFsearch)
        txtNoData = view.findViewById(R.id.txtNoData)
        mainHeader.visibility = View.VISIBLE


        DFsearch.setText(search)

        setAdapter(data)
        searchApiCalls()

        return view
    }

    private fun callApi(search : String,page : Int = 1){
        viewModel.CartManagmentApi(search, page, 10)
    }

    private fun searchApiCalls() {

        var job : Job? = null
        DFsearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
//
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        isSearching = true
                        search = editable.toString()
                        callApi(search,page)
                    }else{
                        isSearching = true
                        search = ""
                        callApi(search,page)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponce()
    }


    private fun ObserveResponce() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._CartStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200) {
                                txtNoData.visibility = View.GONE
                                data = response.data.result.docs
                                if (data.size > 0){
                                    searchLayout.visibility = View.VISIBLE
                                    setAdapter(data)
                                }else{
                                    searchLayout.visibility = View.GONE
                                }

                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@CategoryList).show(it, "MyCustomFragment")
                                }
                            }
                        }

                        is Resource.Error -> {

                            ProgressBar.hideProgress()

                            data.clear()
                            setAdapter(data)
                            txtNoData.visibility = View.VISIBLE
                            searchLayout.visibility = View.GONE
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
    }

    private fun setAdapter(data: ArrayList<CategoryManagementDoc>) {

        val LayoutManager = GridLayoutManager(requireContext(),4)
        recyclerViewProduct.apply {
            layoutManager = LayoutManager
        }
        ProductLatestAdapter = CategoryAdpter(activity as Context, data, this)
        recyclerViewProduct.adapter = ProductLatestAdapter

//        recyclerViewProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//                currentItems = LayoutManager.childCount
//                totalItems = LayoutManager.itemCount
//                scrollOutItems = LayoutManager.findFirstVisibleItemPosition()
//
//                if(isScrolling && (currentItems + scrollOutItems == totalItems))
//                {
//                    isScrolling = false
//                    isSearching = false
//                    callApi(search,page+1)
//                }
//            }
//        })

    }

    override fun categoryClick(categoryId: String) {
        val amount = categoryId
        val bundle = bundleOf("categoryId" to amount)
        findNavController().navigate(R.id.productsList, bundle)
    }

    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.setText("Category")
        PreLoginTitle_TextView2.visibility = View.VISIBLE
        iconsLayout.visibility = View.VISIBLE
        profile_ll.visibility = View.GONE
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