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
import kotlinx.android.synthetic.main.createchatusers.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class createchatusersadapter(options: FirestoreRecyclerOptions<user>, val listener: createchatselectmembers,searchtext:String,category:String) :
    FirestoreRecyclerAdapter<user, createchatuserviewholder>(options) {

    val uid = Firebase.auth.currentUser!!.uid
    val category= category
    val searchtext= searchtext

    val selecteduid:ArrayList<String> = ArrayList()
     private lateinit var tempviewholder: createchatuserviewholder;





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): createchatuserviewholder {


        val view= LayoutInflater.from(parent.context).inflate(R.layout.createchatusers,parent,false)
        val viewholderi= createchatuserviewholder(view)




        view.setOnClickListener {

            if(selecteduid.isEmpty())
            {
                selecteduid.add((snapshots.getSnapshot(viewholderi.adapterPosition).id));
                viewholderi.statusi.text="Selected"
                tempviewholder= viewholderi
            }else
            {

                if( category=="personal" )
                {
                    selecteduid[0]=snapshots.getSnapshot(viewholderi.adapterPosition).id
                    tempviewholder.statusi.text="";
                    tempviewholder=viewholderi
                    viewholderi.statusi.text="Selected"
                }else {
                    selecteduid.add(snapshots.getSnapshot(viewholderi.adapterPosition).id)

                    viewholderi.statusi.text="Selected"
                }

            }



        }





        return viewholderi


    }



    override fun onBindViewHolder(holder: createchatuserviewholder, position: Int, model: user ) {

        if(searchtext!="") {
            val n = searchtext.length
 var substr="";

            if(n<=model.username.length) {
                 substr = model.username.subSequence(0, n).toString()
            }
            if(searchtext==substr || searchtext==model.contact){

                holder.name.text= model.username.toString()

                if(!model.deptyear.isEmpty())
                    holder.deptyeartext.text = model.deptyear.get(0);

                Glide.with(holder.photo.context).load(model.photourl).circleCrop().into(holder.photo)

            }

        }else {

            holder.name.text = model.username.toString()

            if (!model.deptyear.isEmpty())
                holder.deptyeartext.text = model.deptyear.get(0);

            Glide.with(holder.photo.context).load(model.photourl).circleCrop().into(holder.photo)
        }


    }

    interface Ichat {
        fun onClicked(chatid: String)


    }


}
//


class createchatuserviewholder(item: View): RecyclerView.ViewHolder(item){


    var photo: ImageView = item.findViewById(R.id.profilepicture)
    var name: TextView = item.findViewById(R.id.username)
    var deptyeartext : TextView= item.findViewById(R.id.deptyearview)
    var statusi:TextView= item.findViewById(R.id.status)

}
