package com.prianshuprasad.buzz



import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.chatsview.*
import kotlinx.android.synthetic.main.chatsview.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


// Here ":" symbol is indicate that LoginFragment
// is child class of Fragment Class
class chatsview(name:String ,id:String, uid:String,Isauthorised:Boolean): Fragment() {

    val handler= Handler()
var idi:String= id.toString()
    var uidi:String= uid.toString()
    var name:String= name
    var Isauthorised:Boolean= Isauthorised

    private lateinit var rcview: RecyclerView
    private lateinit var adapter:chatshowadapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {





        val view= inflater.inflate(

            R.layout.chatsview, container, false

        )

        if(!Isauthorised && name!="Home"){


            view.warningtext.text= "Your request to join $name is pending"


        }
// recycler view setup

rcview= view.findViewById(R.id.recyclerviewchatsview)


        rcview.layoutManager= LinearLayoutManager(view.context)
//
        val dao= chatsshowdao(name,uidi)
//
        val collection= dao.postcollection
        val query= collection.orderBy("lasttime" , Query.Direction.DESCENDING )
        val recyclerViewoptions= FirestoreRecyclerOptions.Builder<chatOpenActdata>().setQuery(query,chatOpenActdata::class.java).build()
        adapter= chatshowadapter(recyclerViewoptions,this,name  )

        rcview.adapter=adapter


        val time= System.currentTimeMillis()

var zz= "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"
        chatsshowdao(name,uidi).addnewchat(chatOpenActdata("ro",zz,"ghh",0,time,""));



//        rcview.visibility = View.VISIBLE

adapter.startListening()


        view.addbutton.setOnClickListener {

            (activity as MainActivity).createchat()

        }

        return view
    }


    fun onChatclicked(chatid:String){

    }




}
