package com.prianshuprasad.buzz

import ViewPagerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.prianshuprasad.buzz.R.menu
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.chatsview.*
import kotlinx.android.synthetic.main.chatsview.view.*
import kotlinx.android.synthetic.main.menubar.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlin.system.measureNanoTime


class MainActivity : AppCompatActivity() {

    private lateinit var navview: NavigationView
    private lateinit var drawerl: DrawerLayout
    private lateinit var actionbartoggle: ActionBarDrawerToggle
    private lateinit var tab_viewpager: ViewPager

    var speaker: Int = 0
    var Isadmin: Boolean = false
    var toolbarlist: ArrayList<String> = ArrayList()

    var chatsviewprev= chatsview("","","",false)
    var notificationviewprev= notificationview("","","",false)
    var qnaviewprev= qnaview("","","",false)
    var adminpanelprev= adminpanel("","")
    var Isauthorised=false

    val handler = Handler()


    val db = FirebaseFirestore.getInstance()
    val aboutcollections = db.collection("about")

    // nav bar check activity

    lateinit var previousSelected: MenuItem

    //user about
    var abouttext: String = "";
    var userid: String = ""

    var temp = 0;
    private lateinit var currentuser: user

    //adapter toolbar
    var adaptertoolbar: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

    var mapoflistoptions= mutableMapOf<String,ArrayList<String>>()
var mapisauthorised= mutableMapOf<String,Boolean>();
    var mapofadmin= mutableMapOf<String,Boolean>()


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        pgbar.visibility=View.GONE
        entry()
        userid = "k"

        val intentii= Intent(this,loginactivity::class.java)

        if (Firebase.auth.currentUser != null) {
            temp = 1;

            userid = Firebase.auth.currentUser!!.uid





        }else
        {
            startActivity(intentii)
            finish()
        }
            //gettig about




