package com.adnan.e_book_adda.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adnan.e_book_adda.R
import com.adnan.e_book_adda.model.MenuList

class RestaurantMenuAdapter(var restaurantMenu: ArrayList<MenuList>, val context: Context):RecyclerView.Adapter<RestaurantMenuAdapter.RestaurantMenuViewHolder>() {
    class RestaurantMenuViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtSr: TextView =view.findViewById(R.id.txtSr) as TextView
        val txtItem:TextView=view.findViewById(R.id.txtItem) as TextView
        val txtItemPrice:TextView=view.findViewById(R.id.txtItemPrice) as TextView
        val btnAddToCart: Button =view.findViewById(R.id.btnAddToCart) as Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.activity_restaurant_menu_adapter,parent,false)
        return RestaurantMenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantMenu.size
    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, p: Int) {
        val restaurant = restaurantMenu[p]
        var s=p
        holder.txtSr.text =(++s).toString()
        holder.txtItem.text = restaurant.name
        holder.txtItemPrice.text = "Rs.${restaurant.cost_for_one}"

    }
}