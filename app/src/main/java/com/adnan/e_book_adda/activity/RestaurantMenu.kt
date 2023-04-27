package com.adnan.e_book_adda.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adnan.e_book_adda.R
import com.adnan.e_book_adda.adapter.RestaurantMenuAdapter
import com.adnan.e_book_adda.model.MenuList
import com.adnan.e_book_adda.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class RestaurantMenu : AppCompatActivity() {
    lateinit var recyclerRestaurants: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: RestaurantMenuAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var RestaurantMenuList = arrayListOf<MenuList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)
        recyclerRestaurants = findViewById(R.id.recyclerRestaurants)
        progressLayout=findViewById(R.id.rlLoading)
        progressBar=findViewById(R.id.progressBar)
        progressLayout.visibility= View.VISIBLE
        layoutManager = LinearLayoutManager(this)
        val queue= Volley.newRequestQueue(this)
        val id= intent.getStringExtra("id")
        val url="http://13.235.250.119/v2/restaurants/fetch_result/$id"
        if (ConnectionManager().checkConnectivity(this@RestaurantMenu)) {
            val jsonRequest=object: JsonObjectRequest(
                Method.GET,url,null,
                Response.Listener{
                    progressLayout.visibility=View.GONE
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    Toast.makeText(this@RestaurantMenu,"$it, $id",Toast.LENGTH_SHORT).show()

                        if(success){
                            val data=data.getJSONArray("data")
                            for(i in 0 until data.length()){
                                val JsonObject=data.getJSONObject(i)
                                val RestaurantObject= MenuList(
                                    JsonObject.getString("id"),
                                    JsonObject.getString("name"),
                                    JsonObject.getString("cost_for_one"),
                                    JsonObject.getString("restaurant_id")
                                )
                                RestaurantMenuList.add(RestaurantObject)
                                recyclerAdapter = RestaurantMenuAdapter(RestaurantMenuList,this)
                                recyclerRestaurants.adapter = recyclerAdapter
                                recyclerRestaurants.layoutManager = layoutManager

        }
        }
                    else{
                            val errorMessage=data.getString("errorMessage")
                            Toast.makeText(this@RestaurantMenu,"$errorMessage",Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                    Toast.makeText(this@RestaurantMenu,"Some error occurred",Toast.LENGTH_SHORT).show()
                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "230e80353ecc8f"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }

    }


    }

