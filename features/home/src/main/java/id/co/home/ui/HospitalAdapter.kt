package id.co.home.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.data.data.model.Hospital
import id.co.home.R
import id.co.home.databinding.ItemHospitalBinding

class HospitalAdapter(val context: Context, val showDetail: (Hospital) -> Unit): RecyclerView.Adapter<HospitalAdapter.ViewHolder>(){

    val listHospital = ArrayList<Hospital>()

    fun setListHospital(newListHospital: List<Hospital>){
        if(newListHospital.isEmpty()) return
        listHospital.clear()
        listHospital.addAll(newListHospital)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHospitalBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(hospital: Hospital){
            with(binding){
                tvHospital.text = hospital.name
                Glide.with(context)
                    .load(hospital.image)
                    .into(ivHospital)
            }
            itemView.setOnClickListener {
                showDetail(hospital)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemHospitalBinding = DataBindingUtil.inflate(inflater, R.layout.item_hospital, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hospital = listHospital[position]
        holder.bind(hospital)
    }

    override fun getItemCount(): Int {
        return listHospital.size
    }

}