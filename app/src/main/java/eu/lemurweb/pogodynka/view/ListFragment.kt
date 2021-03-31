package eu.lemurweb.pogodynka.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.lemurweb.pogodynka.R
import eu.lemurweb.pogodynka.view.adapter.CitiesListAdapter
import eu.lemurweb.pogodynka.viewmodel.ListsViewModel
import eu.lemurweb.pogodynka.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private lateinit var viewModel: ListsViewModel
    private lateinit var savedListAdapter: CitiesListAdapter
    private lateinit var lastListAdapter: CitiesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(ListsViewModel::class.java)
        savedListAdapter = CitiesListAdapter(viewModel.savedList)
        lastListAdapter = CitiesListAdapter(viewModel.lastSearchList)

        viewModel.savedList.observe(viewLifecycleOwner, Observer {
            savedListAdapter.notifyDataSetChanged()
        })

        viewModel.lastSearchList.observe(viewLifecycleOwner, Observer {
            lastListAdapter.notifyDataSetChanged()
        })

        viewModel._homeCity.observe(viewLifecycleOwner, Observer {
            if(it != null) txtHomeCityName.text = it.name
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedList.apply {
            adapter = savedListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        lastList.apply {
            adapter = lastListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        txtHomeCityName.setOnClickListener {
            if(viewModel._homeCity.value != null){
                val action = ListFragmentDirections.actionListFragmentToWeatherFragment(viewModel._homeCity.value?.api_id!!)
                view.findNavController().navigate(action)
            }
        }
    }
}