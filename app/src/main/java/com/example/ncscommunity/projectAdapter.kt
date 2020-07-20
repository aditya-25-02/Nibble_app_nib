package com.example.ncscommunity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.method.ScrollingMovementMethod
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


        var team_members: String = ""
        for ( s in 0 until obj.team.size){
            val temp = obj.team[s].full_name
            team_members += temp
            if(s!=obj.team.count()-1){
                team_members+="\n"
            }
        }
        val tech_used = obj.techstack
        var tech_stack: String = ""

        for(t in 0 until tech_used.languages.size){
            var temp = tech_used.languages[t].language+"\n"
            tech_stack+=temp
        }
        for(t in 0 until tech_used.frontend_techs.size){
            var temp1 = tech_used.frontend_techs[t].frontend_tech + "\n"
            tech_stack+=temp1
        }
        for(t in 0 until tech_used.backend_techs.size){
            var temp2 = tech_used.backend_techs[t].backend_tech + "\n"
            tech_stack+=temp2
        }
        for(t in 0 until tech_used.app_techs.size){
            var temp3 = tech_used.app_techs[t].app_tech + "\n"
            tech_stack+=temp3
        }

        holder.view.project_team.text = team_members
        holder.view.project_stack2.text = tech_stack
        holder.view.project_stack2.movementMethod = ScrollingMovementMethod()
        holder.view.project_team.movementMethod = ScrollingMovementMethod()
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
