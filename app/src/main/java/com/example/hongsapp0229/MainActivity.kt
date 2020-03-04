package com.example.hongsapp0229

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //create 내가 이거 만들겠다.
        setContentView(R.layout.activity_main)//practice.xml을 보여주겠다.


        firestore = FirebaseFirestore.getInstance()

        main_bn.setOnNavigationItemSelectedListener(this)
        // main_bn.disableShiftMode()
        main_bn.selectedItemId = R.id.action_home

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                //val args = Bundle()
                //args.putString("membership", membership)
                val homeFragment = HomeFragment()
                //studyFragment!!.arguments = args
                supportFragmentManager.beginTransaction().replace(R.id.main_fl, homeFragment)
                    .commit()

                return true
            }
            R.id.action_find -> {
                val findFragment = FindFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_fl, findFragment)
                    .commit()

                return true
            }

            R.id.action_chat -> {

                val chatFragment = ChatFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_fl, chatFragment)
                    .commit()

                return true
            }
            R.id.action_myaccount -> {

                val myaccountFragment = MyaccountFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_fl, myaccountFragment)
                    .commit()

                return true
            }


        }
        return false

    }
}
