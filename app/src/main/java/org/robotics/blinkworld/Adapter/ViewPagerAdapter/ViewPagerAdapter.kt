package org.robotics.blinkworld.Adapter.ViewPagerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.robotics.blinkworld.R

class ViewPagerAdapter(private var images:List<Int>):
    RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {
    inner class Pager2ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val image:ImageView = itemView.findViewById(R.id.view_pager_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_pager_item,parent,false))
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.image.setImageResource(images[position])
    }

    override fun getItemCount(): Int= images.size
}