package com.prianshuprasad.buzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.CalendarContract
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_createchatselectmembers.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.StringReader

class createchatselectmembers : AppCompatActivity() {


    private lateinit var adapter: createchatusersadapter;
    private lateinit var rcview:RecyclerView
            val db= FirebaseFirestore.getInstance();
    lateinit var spinner: Spinner;
    lateinit var deptyearlistarray: ArrayList<String>;
    val handler= Handler()
    var seachtext:String=""
var category:String="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createchatselectmembers)
        supportActionBar?.hide();

        var selected:String="All"
         category = intent.getStringExtra("category").toString()

        Toast.makeText(this,"$category",Toast.LENGTH_LONG).show()


        searchbutton.setOnClickListener {

          seachtext= inputsearch.text.toString()
          setuprcview(selected)

        }




       rcview= findViewById(R.id.recyclerviewcreatechat)

        rcview.layoutManager= LinearLayoutManager(this)


        spinner = findViewById(com.prianshuprasad.buzz.R.id.deptyearlist)


        GlobalScope.launch {

            val tempcollection= db.collection("Department-Year");

            val docitemp= tempcollection.document("Department-Year").get().await()
            if(docitemp.exists())
                deptyearlistarray = docitemp.toObject(listclass::class.java)!!.list


            deptyearlistarray[0]= "All"

         handler.postDelayed({

             spinnersetupextension()

         },500)

        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if(position>=0){
                    selected= deptyearlistarray[position]
                    setuprcview(selected)

                }



            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }



       setuprcview(selected)




    }



    fun setuprcview(selected:String){

        var collection= db.collection("users")
        if(selected!="All")
        {
            collection= db.collection("$selected-users")
        }

    val query= collection.orderBy("username", Query.Direction.DESCENDING)
        val recyclerViewoptions= FirestoreRecyclerOptions.Builder<user>().setQuery(query,user::class.java).build()
        adapter= createchatusersadapter(recyclerViewoptions,this,seachtext,category)

        rcview.adapter= adapter

        adapter.startListening()




    }



    fun spinnersetupextension(){

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            deptyearlistarray.toTypedArray()

        )

        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinner.adapter = ad



    }


    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null);
    }






}