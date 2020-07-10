package com.example.ncscommunity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nib_row.view.*

class Mainadapter  : RecyclerView.Adapter <CustomViewHolder>() {

    val nib_name = listOf("Aditya verma","Vaibhav Shukla","Ojuswi Rastogi")
    val club_name = listOf("Programming Club","Designing Club","Programming Club")
    val image_url = listOf("https://ojuswi.pythonanywhere.com/media/profile_pictures/aditya.jpg","https://ojuswi.pythonanywhere.com/media/profile_pictures/vaibhav.jpg","https://ojuswi.pythonanywhere.com/media/profile_pictures/ojuswi.jpg")
    val num       = listOf("9528978431","9354545801","8528522733")
    override fun getItemCount(): Int {
        return 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        //how do we create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.nib_row,parent,false)
        return CustomViewHolder(cellForRow)

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // parsing data into the view
        val name =nib_name.get(position)
        val club =club_name.get(position)
        val image=image_url.get(position)
        val number=num.get(position)

        holder?.view?.nib_name?.text = name
        holder?.view?.nib_club?.text = club
        Picasso.get().load(image).into(holder.view.nib_image)
    }
}
class CustomViewHolder (val view : View) : RecyclerView.ViewHolder (view) {
}