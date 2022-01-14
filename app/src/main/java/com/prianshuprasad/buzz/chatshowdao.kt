package com.prianshuprasad.buzz


import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class chatsshowdao(idi:String ,uid: String) {

    val uidi= uid;
    val idii: String=idi
    val db= FirebaseFirestore.getInstance()
    val postcollection= db.collection(idii).document("chats").collection(uidi);
    val auth= Firebase.auth;

//    val newchats= newchats

    fun addnewchat(newchats:chatOpenActdata){


val time:String= System.nanoTime().toString()
        val randomid= (0..1000000000000000000).random().toString()
        val chatid= "{$time}{$uidi}{$randomid}"

        newchats.chatid=chatid

        GlobalScope.launch {

            postcollection.document(chatid).set(newchats);
        }



    }


    fun getchatsviewById(postId: String): Task<DocumentSnapshot> {
        return postcollection.document(postId).get()
    }


    fun updatechatsview(newchatss: chatOpenActdata,chatid: String){
        GlobalScope.launch {
            postcollection.document(chatid).set(newchatss)
        }
    }

fun deletechatsview(chatid:String){
    GlobalScope.launch {

        postcollection.document(chatid).delete()
    }
}

    fun check(chatviewid:String){

        GlobalScope.launch {
            val db= FirebaseFirestore.getInstance()
            val collection= db.collection("chatdata");


            val doc = collection.document(chatviewid).get().await()

            if(doc.exists()){
                val tempchatactdata= doc.toObject(chatOpenActdata::class.java)!!
                postcollection.document(chatviewid).set(tempchatactdata)
//

            }
            else
            {

//                postcollection.document(chatviewid).delete()
            }


        }


    }



}