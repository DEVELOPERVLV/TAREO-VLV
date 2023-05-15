package com.example.tareo_vlv.actividades

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tareo_vlv.R
import com.google.android.material.navigation.NavigationView

class Principal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        supportActionBar?.hide()

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null){

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()

        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.nav_home -> {

             supportFragmentManager.beginTransaction()
                 .replace(R.id.fragment_container, HomeFragment()).commit()

            }
            R.id.nav_viewRegister -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UpdateTareos()).commit()

            }

            R.id.nav_sendTareos -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SendTareos()).commit()

            }

            R.id.nav_sinc -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SyncData()).commit()

            }

            R.id.nav_viewTareos -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DownLoad()).commit()

            }

            R.id.nav_setting -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, Configuration()).commit()

            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    override fun onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START)

        } else {

            onBackPressedDispatcher.onBackPressed()

        }
    }


}