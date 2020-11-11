package com.example.hw_5.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import com.example.hw_5.R
import com.example.hw_5.navigate
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.lang.StringBuilder
import java.math.BigInteger

class HomeFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var count: Int
        try {
            count = HomeFragmentArgs.fromBundle(requireArguments()).count
        } catch (e: Exception) {
            count = 0
        }

        view.findViewById<Button>(R.id.home_button).setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentSelf(count + 1))
        }
        var text = StringBuilder("0")
        for (i in 1..count) {
            text.append("->$i")
        }
        home_text.text = text.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}