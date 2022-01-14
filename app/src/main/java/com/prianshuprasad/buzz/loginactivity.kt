package com.prianshuprasad.buzz

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.common.io.Files
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_loginactivity.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import android.webkit.MimeTypeMap

import android.content.ContentResolver
import com.bumptech.glide.load.model.Model
import com.google.firebase.storage.OnProgressListener


class loginactivity : AppCompatActivity() {

    var temp=1;
   lateinit var userdata: user
    var RC_SIGN_IN: Int=123
    var TAG="1"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
   private lateinit var olduser: user
    var deptyearlistarray:ArrayList<String> = arrayListOf("rp")
    var selecteddeptyear:String="";
    var imgurl:String="j"
   lateinit var spinner: Spinner;
    val handler= Handler()

    //image upload

    private val reference: StorageReference = FirebaseStorage.getInstance().getReference()
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity )
   supportActionBar?.hide()
pgbar.visibility= View.GONE


spinner = findViewById(com.prianshuprasad.buzz.R.id.deptyearlist)

        val db= FirebaseFirestore.getInstance()
        val collection= db.collection("Department-Year")



      //image upload

        profilepicview.setOnClickListener(View.OnClickListener {
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        })

//        uploadBtn.setOnClickListener(View.OnClickListener {
//            if (imageUri != null) {
//                uploadToFirebase(imageUri)
//            } else {
//                Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show()
//            }
//        })




cameraicon.visibility= View.GONE
//spinner



        GlobalScope.launch {

            val docitemp= collection.document("Department-Year").get().await()
            if(docitemp.exists())
            deptyearlistarray = docitemp.toObject(listclass::class.java)!!.list

            handler.postDelayed({

             spinnersetupextension()
            },1000)



        }




//spinner selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if(position>0){
                    selecteddeptyear= deptyearlistarray[position]

                }



            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }




                val uid:String= Firebase.auth.currentUser?.uid.toString()
        if(Firebase.auth.currentUser!=null)
        {

            loginview()
            cameraicon.visibility= View.GONE
            text.visibility= View.GONE;
            logo.visibility= View.GONE
            gloginbutton.visibility= View.GONE
            pgbar.visibility= View.VISIBLE

            GlobalScope.launch {

                var userdoctemp=userdao().getuserbyid(uid).await()
                if(userdoctemp.exists()) {
                    olduser = userdoctemp.toObject(user::class.java)!!


                }else
                    olduser=  user(
                        Firebase.auth.currentUser?.displayName.toString(),
                        Firebase.auth.currentUser?.uid.toString(),
                        Firebase.auth.currentUser?.photoUrl.toString(),
                        Firebase.auth.currentUser?.email.toString(),
                        "",
                        ArrayList()

                    )

                handler.postDelayed({

  takeforminputextesion()
                },500)


            }

        }else
            loginview();




        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        auth= Firebase.auth




        gloginbutton.setOnClickListener{
            signIn()
        }



    }

    private fun signIn() {

        gloginbutton.setVisibility(View.GONE)
        text.visibility= View.GONE
        logo.visibility= View.GONE
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Toast.makeText(this,"$RC_SIGN_IN",Toast.LENGTH_LONG).show()

        if(requestCode==2&& resultCode == RESULT_OK && data != null){
            imageUri = data.data
            profilepicview.setImageURI(imageUri)

        }


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handlesignin(task)
        }
    }

    private fun handlesignin(task: Task<GoogleSignInAccount>?) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task?.getResult(ApiException::class.java)!!
