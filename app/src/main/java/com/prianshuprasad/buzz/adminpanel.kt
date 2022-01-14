package com.prianshuprasad.buzz



import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


// Here ":" symbol is indicate that LoginFragment
// is child class of Fragment Class
class adminpanel(id:String,uid:String): Fragment() {



    val handler= Handler()
    var idi:String= id.toString()
    var uidi:String= uid.toString()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        val view= inflater.inflate(

            R.layout.qnaview, container, false

        )








        return view
    }















}
