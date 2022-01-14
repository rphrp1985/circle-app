package com.prianshuprasad.buzz


import android.content.Intent
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.Utils
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class chatshowadapter(options: FirestoreRecyclerOptions<chatOpenActdata>, val listener: chatsview, val idi: String) :
    FirestoreRecyclerAdapter<chatOpenActdata, chatsviewholder>(options) {

    val uid = Firebase.auth.currentUser!!.uid

    val idii= idi;
var tempchatid:String=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatsviewholder {


        val view= LayoutInflater.from(parent.context).inflate(R.layout.chatshowact,parent,false)
        val viewholderi= chatsviewholder(view)




   view.setOnClickListener {

       listener.onChatclicked(snapshots.getSnapshot(viewholderi.adapterPosition).id)

   }





        return viewholderi


    }



    override fun onBindViewHolder(holder: chatsviewholder, position: Int, model: chatOpenActdata) {

        holder.name.text= model.chatname.toString()
        holder.extra.text= model.extra.toString()

        chatsshowdao(idii,uid).check(model.chatid)

        Glide.with(holder.photo.context).load(model.chatlogourl).circleCrop().into(holder.photo)


    }

    interface Ichat {
        fun onClicked(chatid: String)


    }
}
//


class chatsviewholder(item: View): RecyclerView.ViewHolder(item){


    var photo: ImageView = item.findViewById(R.id.chatlogopic)
    var name: TextView = item.findViewById(R.id.chatname)
    var extra: TextView= item.findViewById(R.id.chatextra)

}
