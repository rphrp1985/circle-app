package com.prianshuprasad.buzz


import com.firebase.ui.auth.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class collectionsdao(id:String) {

    val db= FirebaseFirestore.getInstance()
    val collection= db.collection("$id")

    fun add(useri: user?){

        useri?.let{
            GlobalScope.launch(Dispatchers.IO) {

                collection.document(useri.userid).set(it)
            }

        }
    }


    fun getbyid(userid:String):Task<DocumentSnapshot>{



        return collection.document(userid).get()





    }




}