            GlobalScope.launch {

                if(temp==1) {
                    val userdoctemp =
                        userdao().getuserbyid(Firebase.auth.currentUser!!.uid.toString()).await()

                    if (!userdoctemp.exists()) {
                        temp = 0;

                    } else {
                        currentuser =
                            userdoctemp.toObject(user::class.java)!!
                    }

                    val temp = aboutcollections.document(userid).get().await()
                    if (temp.exists()) {

                        abouttext = temp.toObject(about::class.java)!!.about

                    } else {
                        val abouti: about = about("Hey there! I am using Circle")

                        aboutcollections.document(userid).set(abouti)

                    }
                }
                   handler.postDelayed({
                       mainactivityexec()
                   },500)

                }













    }


    // navigation view codes

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menubar, menu);
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        val idi = R.id.signoutvutton
        val intent = Intent(this, loginactivity::class.java)

        if (id == idi) {

            FirebaseAuth.getInstance().signOut();
            startActivity(intent)
            finish()


        }

        if (actionbartoggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //swipe view codes

     fun setupViewPager(viewpager: ViewPager, idname: String) {



//        var adaptertoolbara: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)


        val db = FirebaseFirestore.getInstance()
        var admincollection = db.collection("${idname}-admins")
        val collections = db.collection("$idname")

        if(mapofadmin.containsKey(idname)) {
            if (mapofadmin.getValue(idname) == true)
                Isadmin = true;
            else
                Isadmin = false;
        }
        else
            Isadmin = false;

        if(mapoflistoptions.containsKey(idname))
            toolbarlist= mapoflistoptions.getValue(idname)
        else
            speaker=1;

        navbarextension(viewpager,idname)



    }


    fun navbarextension(viewpager: ViewPager, idname: String){

 pgbar.visibility= View.GONE
//        tab_viewpager.visibility=View.GONE

        tab_viewpager.visibility= View.VISIBLE

        adaptertoolbar.deletall()
//            adaptertoolbar.


        Toast.makeText(this, "$idname", Toast.LENGTH_LONG).show()

        var n = toolbarlist.size

        if (speaker == 1 || n % 3 != 0) {
            Toast.makeText(this, "Some Error Occured", Toast.LENGTH_LONG).show()


        } else {

            var i = 0;
            while (i < n) {

                if (toolbarlist[i] == "1") {

                    notificationviewprev.idi=toolbarlist.get(i + 2)
                    notificationviewprev.name=idname
                    notificationviewprev.uidi= userid
                    notificationviewprev.Isauthorised=Isauthorised


                    adaptertoolbar.addFragment(
                        notificationviewprev,
                        toolbarlist[i + 1]
                    )
                }



                if (toolbarlist[i] == "2") {


                    chatsviewprev.idi=toolbarlist.get(i + 2)
                    chatsviewprev.name=idname
                    chatsviewprev.uidi= userid
                    chatsviewprev.Isauthorised=Isauthorised

                    adaptertoolbar.addFragment(
                        chatsviewprev,
                        toolbarlist[i + 1]
                    )


                }
                if (toolbarlist[i] == "3") {

                    qnaviewprev.idi=toolbarlist.get(i + 2)
                    qnaviewprev.name=idname
                    qnaviewprev.uidi= userid
                    qnaviewprev.Isauthorised= Isauthorised
                    adaptertoolbar.addFragment(
                        qnaviewprev,
                        toolbarlist[i + 1]
                    )
                }

                i += 3;
            }



            if (Isadmin) {


//                    getSupportFragmentManager().beginTransaction().detach(adminpanelprev).commit()
//
//
                adminpanelprev.idi=idname
                adminpanelprev.uidi= userid
//
//                    getSupportFragmentManager().beginTransaction().attach(adminpanelprev).commit()




                adaptertoolbar.addFragment(adminpanelprev, "Admin Panel")
            }


            // setting adapter to view pager.
            viewpager.setAdapter(adaptertoolbar)


        }

        tab_tablayout.setupWithViewPager(tab_viewpager)


    }



    //appopening
    fun entry() {
        pgbar.visibility= View.GONE

        applogo.visibility = View.VISIBLE
        pgappready.visibility = View.VISIBLE
        pgappready.bringToFront()
        supportActionBar?.hide()

        appbar.visibility = View.GONE

//        tab_tablayout.visibility = View.GONE
//    tab_viewpager.visibility= View.
//    navview.visibility= View.GONE

    }

    fun appready() {
        pgappready.visibility = View.GONE
        applogo.visibility = View.GONE
        supportActionBar?.show()
       pgbar.visibility= View.VISIBLE
        appbar.visibility = View.VISIBLE
//        tab_viewpager.visibility = View.GONE
    }


    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null);
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)


    }


    fun mainactivityexec(){

//        Toast.makeText(this,"hb",Toast.LENGTH_LONG).show()

        val intent = Intent(this, loginactivity::class.java)
        if (temp == 0) {
//            Toast.makeText(this,"Ple",Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        }

        appready()



        // navigatton view codes

        drawerl = findViewById(R.id.my_drawer_layout)
        actionbartoggle =
            ActionBarDrawerToggle(this, drawerl, R.string.nav_open, R.string.nav_close)

        drawerl.addDrawerListener(actionbartoggle)
        actionbartoggle.syncState()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        navview = findViewById(R.id.navview)
        val menu = navview.menu


        val size: Int = currentuser.deptyear.size

        val submenu3 = menu.addSubMenu("My Circles")

        submenu3.add("Home")

        submenu3.getItem(0).isChecked = true;
        previousSelected = submenu3.getItem(0)
        if (size > 0) {

            submenu3.add(currentuser.deptyear.get(0));

            val subMenu = menu.addSubMenu("Clubs and Socities")
            subMenu.add("Join Now");


            if (size > 1) {


                var i = 1;
                while (i < size) {
                    subMenu.add(currentuser.deptyear[i])
                    i++;
                }
            }

        }

        val subMenu1 = menu.addSubMenu("My Account")
        subMenu1.add("My Profile");
        subMenu1.add("Log out")
        val subMenu2 = menu.addSubMenu("Application")



        subMenu2.add("About")


        val navheaderview = navview.getHeaderView(0);
        navheaderview.accname.text = currentuser.username;
        Glide.with(navheaderview.acclogo.context).load(currentuser.photourl).circleCrop()
            .into(navheaderview.acclogo)
        navheaderview.emailview.text = currentuser.email

        if (abouttext == "")
            navheaderview.aboutview.text = "Set your about"
        else
            navheaderview.aboutview.text = abouttext;


        navheaderview.aboutview.visibility = View.VISIBLE
        navheaderview.editabout.setText(abouttext)
        navheaderview.editabout.visibility = View.GONE
        navheaderview.saveabout.visibility = View.GONE

        navheaderview.aboutview.setOnClickListener {

            navheaderview.aboutview.visibility = View.GONE

            navheaderview.editabout.visibility = View.VISIBLE
            navheaderview.saveabout.visibility = View.VISIBLE


        }

        navheaderview.saveabout.setOnClickListener {


            abouttext = navheaderview.editabout.text.toString()

//            navheaderview.aboutview.setTextColor(R.color.red)

            if (abouttext == "")
                navheaderview.aboutview.text = "Set your about"
            else
                navheaderview.aboutview.text = abouttext

            navheaderview.aboutview.visibility = View.VISIBLE
            navheaderview.editabout.setText(abouttext)
            navheaderview.editabout.visibility = View.GONE
            navheaderview.saveabout.visibility = View.GONE



            GlobalScope.launch {
                aboutcollections.document(userid).set(about(abouttext));

            }

//            handler.postDelayed({
////                navheaderview.aboutview.setTextColor(R.color.black)
//
//            }, 500)


        }


//swipe view codes

        tab_viewpager = findViewById<ViewPager>(R.id.tab_viewpager)







        navview.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener() {


            drawerl.closeDrawers()

            previousSelected.isChecked = false
            it.isChecked = true

            previousSelected = it;



            if (it.toString() == "Log out") {
                previousSelected.isChecked = false

                FirebaseAuth.getInstance().signOut();
                startActivity(intent)
                finish()
            } else

                if (it.toString() == "My Profile") {
                    previousSelected.isChecked = false

                    startActivity(intent)
//            finish()
                } else
                    if (it.toString() == "Join Now") {
                        previousSelected.isChecked = false


                    } else
                        if (it.toString() == "About") {
                            previousSelected.isChecked = false
                            val intentabout = Intent(this, applicationAbout::class.java)
                            startActivity(intentabout)


                        } else {

                            pgbar.visibility=View.VISIBLE
                            tab_viewpager.visibility= View.GONE



                            if(mapisauthorised.containsKey(it.toString())) {
                                if (mapisauthorised.getValue(it.toString()) == true) {
                                    Isauthorised = true;
                                } else {
                                    Isauthorised = false;
                                }
                            }else {
                                Isauthorised = false;
                            }


                                    setupViewPager(tab_viewpager, it.toString())







                        }



            return@OnNavigationItemSelectedListener false




        });



        userid= currentuser.userid
        mapisauthorised.put("Home",true);
        GlobalScope.launch {

            val size:Int= currentuser.deptyear.size;
            val db= FirebaseFirestore.getInstance();


            val collection= db.collection("Home");
            val doc= collection.document("Options").get().await()

            if(doc.exists()){
                val temptoolbarlist=doc.toObject(toolbaroption::class.java)!!
                mapoflistoptions.put("Home",temptoolbarlist.optionsmenu);
            }


            var i=0;
            while(i<size){

                val collection= db.collection(currentuser.deptyear[i]);
                val doc= collection.document("Options").get().await()

                if(doc.exists()){
                    val temptoolbarlist=doc.toObject(toolbaroption::class.java)!!
                    mapoflistoptions.put(currentuser.deptyear[i],temptoolbarlist.optionsmenu);
                }

                if(db.collection("${currentuser.deptyear[i]}-users").document("${userid}").get().await().exists())
                    mapisauthorised.put(currentuser.deptyear[i],true);
                else
                    mapisauthorised.put(currentuser.deptyear[i],false);


                if(db.collection("${currentuser.deptyear[i]}-admins").document(userid).get().await().exists())
                mapofadmin.put(currentuser.deptyear[i],true)
                else
                    mapofadmin.put(currentuser.deptyear[i],false)
i++;

            }

            handler.postDelayed({


                setupViewPager(tab_viewpager, "Home")

                tab_tablayout.setupWithViewPager(tab_viewpager)

            },500)



        }






    }


    fun onChatclicked(chatid: String){

    }

    fun createchat()
    {
        val intentcreatechat= Intent(this,createchat::class.java)
        startActivity(intentcreatechat);
    }






}