package com.example.taximeter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TravelsAdapter(private var items: List<Travel>): RecyclerView.Adapter<TravelsAdapter.ProductsViewHolder>(), Filterable {

    var productsFilteredList: List<Travel> = ArrayList()

    init {
        productsFilteredList = items
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var charSearch = p0.toString()
                if (charSearch.isEmpty()) {
                    productsFilteredList = items
                }else {
                    //var resultList = ArrayList<Product>()
//                    for (product in items) {
//                        if (product.title.lowercase().contains(charSearch.lowercase())) {
//                            resultList.add(product)
//                        }
//                    }
                    //productsFilteredList = resultList

                    productsFilteredList = items.filter {
                            product ->  product.title.lowercase().contains(charSearch.lowercase())
                    }

                }
                val filterResults = FilterResults()
                filterResults.values = productsFilteredList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                productsFilteredList = p1?.values as ArrayList<Travel>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return ProductsViewHolder(itemView)
    }
    override fun getItemCount(): Int = productsFilteredList.size
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = productsFilteredList[position]
        holder.bind(product)
    }
    inner class ProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var tvTitle: TextView
        private var rbRating: RatingBar
        private var tvPrice: TextView
        private var imageProduct: ImageView
        init {
            tvTitle = itemView.findViewById(R.id.tvTitre)
            rbRating = itemView.findViewById(R.id.rbRating)
            tvPrice = itemView.findViewById(R.id.tvPrice)
            imageProduct = itemView.findViewById(R.id.imageProduct)
        }
        fun bind(product: Travel) {
            tvTitle.text = product.title
            rbRating.rating = product.rating
            tvPrice.text = product.price
            imageProduct.setImageResource(product.image)
        }
    }

}