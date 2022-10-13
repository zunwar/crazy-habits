package com.example.crazy_habits.fragments

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.R


class SplashFragment : Fragment() {


    /**
     * перешел на google splash screen
     */

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.fragment_splash, container, false)
//
////        //выключение back click...
////        requireActivity().onBackPressedDispatcher.addCallback(this) {
////            // Handle the back button event - ничего не делаем
////        }
//        Handler(Looper.getMainLooper()).postDelayed({
//            findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment2)
//        }, 1500)
//
//        return  view
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
//    }


}