package com.kanabix.ui.acitivity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.databinding.ActivityFragmentContainerBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.ui.bottomSheets.LoginScreen
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.MyProfileViewModel
import com.kanabix.viewModel.SharedLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentContainerBinding
    lateinit var navController: NavController
    var token = ""

    private val viewModel: MyProfileViewModel by viewModels()
    private val sharedLoginViewModel: SharedLoginViewModel by viewModels()

    var flag = ""
    var fcm = ""
    var orderId = ""

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("paymentFlag")?.let {
            flag = it
        }
        intent.getStringExtra("orderId")?.let {
            orderId = it
        }
        intent.getStringExtra("token")?.let {
            token = it
        }
        intent.getStringExtra("fcm")?.let {
            fcm = it
        }

        SavedPrefManager.saveStringPreferences(this,SavedPrefManager.TOKEN,token)

        if (!token.equals("")){
            viewModel.myProfile(token)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        if (flag.equals("payment")) {
            val ammount = bundleOf("flag" to "payment")
            navController.navigate(R.id.myOrdersFragment, ammount)
        }

        if (fcm.equals("fcm")){
            navController.navigate(R.id.notification)
        }

        // set with toolbar and bottom navigation view
        // setupActionBarWithNavController(navController)
        binding.bottomNavigationView.setupWithNavController(navController)

        // always show selected Bottom Navigation item as selected (return true)
        // remove multiple back stacks
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            // Pop everything up to the reselected item

            if (item.itemId == R.id.myOrdersFragment && !SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.loggedIn)){

                supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }
                return@setOnItemSelectedListener false

            }else if (item.itemId == R.id.myAccount && !SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.loggedIn)) {

                supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )

                }
                return@setOnItemSelectedListener false
            }
            else{
                NavigationUI.onNavDestinationSelected(item, navController)
                val reselectedDestinationId = item.itemId
                navController.popBackStack(reselectedDestinationId, inclusive = false)
                return@setOnItemSelectedListener true
            }

        }


        // toolbar clicks
        binding.notificationLayout.setOnClickListener {

            if (!SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.loggedIn)){
                supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }
            }else{
                navController.navigateUp()
                navController.navigate(R.id.notification)
            }

        }

        binding.likeLayout.setOnClickListener {
            if (!SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.loggedIn)){
                supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }
            }else{
                navController.navigateUp()
                navController.navigate(R.id.wishListFragment)
            }
        }

        binding.cartLayout.setOnClickListener {
            if (!SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.loggedIn)){
                supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }
            }else{
                navController.navigateUp()
                navController.navigate(R.id.cartFragment)
            }
        }


//    Profile click

        binding.profileLl.setOnClickListener {
            if (!SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.loggedIn)){
                supportFragmentManager?.let {
                    LoginScreen().show(
                        it, "Login Customer"
                    )
                }
            }else{
                navController.navigateUp()
                navController.navigate(R.id.myAccount)
            }
        }

    }


    override fun onStart() {
        super.onStart()

        ObserveLoginResponse()
        ObserveResponse()
    }

    private fun ObserveResponse() {

        lifecycleScope.launch {
            viewModel._myProfileFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {

                            binding.profileName.text = "Hi, ${response.data.result.name}"
                            Glide.with(this@FragmentContainerActivity)
                                .load(response.data.result.profilePic)
                                .error(R.drawable.place_holder).into(binding.profileImage)
                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@FragmentContainerActivity)
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


    fun ObserveLoginResponse(){

        sharedLoginViewModel._sharedTokenState.observe(this,Observer{ token ->
            SavedPrefManager.saveStringPreferences(this,SavedPrefManager.TOKEN,token)
            viewModel.myProfile(token)
        })
    }


//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

}