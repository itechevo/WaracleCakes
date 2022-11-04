package com.itechevo.waraclecakes.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itechevo.domain.model.Cake
import com.itechevo.waraclecakes.R
import com.itechevo.waraclecakes.extensions.loadRoundedImage

class CakeListAdapter(private val clickListener: ((Cake) -> Unit)? = null) :
    RecyclerView.Adapter<CakeListAdapter.ViewHolder>() {

    private var cakeList: List<Cake> = listOf()

    fun update(data: List<Cake>) {
        cakeList = data

        //TODO: Use DiffUtil to update only changed item
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cake_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(cakeList[position], clickListener)

    override fun getItemCount() = cakeList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleView = itemView.findViewById<TextView>(R.id.cake_title)
        private val imageView = itemView.findViewById<ImageView>(R.id.cake_image)

        fun bind(item: Cake, clickListener: ((Cake) -> Unit)? = null) =
            with(itemView) {
                titleView.text = item.title

                imageView.loadRoundedImage(item.imageUrl)

                setOnClickListener {
                    clickListener?.let { it(item) }
                }
            }
    }
}