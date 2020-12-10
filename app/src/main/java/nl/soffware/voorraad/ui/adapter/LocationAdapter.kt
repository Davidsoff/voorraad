package nl.soffware.voorraad.ui.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.soffware.voorraad.R
import nl.soffware.voorraad.databinding.ItemLocationBinding
import nl.soffware.voorraad.model.Location

class LocationAdapter (private val locations: MutableList<Location>, private val onClick: (Location) -> Unit) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var binding: ItemLocationBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemLocationBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(locations[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { onClick(locations[adapterPosition]) }
        }

        @SuppressLint("SetTextI18n")
        fun bind(location: Location) {
            binding.locationName.text = location.name
            binding.textView2.text = location.products.size.toString()
        }
    }

}