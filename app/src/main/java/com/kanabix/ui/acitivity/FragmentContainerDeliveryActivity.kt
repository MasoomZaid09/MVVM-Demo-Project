package com.kanabix.ui.acitivity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.kanabix.R
import com.kanabix.databinding.ActivityFragmentContainerDeliveryBinding
import com.kanabix.utils.SavedPrefManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentContainerDeliveryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFragmentContainerDeliveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentContainerDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SavedPrefManager.saveStringPreferences(this, SavedPrefManager.ROLE, "DeliveryPartner")

        val navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)

        // always show selected Bottom Navigation item as selected (return true)
        // remove multiple back stacks
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            // Pop everything up to the reselected item

            NavigationUI.onNavDestinationSelected(item, navController)
            val reselectedDestinationId = item.itemId
            navController.popBackStack(reselectedDestinationId, inclusive = false)

            return@setOnItemSelectedListener true
        }
    }



}