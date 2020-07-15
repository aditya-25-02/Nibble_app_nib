package com.example.ncscommunity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nib_row.view.*
import kotlinx.android.synthetic.main.project_item.view.*

class projectAdapter (val projectfeed: Array<Projectfeed>, val context: Context)  : RecyclerView.Adapter <projectViewHolder>() {

    override fun getItemCount(): Int {
        return projectfeed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): projectViewHolder {
        //how do we create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.project_item,parent,false)
        return projectViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: projectViewHolder, position: Int) {

        val obj = projectfeed.get(position)
        val p_name = obj.project_name
        val p_des = obj.project_description

        holder.view.project_name.text = p_name
        holder.view.project_desc.text = p_des

        // load image
        val image = holder.view.project_image
        Picasso.get().load(obj.background).into(image)

        holder.view.project_github.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(obj.github))
            context.startActivity(i)
        }
    }

}
class projectViewHolder (val view : View) : RecyclerView.ViewHolder (view) {
}
