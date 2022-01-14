package com.prianshuprasad.buzz

import com.google.firebase.auth.FirebaseUser


data class user(
    var username:String="",
    var userid:String="",
    var photourl:String="",
    var email: String ="",
    var contact: String="",
    var deptyear:ArrayList<String> = ArrayList(),



)