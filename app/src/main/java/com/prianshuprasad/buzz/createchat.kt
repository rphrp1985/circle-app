package com.prianshuprasad.buzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class createchat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createchat)

        val textView :TextView= findViewById(R.id.createpersonal)
    }



    fun createpersonal(view: android.view.View) {

        val intentcreatechatselect= Intent(this,createchatselectmembers::class.java)

        intentcreatechatselect.putExtra("category","personal");

        startActivity(intentcreatechatselect)




    }


}