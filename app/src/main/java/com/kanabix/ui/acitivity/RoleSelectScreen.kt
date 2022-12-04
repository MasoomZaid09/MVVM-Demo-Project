package com.kanabix.ui.acitivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.complyany.entity.permission.RequestPermission
import com.fram.farmserv.utils.Currency_from_Location
import com.google.android.gms.location.LocationServices
import com.kanabix.R
import com.kanabix.databinding.ActivityRoleSelectScreenBinding
import com.kanabix.utils.LocationClass
import com.kanabix.utils.MyLocationPermisstion
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.SharedLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class RoleSelectScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectScreenBinding
    private val sharedLoginViewModel: SharedLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        RequestPermission.requestMultiplePermissions(this@RoleSelectScreen)


        if (SavedPrefManager.getBooleanPreferences(
                this@RoleSelectScreen,
                SavedPrefManager.loggedIn
            )
        ) {
            if (SavedPrefManager.getStringPreferences(this, SavedPrefManager.ROLE)
                    .equals("Customer")
            ) {

                sharedLoginViewModel.saveToken(
                    SavedPrefManager.getStringPreferences(
                        this,
                        SavedPrefManager.TOKEN
                    ).toString()
                )
                Intent(this, FragmentContainerActivity::class.java).also {
                    it.putExtra(
                        "token",
                        SavedPrefManager.getStringPreferences(this, SavedPrefManager.TOKEN)
                            .toString()
                    )
                    startActivity(it)
                }
            } else {
                Intent(this, FragmentContainerDeliveryActivity::class.java).also {
                    it.putExtra("roleFlag", "DeliveryPartner")
                    startActivity(it)
                    finishAffinity()
                }
            }
        }

        binding.CustomerLogin.setOnClickListener {

            if (SavedPrefManager.getStringPreferences(this,SavedPrefManager.LAT).equals("")){

                lifecycleScope.launch(Dispatchers.Main) {

                    ProgressBar.showProgress(this@RoleSelectScreen)

                    delay(3000L)

                    ProgressBar.hideProgress()
                    goToCustomerHome()
                    }
            }else{
                goToCustomerHome()
            }
        }

        binding.RetailerLogin.setOnClickListener {
            goToDeliveryPartnerHome()
        }

    }


    private fun goToCustomerHome(){
        SavedPrefManager.saveStringPreferences(this, SavedPrefManager.ROLE, "Customer")
        SavedPrefManager.saveStringPreferences(this, SavedPrefManager.TOKEN, "")
        sharedLoginViewModel.saveToken("")

        Intent(this, FragmentContainerActivity::class.java).also {
            it.putExtra("token", "")
            startActivity(it)
        }
    }

    private fun goToDeliveryPartnerHome(){
        SavedPrefManager.saveStringPreferences(this, SavedPrefManager.ROLE, "DeliveryPartner")
        Intent(this, LoginActivity::class.java).also {
            it.putExtra("roleFlag", "DeliveryPartner")
            startActivity(it)
        }
    }

}