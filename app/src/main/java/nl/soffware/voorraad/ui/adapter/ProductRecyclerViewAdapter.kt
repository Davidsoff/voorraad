package nl.soffware.voorraad.ui.adapter

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nl.soffware.voorraad.databinding.FragmentProductItemBinding
import nl.soffware.voorraad.model.Product

class ProductRecyclerViewAdapter(
    private val values: List<Product>
) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var binding: FragmentProductItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerViewAdapter.ViewHolder {
        context = parent.context
        binding = FragmentProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.product_name
        holder.quantity.text = item.quantity
        Glide.with(context).load(item.image_url).into(holder.image)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = binding.name
        val quantity = binding.quantity
        val image = binding.image
    }
}