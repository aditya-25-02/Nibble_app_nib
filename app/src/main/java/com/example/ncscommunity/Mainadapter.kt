package com.example.ncscommunity

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nib_row.view.*

class Mainadapter (val homefeed: Array<Homefeed> , val context: Context)  : RecyclerView.Adapter <CustomViewHolder>() {

    override fun getItemCount(): Int {
        return homefeed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //how do we create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.nib_row,parent,false)
        return CustomViewHolder(cellForRow)

    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // parsing data into the view
        val obj = homefeed.get(position)
        holder.view.nib_name.text = obj.full_name
        holder.view.nib_club.text = obj.club
        holder.view.nib_work.text = obj.designation
        holder.view.nib_year.text = obj.year

        val number = obj.phone_no.trim()
        val mail = obj.email
        val linkedin = obj.profiles.get(0).link
        val git = obj.profiles.get(1).link
        val cc = obj.profiles.get(2).link
        val cf = obj.profiles.get(3).link

        // load image
        val image = holder.view.nib_image
        Picasso.get().load(obj.profile_pic).into(image)

        // Setting onclick listener for every member

        holder.view.nib_call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(number)))
            context.startActivity(intent)
        }
        holder.view.nib_mail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+Uri.encode(mail)))
            context.startActivity(intent)
        }
        holder.view.nib_linkedIn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(linkedin))
            context.startActivity(intent)
        }
        holder.view.nib_git.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(git))
            context.startActivity(intent)
        }
        holder.view.nib_cc.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(cc))
            context.startActivity(intent)
        }
        holder.view.nib_cf.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(cf))
            context.startActivity(intent)
        }
    }
}
class links ( val link: String ,val website : String)
class CustomViewHolder (val view : View) : RecyclerView.ViewHolder (view) {
}