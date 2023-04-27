package com.adnan.e_book_adda.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.adnan.e_book_adda.R
import com.adnan.e_book_adda.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject


class Register : AppCompatActivity() {
    @IgnoreExtraProperties
    data class User(
        var username: String? = "",
        var email: String? = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register Yourself"
        val etName: EditText
        val etMobile: EditText
        val etEmail: EditText
        val etAddress: EditText
        val etPassword: EditText
        val etConfirmPassword: EditText
        val spinner: Spinner = findViewById(R.id.Spinner)
        val sharedPreferences: SharedPreferences
        lateinit var database: DatabaseReference
        database = Firebase.database.reference
        var course_id=0



        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        etName = findViewById(R.id.etName)
        etMobile = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
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
        if (ConnectionManager().checkConnectivity(this@Register)) {
            val queue = Volley.newRequestQueue(this@Register)
            val url = "http://13.235.250.119/v2/register/fetch_result"
            btnRegister.setOnClickListener {
                btnRegister.visibility=View.GONE
                ProgressBar.visibility=View.VISIBLE
                var Name = etName.text.toString()
                var Mobile = etMobile.text.toString()
                var Email = etEmail.text.toString()
                var Address = etAddress.text.toString()
                var Password = etPassword.text.toString()
                var ConfirmPassword = etConfirmPassword.text.toString()

                if ((Name.isEmpty()) || (Mobile.isEmpty()) || (Email.isEmpty()) || (Address.isEmpty()) || (Password.isEmpty()) || (ConfirmPassword.isEmpty())) {
                    Toast.makeText(this@Register, "All fields are required", Toast.LENGTH_SHORT)
                        .show()
                    btnRegister.visibility=View.VISIBLE
                    ProgressBar.visibility=View.GONE

                } else if (Name.length < 3) {
                    Toast.makeText(
                        this@Register,
                        "Name must contain minimum 3 Characters",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnRegister.visibility=View.VISIBLE
                    ProgressBar.visibility=View.GONE
                } else if (Mobile.length != 10) {
                    Toast.makeText(this@Register, "Invalid Mobile Number", Toast.LENGTH_SHORT)
                        .show()
                    btnRegister.visibility=View.VISIBLE
                    ProgressBar.visibility=View.GONE
                } else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches())) {
                    Toast.makeText(this@Register, "Invalid Email", Toast.LENGTH_SHORT).show()
                    btnRegister.visibility=View.VISIBLE
                    ProgressBar.visibility=View.GONE
                } else if (Password.length < 6 || Password.length > 15) {
                    Toast.makeText(
                        this@Register,
                        "Password must be of 6-15 Characters",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnRegister.visibility=View.VISIBLE
                    ProgressBar.visibility=View.GONE
                } else if (Password != ConfirmPassword) {
                    Toast.makeText(
                        this@Register,
                        "Password and Confirm Password does not match",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnRegister.visibility=View.VISIBLE
                    ProgressBar.visibility=View.GONE
                }
                else if(course_id==0){
                    Toast.makeText(this@Register,"Please select course",Toast.LENGTH_SHORT).show()
                    btnRegister.visibility=View.VISIBLE
                    ProgressBar.visibility=View.GONE
                }
                else {
                val jsonParams = JSONObject()
                jsonParams.put("name", Name)
                jsonParams.put("mobile_number", Mobile)
                jsonParams.put("password", Password)
                jsonParams.put("address", Address)
                jsonParams.put("email", Email)
                val jsonRequest = object : JsonObjectRequest(
                    Method.POST, url, jsonParams,
                    Response.Listener {
                        btnRegister.visibility=View.VISIBLE
                        ProgressBar.visibility=View.GONE
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                Toast.makeText(this@Register,"$it Success",Toast.LENGTH_LONG).show()

                                val dataFetched=data.getJSONObject("data")

                                sharedPreferences.edit().putString("User_id", dataFetched.getString("user_id")).apply()
                                sharedPreferences.edit().putString("Mobile", dataFetched.getString("mobile_number")).apply()
                                sharedPreferences.edit().putString("Name", dataFetched.getString("name")).apply()
                                sharedPreferences.edit().putString("Address", dataFetched.getString("address")).apply()
                                sharedPreferences.edit().putString("Email", dataFetched.getString("email")).apply()
                                sharedPreferences.edit().putInt("CourseId",course_id).apply()
                                sharedPreferences.edit().putBoolean("IsLoggedIn",true).apply()
                                Toast.makeText(
                                    this@Register,
                                    "Registration Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent = Intent(this@Register, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                val errorMessage=data.getString("errorMessage")
                                Toast.makeText(this@Register,"$errorMessage",Toast.LENGTH_SHORT).show()
                            }

                        } catch (e1: JSONException) {
                            Toast.makeText(this@Register, "$e1", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this@Register, "Some Error Occurred", Toast.LENGTH_SHORT)
                            .show()

                    }) {
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
                else {
                    val dialog = AlertDialog.Builder(this@Register)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection not Found")
                    dialog.setPositiveButton("Open Settings") { text, listener ->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Cancel") { text, listener ->
                        ActivityCompat.finishAffinity(this@Register)
                    }
                    dialog.create()
                    dialog.show()
                }

            }


        }





