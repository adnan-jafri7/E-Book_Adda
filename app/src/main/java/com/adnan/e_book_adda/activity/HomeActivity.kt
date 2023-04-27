package com.adnan.e_book_adda.activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.adnan.e_book_adda.R
import com.adnan.e_book_adda.fragment.FAQFragment
import com.adnan.e_book_adda.fragment.FavouritesFragment
import com.adnan.e_book_adda.fragment.HomeFragment
import com.adnan.e_book_adda.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView



class HomeActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var previousMenuItem: MenuItem? = null
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        var sharedPreferences=getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        val name=sharedPreferences.getString("Name","Name")
        val email=sharedPreferences.getString("Email","Email")

        init()
        val headerView : View = navigationView.getHeaderView(0)
        val txtDrawerText=headerView.findViewById(R.id.txtDrawerText) as TextView
        val txtDrawerSecondaryText=headerView.findViewById(R.id.txtDrawerSecondaryText) as TextView
        txtDrawerText.text=name.toString()
        txtDrawerSecondaryText.text=email.toString()


        setupToolbar()
        setupActionBarToggle()
        displayHome()

        navigationView.setNavigationItemSelectedListener { item: MenuItem ->

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            item.isCheckable = true
            item.isChecked = true

            previousMenuItem = item

            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {

                R.id.home -> {
                    val homeFragment = HomeFragment()
                    fragmentTransaction.replace(R.id.frame, homeFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "All Books"
                }

                /*Opening the profile fragment*/
                R.id.myProfile -> {
                    val profileFragment = ProfileFragment()
                    fragmentTransaction.replace(R.id.frame, profileFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "My profile"
                }

                /*Opening the favorites fragment*/
                R.id.favRes -> {
                    val favFragment = FavouritesFragment()
                    fragmentTransaction.replace(R.id.frame, favFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Favorite Books"
                }

                /*Opening the frequently asked questions i.e. FAQ fragment*/
                R.id.faqs -> {
                    val faqFragment = FAQFragment()
                    fragmentTransaction.replace(R.id.frame, faqFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                }

                /*Exiting the application*/
                R.id.logout -> {

                    /*Creating a confirmation dialog*/
                    val builder = AlertDialog.Builder(this@HomeActivity)
                    builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            val sharedPreferences= getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.clear()
                            editor.apply()
                            ActivityCompat.finishAffinity(this)
                        }
                        .setNegativeButton("No") { _, _ ->
                            displayHome()
                        }
                        .create()
                        .show()

                }

            }
            return@setNavigationItemSelectedListener true
        }
    }


    /*This is method is created to display the home fragment*/
    private fun displayHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "All Books"
        navigationView.setCheckedItem(R.id.home)
    }


    private fun setupActionBarToggle() {
        actionBarDrawerToggle =
            object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.openDrawer,
                R.string.closeDrawer
            ) {
                override fun onDrawerStateChanged(newState: Int) {
                    super.onDrawerStateChanged(newState)
                    val pendingRunnable = Runnable {
                        val inputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    }
                    Handler().postDelayed(pendingRunnable, 50)
                }
            }

        /*Adding the drawer toggle to the drawer layout*/
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        /*This handles the animation of the hamburger icon when the drawer is opened/closed*/
        actionBarDrawerToggle.syncState()

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        /*This is done to open the navigation drawer when the hamburger icon is clicked*/
        if (item?.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}
