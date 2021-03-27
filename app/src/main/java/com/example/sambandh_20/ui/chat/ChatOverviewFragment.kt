package com.example.sambandh_20.ui.chat


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sambandh_20.R


class ChatOverviewFragment : Fragment(R.layout.fragment_chat_overview) {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_overview, container, false)
    }
}