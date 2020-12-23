package com.example.template.home.ui.listadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.template.R
import com.example.template.databinding.CityListItemBinding

class CityAdapter(
    var list: MutableList<String>
) :
    RecyclerView.Adapter<CityAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: CityListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: CityListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.city_list_item, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.name = list[position]
    }

}