//            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle( account.idToken!! )
        } catch (e: ApiException) {
            loginview()
            Toast.makeText(this, "Login Failed! Please Try Again", Toast.LENGTH_LONG).show()
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)

        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
            pgbar.visibility= View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth= auth.signInWithCredential(credential).await()
            val firebaseUser= auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {


   var templist   = listOf<String>("h")
        gloginbutton.setVisibility(View.GONE)
        text.visibility=View.GONE
        logo.visibility= View.GONE

        val email=Firebase.auth.currentUser?.email.toString()
        val userdaoie:userdao= userdao();
        if(firebaseUser!=null)
        {

            userdata= user(
                firebaseUser?.displayName.toString(),
                firebaseUser?.uid.toString(),
                firebaseUser?.photoUrl.toString(),
                email,
                "",
                ArrayList()
            )



//            userdaoie.adduser(userdata);
            GlobalScope.launch {

                val x: DocumentSnapshot = userdaoie.getuserbyid(userdata.userid).await();
                if(x.exists())
                {
                   temp=0;

                }

                handler.postDelayed({
                                    takeforminputextension2()
                },500)


            }




        }
        else
        {

            loginview()
        }

    }




    //ui
    fun takeforminput(usertemp:user){

        gloginbutton.visibility= View.GONE;
        text.visibility= View.GONE;
        logo.visibility= View.GONE
       cameraicon.visibility= View.VISIBLE
   cameraicon.bringToFront()
        profilepicview.visibility = View.VISIBLE
        usernameview.visibility= View.VISIBLE
        emailview.visibility= View.VISIBLE
        text1.visibility= View.VISIBLE;
        contactnumberget.visibility= View.VISIBLE
        text2.visibility= View.VISIBLE;
        deptyearlist.visibility= View.VISIBLE
        savedetails.visibility=View.VISIBLE

       olduser=usertemp
            usernameview.text = "Username : ${olduser.username}";
            emailview.text = "Email : ${olduser.email}"
            contactnumberget.setText(olduser?.contact.toString())
            Glide.with(profilepicview.context).load(olduser.photourl).circleCrop()
                .into(profilepicview)






        }







    fun loginview(){

        gloginbutton.visibility= View.VISIBLE
        text.visibility= View.VISIBLE
 logo.visibility= View.VISIBLE

        cameraicon.visibility= View.GONE
        profilepicview.visibility= View.GONE
        usernameview.visibility= View.GONE
        emailview.visibility= View.GONE
        text1.visibility= View.GONE
        contactnumberget.visibility= View.GONE
        text2.visibility= View.GONE
        deptyearlist.visibility= View.GONE
        savedetails.visibility=View.GONE


    }




    fun savedata(view: android.view.View) {



        if (imageUri != null){
            uploadToFirebase(imageUri!!);
            return;
        }



       val temparraylist= olduser.deptyear;
       val userdaoyu= userdao()
        olduser.contact= contactnumberget.text.toString()
        if(selecteddeptyear!="") {

            checkandrequestmycircle(olduser.userid,selecteddeptyear)

            if (olduser.deptyear.isEmpty()) {
                olduser.deptyear.add(selecteddeptyear)
            } else
                olduser.deptyear[0] = selecteddeptyear
        }else
            olduser.deptyear.clear()

        if(olduser.contact.isEmpty())
        {

            Toast.makeText(this,"Please enter your contact number",Toast.LENGTH_LONG).show()
            return ;
        }
        if(olduser.contact.toString().length<10  ){

            Toast.makeText(this,"Please enter correct contact number",Toast.LENGTH_LONG).show()
            return ;
        }

  val intent= Intent(this,MainActivity::class.java)



            if (Firebase.auth.currentUser != null) {

                userdaoyu.adduser(olduser)

                startActivity(intent)
                finish()


            } else

                loginview()


    }





    // image upload

    private fun uploadToFirebase(uri: Uri) {
        val fileRef: StorageReference =
            reference.child(System.currentTimeMillis().toString() + "." + Files.getFileExtension(uri.toString()))
        fileRef.putFile(uri)
            .addOnSuccessListener( object : OnSuccessListener<UploadTask.TaskSnapshot?> {
               override fun onSuccess(p0: UploadTask.TaskSnapshot) {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
//                        val model = Model( uri.toString() )
//                        val modelId: String = root.push().getKey()
//                        root.child(modelId).setValue(model)

                          imgurl= uri.toString()
                       onimageuploadsavedata()
                        pgbar.visibility=View.GONE
                        Toast.makeText(this@loginactivity, "Profile Picture Updated Successfully", Toast.LENGTH_SHORT).show()
//                        profilepicview.setImageResource(android.R.drawable.ic_baseline_add_photo_alternate_24)
                    }
                }
            }).addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
              override  fun onProgress(snapshot: UploadTask.TaskSnapshot) {

                  loginview()
                  pgbar.visibility= View.VISIBLE
                  gloginbutton.visibility= View.GONE
                  text.visibility= View.GONE
                  logo.visibility= View.GONE


                }
            }).addOnFailureListener {

                Toast.makeText(this@loginactivity, "Uploading Failed !!", Toast.LENGTH_SHORT).show()
            }
    }


    private fun getFileExtension(mUri: Uri): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }




    //on image upload

    fun onimageuploadsavedata(){

        olduser.photourl= imgurl
        val temparraylist= olduser.deptyear;
        val userdaoyu= userdao()
        olduser.contact= contactnumberget.text.toString()
        if(selecteddeptyear!="") {

            checkandrequestmycircle(olduser.userid,selecteddeptyear)

            if (olduser.deptyear.isEmpty()) {
                olduser.deptyear.add(selecteddeptyear)
            } else
                olduser.deptyear[0] = selecteddeptyear
        }else
            olduser.deptyear.clear()

        if(olduser.contact.isEmpty())
        {

            Toast.makeText(this,"Please enter your contact number",Toast.LENGTH_LONG).show()
            return ;
        }
        if(olduser.contact.toString().length<10  ){

            Toast.makeText(this,"Please enter correct contact number",Toast.LENGTH_LONG).show()
            return ;
        }

        val intent= Intent(this,MainActivity::class.java)



        if (Firebase.auth.currentUser != null) {

            userdaoyu.adduser(olduser)

            startActivity(intent)
            finish()


        } else

            loginview()






    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null);
    }



    fun checkandrequestmycircle(userid:String,request: String){

        val db = FirebaseFirestore.getInstance()
        GlobalScope.launch {

            if(!(db.collection("${request}-users").document(userid).get().await().exists() ) )
            {
                db.collection("${request}-users-request").document(userid).set(olduser)
             }


        }




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


    fun takeforminputextesion(){
        takeforminput(olduser);
        pgbar.visibility= View.GONE
    }

    fun takeforminputextension2(){
        if(temp==1){

            takeforminput(userdata)
            pgbar.setVisibility(View.GONE)
        }else
        {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()


        }


    }









}