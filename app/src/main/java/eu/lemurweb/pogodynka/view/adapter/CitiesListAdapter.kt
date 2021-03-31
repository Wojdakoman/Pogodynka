package eu.lemurweb.pogodynka.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.lemurweb.pogodynka.R
import eu.lemurweb.pogodynka.model.entity.SavedCity
import eu.lemurweb.pogodynka.view.ListFragmentDirections
import kotlinx.android.synthetic.main.city_row.view.*

class CitiesListAdapter(var list: LiveData<List<SavedCity>>): RecyclerView.Adapter<CitiesListAdapter.Holder>() {
    inner class Holder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_row, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.txtCity.text = list.value?.get(position)?.name
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToWeatherFragment(list.value?.get(position)?.api_id!!)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list.value?.size?:0
    }
}