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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.notificationview.view.*
import kotlinx.android.synthetic.main.notificationview.view.warningtext2
import kotlinx.android.synthetic.main.qnaview.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


// Here ":" symbol is indicate that LoginFragment
// is child class of Fragment Class
class qnaview(name:String,id:String,uid:String,Isauthorised:Boolean): Fragment() {

    val handler= Handler()
    var idi:String= id.toString()
    var uidi:String= uid.toString()
    var name:String= name
    var Isauthorised:Boolean= Isauthorised


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {



        val view= inflater.inflate(

            R.layout.qnaview, container, false

        )

        if(!Isauthorised && name!="Home"){

            view.warningtext3.text  = "Your request to join $name is pending"

        }else
        {



       ///////////









        }









        return view
    }















}
