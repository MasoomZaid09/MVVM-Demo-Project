package com.kanabix.ui.acitivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.fram.farmserv.utils.Currency_from_Location
import com.google.android.gms.location.LocationServices
import com.kanabix.R
import com.kanabix.databinding.ActivityLocationBinding
import com.kanabix.extensions.androidExtension
import com.kanabix.utils.LocationClass
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.validations.FormValidation.locationValidations
import com.kanabix.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LocationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLocationBinding
    var latitute:Double=0.0
    var logitute:Double=0.0

    var token = ""
    var currentAddress = ""
    private val viewModel : LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clicksTerms()

        binding.cbRemember.setOnCheckedChangeListener{ buttonView, isChecked ->

            if (isChecked){
                binding.tvTerms.visibility = View.GONE
                binding.tvTerms.text = ""
            }else{
                binding.tvTerms.visibility = View.VISIBLE
                binding.tvTerms.text = "*Please accept terms and conditions."
            }
        }

        binding.btnSet.setOnClickListener {
            if (locationValidations(
                    binding.etLocation,
                    binding.tvLocation,
                    binding.cbRemember,
                    binding.tvTerms
                )
            ) {
                SETLOCATIONAPI()
            }
        }

        PERMISSION()
        Controler()
    }

    override fun onStart() {
        super.onStart()

        ObserveResponce()
    }


    private fun clicksTerms(){
        val ss = SpannableString(getString(R.string.by_accessing_this_site_you_accept_the_terms_conditions_and_privacy_policy))
        val terms: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Intent(this@LocationActivity, TermsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        val privacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Intent(this@LocationActivity, PrivacyPolicyActivity::class.java).also {
                    startActivity(it)
                }
            }

        }

        // It is used to set the span to the string
        ss.setSpan(terms, 38, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(privacy, 61, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.cbRemember.text = ss
        binding.cbRemember.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun ObserveResponce()
    {
        lifecycleScope.launchWhenStarted{
            viewModel.SetUserLocation.collectLatest{ response ->

                when(response){

                    is Resource.Success -> {

                        //hideProgressBar()

                        if (response.data?.responseCode == 200){

                            var flow = SavedPrefManager.getStringPreferences(
                                this@LocationActivity,
                                SavedPrefManager.flow,
                            ).toString()

                            if (flow.equals("RegFlow")){
                                onBackPressed()
                            }else{
                                onBackPressed()
                            }
                        }
                    }

                    is Resource.Error -> {

                        //hideProgressBar()
                        response.message?.let { message ->
                            androidExtension.alertBox(message,this@LocationActivity)
                        }
                    }

                    is Resource.Loading -> {
                       // showProgressBar()
                    }

                    is Resource.Empty -> {
                      //  hideProgressBar()
                    }

                }

            }
        }

    }

    private fun PERMISSION() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }

    private fun Controler() {

        binding.locationLayout.setOnClickListener{
            locationpermission()
        }
    }

    private fun locationpermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        } else {
            LocationClass.getCurrentLocation(this)
            USERCURRENTLOCATION()
        }
    }

    private fun USERCURRENTLOCATION() {
        try {

            SavedPrefManager.getStringPreferences(this, SavedPrefManager.LAT)?.let {
                latitute = it.toDouble()
            }
            SavedPrefManager.getStringPreferences(this, SavedPrefManager.LONG)?.let {
                logitute = it.toDouble()
            }

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            currentAddress = Currency_from_Location(this).getonlyAddress(latitute, logitute)
            binding.etLocation.setText(currentAddress)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun SETLOCATIONAPI()
    {
        token = SavedPrefManager.getStringPreferences(this@LocationActivity, SavedPrefManager.TOKEN).toString()
        viewModel.SETUSERLOCATION(token,"",latitute,logitute)
    }

}