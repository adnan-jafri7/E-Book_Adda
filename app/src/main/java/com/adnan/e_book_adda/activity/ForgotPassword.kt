package com.adnan.e_book_adda.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.adnan.e_book_adda.util.ConnectionManager
import com.adnan.e_book_adda.R
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val etMobile: EditText
        val etEmail: EditText
        val btnNext: Button
        val sharedPreferences=getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        etMobile = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)
        if (ConnectionManager().checkConnectivity(this@ForgotPassword)){
            val queue = Volley.newRequestQueue(this@ForgotPassword)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
        btnNext.setOnClickListener {
            btnNext.visibility= View.GONE
            ProgressBar.visibility= View.VISIBLE
            var Mobile = etMobile.text.toString()
            var Email = etEmail.text.toString()
            if ((Mobile.isEmpty()) || (Email.isEmpty())) {
                Toast.makeText(this@ForgotPassword, "All fields are required", Toast.LENGTH_LONG)
                    .show()
                btnNext.visibility= View.VISIBLE
                ProgressBar.visibility= View.GONE
            }
            else if(Mobile.length<10) {
                Toast.makeText(this@ForgotPassword, "Invalid Mobile Number", Toast.LENGTH_LONG)
                    .show()
                btnNext.visibility= View.VISIBLE
                ProgressBar.visibility= View.GONE
            }
            else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches())) {
                Toast.makeText(this@ForgotPassword, "Invalid Email", Toast.LENGTH_SHORT).show()
                btnNext.visibility= View.VISIBLE
                ProgressBar.visibility= View.GONE
            }
            else{
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", Mobile)
                jsonParams.put("email",Email )
                val jsonRequest=object: JsonObjectRequest(
                    Method.POST,url,jsonParams,
                    Response.Listener{
                        btnNext.visibility= View.VISIBLE
                        ProgressBar.visibility= View.GONE

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success){
                            Toast.makeText(this@ForgotPassword,"OTP Sent on Registered Email",Toast.LENGTH_SHORT).show()
                    intent= Intent(this@ForgotPassword,
                        ForgotPasswordOtp::class.java)
                    intent.putExtra("Mobile",Mobile)
                    startActivity(intent)
                    finish() }
                    else{
                            val errorMessage:String?=data.getString("errorMessage")
                            Toast.makeText(this@ForgotPassword,"$errorMessage",Toast.LENGTH_SHORT).show()}
                    },
                Response.ErrorListener{
                    btnNext.visibility= View.VISIBLE
                    ProgressBar.visibility= View.GONE
                    Toast.makeText(this@ForgotPassword, "Some Error Occurred", Toast.LENGTH_SHORT)
                        .show()}

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
            val dialog = AlertDialog.Builder(this@ForgotPassword)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@ForgotPassword)
            }
            dialog.create()
            dialog.show()
        }
        }
    }

