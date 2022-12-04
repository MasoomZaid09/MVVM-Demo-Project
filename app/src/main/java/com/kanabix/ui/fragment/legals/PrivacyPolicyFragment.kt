package com.kanabix.ui.fragment.legals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanabix.R
import com.kanabix.databinding.FragmentPrivacyPolicyBinding
import com.kanabix.databinding.FragmentRerferIdBinding


class PrivacyPolicyFragment : Fragment() {

    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPrivacyPolicyBinding.inflate(layoutInflater, container, false)
        val view = binding.root


        return view
    }


}