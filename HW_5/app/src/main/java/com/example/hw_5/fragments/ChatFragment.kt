package com.example.hw_5.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.hw_5.R
import com.example.hw_5.navigate
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var count: Int
        try {
            count = ChatFragmentArgs.fromBundle(requireArguments()).count
        } catch (e: Exception) {
            count = 0
        }

        view.findViewById<Button>(R.id.chat_button).setOnClickListener {
            navigate(ChatFragmentDirections.actionChatFragmentSelf(count + 1))
        }
        var text = StringBuilder("0")
        for (i in 1..count) {
            text.append("->$i")
        }
        chat_text.text = text.toString()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

}