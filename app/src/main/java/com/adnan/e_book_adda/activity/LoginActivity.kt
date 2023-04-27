package com.adnan.e_book_adda.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.adnan.e_book_adda.R
import com.adnan.e_book_adda.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val TAG="Test"
        var course_id=0
        val spinner: Spinner = findViewById(R.id.Spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // TODO Auto-generated method stub
                    var id = parent.getItemAtPosition(position)
                    course_id = position //this would give you the id of the selected item
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {
                    // TODO Auto-generated method stub
                }
            })
        }
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
            })
        var sharedPreferences=getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        var IsLoggedIn=sharedPreferences.getBoolean("IsLoggedIn",false)

        if(IsLoggedIn){
            intent=Intent(this@LoginActivity,
                HomeActivity::class.java)
            startActivity(intent)
            finish()

        }
        var txtForgotPassword:TextView
        var txtSignUp:TextView
        var etMobile:EditText
        var etPassword:EditText
        var btnLogin: Button

        txtForgotPassword=findViewById(R.id.txtForgotPassword)
        txtSignUp=findViewById(R.id.txtSignUp)
        etMobile=findViewById(R.id.etMobile)
        etPassword=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogin)
        if (ConnectionManager().checkConnectivity(this@LoginActivity)){
            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result"

        txtForgotPassword.setOnClickListener{
            intent=Intent(this@LoginActivity,
                ForgotPassword::class.java)
            startActivity(intent)
            finish()
        }
        txtSignUp.setOnClickListener {
           var intent2=Intent(this@LoginActivity, Register::class.java)
            startActivity(intent2)
            finish()

        }
        btnLogin.setOnClickListener{


            btnLogin.visibility=View.GONE
            var Mobile=etMobile.text.toString()
            val ProgressBar=findViewById(R.id.ProgressBar) as ProgressBar
            ProgressBar.visibility=View.VISIBLE
            var password=etPassword.text.toString()
            val intent=Intent(this@LoginActivity,
                HomeActivity::class.java)
            if((Mobile.isEmpty()) || (password.isEmpty())){
                Toast.makeText(this@LoginActivity,"Invalid Credentials",Toast.LENGTH_LONG).show()
                btnLogin.visibility=View.VISIBLE
                ProgressBar.visibility=View.GONE
            }
            else if(Mobile.length<10){
                Toast.makeText(this@LoginActivity,"Invalid Mobile Number",Toast.LENGTH_LONG).show()
                btnLogin.visibility=View.VISIBLE
                ProgressBar.visibility=View.GONE
            }
            else if(course_id==0){
                Toast.makeText(this@LoginActivity,"Please select course",Toast.LENGTH_SHORT).show()
                btnLogin.visibility=View.VISIBLE
                ProgressBar.visibility=View.GONE
            }

            else{
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", Mobile)
                jsonParams.put("password", password)
                val jsonRequest=object:JsonObjectRequest(
                    Method.POST,url,jsonParams,
                    Response.Listener{

                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            btnLogin.visibility=View.VISIBLE
                            ProgressBar.visibility=View.GONE

                            if (success) {

                                val dataFetched = data.getJSONObject("data")

                                sharedPreferences.edit()
                                    .putString("User_id", dataFetched.getString("user_id")).apply()
                                sharedPreferences.edit()
                                    .putString("Mobile", dataFetched.getString("mobile_number"))
                                    .apply()
                                sharedPreferences.edit()
                                    .putString("Name", dataFetched.getString("name")).apply()
                                sharedPreferences.edit()
                                    .putString("Address", dataFetched.getString("address")).apply()
                                sharedPreferences.edit()
                                    .putString("Email", dataFetched.getString("email")).apply()
                                sharedPreferences.edit().putInt("CourseId",course_id).apply()
                                sharedPreferences.edit().putBoolean("IsLoggedIn", true).apply()
                                startActivity(intent)
                                finish()
                            }
                            else{
                                val errorMessage:String?=data.getString("errorMessage")
                                Toast.makeText(this@LoginActivity,"$errorMessage",Toast.LENGTH_SHORT).show()
                            }

                    },
                            Response.ErrorListener{
                                Toast.makeText(this@LoginActivity,"Volley Error Occurred",Toast.LENGTH_SHORT).show()
                                btnLogin.visibility=View.VISIBLE
                                ProgressBar.visibility=View.GONE
                            }
                ){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "230e80353ecc8f"
                        return headers
                    }
                }
                queue.add(jsonRequest)}

                }

            }
        else{
            val dialog = AlertDialog.Builder(this@LoginActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@LoginActivity)
            }
            dialog.create()
            dialog.show()
        }
        }

    }