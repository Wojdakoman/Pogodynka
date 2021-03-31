package eu.lemurweb.pogodynka.view

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import eu.lemurweb.pogodynka.R
import eu.lemurweb.pogodynka.model.bindImage
import eu.lemurweb.pogodynka.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var layoutManager: LinearLayoutManager

    //get arguments passed to fragment in navigation
    private val args: WeatherFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        //observe if current weather has changed
        viewModel.weather.observe(viewLifecycleOwner, Observer {
            if(it != null){
                //if not null, update ui
                bindImage(weatherIcon, "https://openweathermap.org/img/wn/${it?.weather?.get(0)?.icon!!}@4x.png")

                txtCity.text = it?.name
                txtTemp.text = "${it?.main?.temp}${getString(R.string.degree)}C"
                txtFeelsLike.text = "${getString(R.string.feelslike)} ${it?.main?.feels_like}${getString(R.string.degree)}C"

                txtWeatherDesc.text = it?.weather?.get(0)?.description

                txtWindSpeed.text = "${it?.wind?.speed} m/s"
                txtWindDirection.text = viewModel.getWindDirection(it?.wind?.deg)
                txtPressure.text = "${it?.main.pressure} hPA"

                txtSunrise.text = viewModel.unixToString(it?.sys.sunrise + it?.timezone)
                txtSunset.text = viewModel.unixToString(it?.sys.sunset + it?.timezone)
                //save name of searched city
                viewModel.saveCity()
            } else Toast.makeText(context, getString(R.string.notFound), Toast.LENGTH_SHORT).show()
        })
        //show progress circle or not
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if(it)
                progressCircle.visibility = View.VISIBLE
            else progressCircle.visibility = View.GONE
        })
        //observe is home city has changed. if so update ui
        viewModel.homeCity.observe(viewLifecycleOwner, Observer {
            Log.d("[CITY]", "$it")
            if(viewModel.shouldLoadWeather){ //block loading new weather if old search results are still available
                if (it == null) viewModel.getCurrentWeather("Gliwice") //default city is Gliwice (when home city is not set)
                else viewModel.getCurrentWeatherInHome()
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //load default city or from args
        if(args.cityID != -1)
            viewModel.getCurrentWeather(cityId = args.cityID)

        //update ui date
        txtDate.text = "${viewModel.dayName}, ${viewModel.currentDate}"
        //bottom navigation clicks handler
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    if(viewModel.isSetHomeCity)
                        viewModel.getCurrentWeatherInHome()
                    else
                        //show dialog which informs that home city is not set
                        MaterialAlertDialogBuilder(requireContext()).setMessage(getString(R.string.homeCityNotSet))
                            .setNegativeButton(getString(R.string.no)){ dialog, which ->
                                dialog.dismiss()
                            }.setPositiveButton(getString(R.string.yes)){ dialog, whihc ->
                                viewModel.setCurrentAsHome()
                                dialog.dismiss()
                            }.show()

                    true
                }
                R.id.searchList -> {
                    view.findNavController().navigate(R.id.action_weatherFragment_to_listFragment)
                    true
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.defaulttopbar, menu)

        val searchItem = menu.findItem(R.id.btnSearch)
        val searchView: SearchView = searchItem.actionView as SearchView
        //search view handler
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.getCurrentWeather(p0!!)
                searchView.clearFocus()
                searchItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.changeView -> {
                view?.findNavController()?.navigate(R.id.action_weatherFragment_to_simplyWeatherFragment)
                true
            }
            R.id.setHome -> {
                viewModel.setCurrentAsHome()
                true
            }
            R.id.saveCity -> {
                //save information about current city in favourite list
                viewModel.saveCurrent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}