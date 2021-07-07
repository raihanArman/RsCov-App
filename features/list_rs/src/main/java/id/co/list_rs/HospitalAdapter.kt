package id.co.list_rs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.data.data.model.Hospital
import id.co.list_rs.databinding.ItemHasilBinding

class HospitalAdapter(val context: Context, val showDetail: (Hospital) -> Unit): RecyclerView.Adapter<HospitalAdapter.ViewHolder>(){

    val listHospital = ArrayList<Hospital>()

    fun setListHospital(newListHospital: List<Hospital>){
        if(newListHospital.isEmpty()) return
        listHospital.clear()
        listHospital.addAll(newListHospital)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHasilBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(hospital: Hospital){
            with(binding){
                var jarak: String = if(hospital.jarak != ""){
                    (hospital.jarak!!.toFloat()/1000).toString()
                }else{
                    "0"
                }
                tvHospital.text = hospital.name
                tvLocation.text = hospital.location
                tvJarak.text = "${jarak} Km"
            }
            itemView.setOnClickListener {
                showDetail(hospital)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemHasilBinding = DataBindingUtil.inflate(inflater, R.layout.item_hasil, parent, false)
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