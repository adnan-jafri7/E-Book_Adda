package com.adnan.e_book_adda.fragment


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import androidx.appcompat.widget.SearchView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.adnan.e_book_adda.R
import com.adnan.e_book_adda.adapter.AllRestaurantsAdapter
import com.adnan.e_book_adda.model.Restaurants
import com.adnan.e_book_adda.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {


    private lateinit var recyclerRestaurant: RecyclerView
    private lateinit var allRestaurantsAdapter: AllRestaurantsAdapter
    private var restaurantList = arrayListOf<Restaurants>()
    private lateinit var progressBar: ProgressBar
    private lateinit var rlLoading: RelativeLayout
    lateinit var searchView: EditText
    lateinit var filteredList: ArrayList<Restaurants>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = view?.findViewById(R.id.progressBar) as ProgressBar
        rlLoading = view.findViewById(R.id.rlLoading) as RelativeLayout
        searchView=view.findViewById(R.id.etSearchView)

        rlLoading.visibility = View.VISIBLE

        setUpRecycler(view)

        return view
    }


    private fun setUpRecycler(view: View) {
        recyclerRestaurant = view.findViewById(R.id.recyclerRestaurants) as RecyclerView

        /*Create a queue for sending the request*/
        val queue = Volley.newRequestQueue(activity as Context)
        val sharedPreferences: SharedPreferences=(activity as FragmentActivity).getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        val course_id=sharedPreferences.getInt("CourseId",0)


        /*Check if the internet is present or not*/
        if (ConnectionManager().checkConnectivity(activity as Context)) {

            /*Create a JSON object request*/
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                "https://bookadda-f7354.firebaseio.com/.json",
                null,
                Response.Listener<JSONObject> { response ->
                    rlLoading.visibility = View.GONE

                    when(course_id) {
                        1->{

                        /*Once response is obtained, parse the JSON accordingly*/
                        try {
                            val data = response.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {

                                val resArray = data.getJSONArray("data")
                                for (i in 0 until resArray.length()) {
                                    val resObject = resArray.getJSONObject(i)
                                    val restaurant = Restaurants(
                                        resObject.getString("id").toInt(),
                                        resObject.getString("name"),
                                        resObject.getString("rating"),
                                        resObject.getString("cost_for_one"),
                                        resObject.getString("image_url"),
                                        resObject.getString("pdf_url")
                                    )
                                    restaurantList.add(restaurant)
                                    if (activity != null) {
                                        allRestaurantsAdapter =
                                            AllRestaurantsAdapter(
                                                restaurantList,
                                                activity as Context
                                            )
                                        val mLayoutManager = LinearLayoutManager(activity)
                                        recyclerRestaurant.layoutManager = mLayoutManager
                                        recyclerRestaurant.itemAnimator = DefaultItemAnimator()
                                        recyclerRestaurant.adapter = allRestaurantsAdapter
                                        recyclerRestaurant.setHasFixedSize(true)


                                    }


                                }

                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                        2->{
                            try {
                                val data = response.getJSONObject("data2")
                                val success = data.getBoolean("success")
                                if (success) {

                                    val resArray = data.getJSONArray("data2")
                                    for (i in 0 until resArray.length()) {
                                        val resObject = resArray.getJSONObject(i)
                                        val restaurant = Restaurants(
                                            resObject.getString("id").toInt(),
                                            resObject.getString("name"),
                                            resObject.getString("rating"),
                                            resObject.getString("cost_for_one"),
                                            resObject.getString("image_url"),
                                            resObject.getString("pdf_url")
                                        )
                                        restaurantList.add(restaurant)
                                        if (activity != null) {
                                            allRestaurantsAdapter =
                                                AllRestaurantsAdapter(
                                                    restaurantList,
                                                    activity as Context
                                                )
                                            val mLayoutManager = LinearLayoutManager(activity)
                                            recyclerRestaurant.layoutManager = mLayoutManager
                                            recyclerRestaurant.itemAnimator = DefaultItemAnimator()
                                            recyclerRestaurant.adapter = allRestaurantsAdapter
                                            recyclerRestaurant.setHasFixedSize(true)
                                        }


                                    }

                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        3->{
                            try {
                                val data = response.getJSONObject("data3")
                                val success = data.getBoolean("success")
                                if (success) {

                                    val resArray = data.getJSONArray("data3")
                                    for (i in 0 until resArray.length()) {
                                        val resObject = resArray.getJSONObject(i)
                                        val restaurant = Restaurants(
                                            resObject.getString("id").toInt(),
                                            resObject.getString("name"),
                                            resObject.getString("rating"),
                                            resObject.getString("cost_for_one"),
                                            resObject.getString("image_url"),
                                            resObject.getString("pdf_url")
                                        )
                                        restaurantList.add(restaurant)
                                        if (activity != null) {
                                            allRestaurantsAdapter =
                                                AllRestaurantsAdapter(
                                                    restaurantList,
                                                    activity as Context
                                                )
                                            val mLayoutManager = LinearLayoutManager(activity)
                                            recyclerRestaurant.layoutManager = mLayoutManager
                                            recyclerRestaurant.itemAnimator = DefaultItemAnimator()
                                            recyclerRestaurant.adapter = allRestaurantsAdapter
                                            recyclerRestaurant.setHasFixedSize(true)
                                        }


                                    }

                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        4->{
                            try {
                                val data = response.getJSONObject("data4")
                                val success = data.getBoolean("success")
                                if (success) {

                                    val resArray = data.getJSONArray("data4")
                                    for (i in 0 until resArray.length()) {
                                        val resObject = resArray.getJSONObject(i)
                                        val restaurant = Restaurants(
                                            resObject.getString("id").toInt(),
                                            resObject.getString("name"),
                                            resObject.getString("rating"),
                                            resObject.getString("cost_for_one"),
                                            resObject.getString("image_url"),
                                            resObject.getString("pdf_url")
                                        )
                                        restaurantList.add(restaurant)
                                        if (activity != null) {
                                            allRestaurantsAdapter =
                                                AllRestaurantsAdapter(
                                                    restaurantList,
                                                    activity as Context
                                                )
                                            val mLayoutManager = LinearLayoutManager(activity)
                                            recyclerRestaurant.layoutManager = mLayoutManager
                                            recyclerRestaurant.itemAnimator = DefaultItemAnimator()
                                            recyclerRestaurant.adapter = allRestaurantsAdapter
                                            recyclerRestaurant.setHasFixedSize(true)
                                        }


                                    }

                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        5->{
                            try {
                                val data = response.getJSONObject("data5")
                                val success = data.getBoolean("success")
                                if (success) {

                                    val resArray = data.getJSONArray("data5")
                                    for (i in 0 until resArray.length()) {
                                        val resObject = resArray.getJSONObject(i)
                                        val restaurant = Restaurants(
                                            resObject.getString("id").toInt(),
                                            resObject.getString("name"),
                                            resObject.getString("rating"),
                                            resObject.getString("cost_for_one"),
                                            resObject.getString("image_url"),
                                            resObject.getString("pdf_url")
                                        )
                                        restaurantList.add(restaurant)
                                        if (activity != null) {
                                            allRestaurantsAdapter =
                                                AllRestaurantsAdapter(
                                                    restaurantList,
                                                    activity as Context
                                                )
                                            val mLayoutManager = LinearLayoutManager(activity)
                                            recyclerRestaurant.layoutManager = mLayoutManager
                                            recyclerRestaurant.itemAnimator = DefaultItemAnimator()
                                            recyclerRestaurant.adapter = allRestaurantsAdapter
                                            recyclerRestaurant.setHasFixedSize(true)
                                        }


                                    }

                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }},
                Response.ErrorListener { error: VolleyError? ->
                    Toast.makeText(activity as Context, error?.message, Toast.LENGTH_SHORT).show()
                }) {

                /*Send the headers using the below method*/
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"

                    /*The below used token will not work, kindly use the token provided to you in the training*/
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        } else {
            val builder = AlertDialog.Builder(activity as Context)
            builder.setTitle("Error")
            builder.setMessage("No Internet Connection found. Please connect to the internet and re-open the app.")
            builder.setCancelable(false)
            builder.setPositiveButton("Ok") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            builder.create()
            builder.show()
        }

    }


}
