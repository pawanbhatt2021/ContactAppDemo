package com.contactapp.contactappdemo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.contactapp.contactappdemo.databinding.ContactRowBinding

class ContactAdapter(private val list:List<String>) : RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {


    inner class MyViewHolder(val viewDataBinding: ContactRowBinding):RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.MyViewHolder {
        Log.d("ContactSize",list.size.toString())
        val binding = ContactRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactAdapter.MyViewHolder, position: Int) {
        val binding = holder.viewDataBinding
        binding.contactName.text = list[position]
        binding.mobileNumber.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}