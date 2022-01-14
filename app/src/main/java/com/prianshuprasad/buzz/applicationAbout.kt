package com.prianshuprasad.buzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class applicationAbout : AppCompatActivity() {

//    private lateinit var rcview: RecyclerView
//    private lateinit var adapter:chatshowadapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_about)
//
//        rcview= findViewById(R.id.recyclerviewii)
//
//        val udi= Firebase.auth.currentUser!!.uid.toString()
//        val dao= chatsshowdao("AKCSIT-2024",udi)
//
//        rcview.layoutManager= LinearLayoutManager(this)
//        val collection= dao.postcollection
//        val query= collection.orderBy("lasttime" , Query.Direction.DESCENDING )
//        val recyclerViewoptions= FirestoreRecyclerOptions.Builder<chatOpenActdata>().setQuery(query,chatOpenActdata::class.java).build()
//        adapter= chatshowadapter(recyclerViewoptions,this, "AKCSIT-2024" )
//
//        rcview.adapter=adapter
//
//
//        val time= System.currentTimeMillis()
//var zz= "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"
//        chatsshowdao("","").addnewchat(chatOpenActdata("ro",zz,"ghh",0,time));
//
//
//adapter.startListening()
//

    }
}