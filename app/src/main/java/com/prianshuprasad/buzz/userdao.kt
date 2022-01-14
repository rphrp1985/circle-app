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

class userdao {

    val db= FirebaseFirestore.getInstance()
    val usercollection= db.collection("users")

    fun adduser(useri: user?){

        useri?.let{
            GlobalScope.launch(Dispatchers.IO) {

                usercollection.document(useri.userid).set(it)
            }

        }
    }


    fun getuserbyid(userid:String):Task<DocumentSnapshot>{



        return usercollection.document(userid).get()





    }




}