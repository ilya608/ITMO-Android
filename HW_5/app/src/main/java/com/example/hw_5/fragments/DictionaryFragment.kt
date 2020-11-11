package com.example.hw_5.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.content.Context
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hw_5.R
import com.example.hw_5.navigate
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.lang.StringBuilder

class DictionaryFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var count: Int
        try {
            count = DictionaryFragmentArgs.fromBundle(requireArguments()).count
        } catch (e: Exception) {
            count = 0
        }

        view.findViewById<Button>(R.id.dictionary_button).setOnClickListener {
            navigate(DictionaryFragmentDirections.actionDictionaryFragmentSelf(count + 1))
        }
        var text = StringBuilder("0")
        for (i in 1..count) {
            text.append("->$i")
        }
        dictionary_text.text = text.toString()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

}