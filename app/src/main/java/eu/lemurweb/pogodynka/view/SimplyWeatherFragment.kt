package eu.lemurweb.pogodynka.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import eu.lemurweb.pogodynka.R
import eu.lemurweb.pogodynka.model.bindImage
import eu.lemurweb.pogodynka.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_simply_weather.*
import kotlinx.android.synthetic.main.fragment_weather.progressCircle
import kotlinx.android.synthetic.main.fragment_weather.txtCity
import kotlinx.android.synthetic.main.fragment_weather.txtDate
import kotlinx.android.synthetic.main.fragment_weather.txtFeelsLike
import kotlinx.android.synthetic.main.fragment_weather.txtPressure
import kotlinx.android.synthetic.main.fragment_weather.txtSunrise
import kotlinx.android.synthetic.main.fragment_weather.txtSunset
import kotlinx.android.synthetic.main.fragment_weather.txtTemp
import kotlinx.android.synthetic.main.fragment_weather.txtWeatherDesc
import kotlinx.android.synthetic.main.fragment_weather.txtWindDirection
import kotlinx.android.synthetic.main.fragment_weather.txtWindSpeed
import kotlinx.android.synthetic.main.fragment_weather.weatherIcon

class SimplyWeatherFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var layoutManager: LinearLayoutManager

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
            } else Toast.makeText(context, getString(R.string.notFound), Toast.LENGTH_SHORT).show()
        })
        //show progress circle or not
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if(it)
                progressCircle.visibility = View.VISIBLE
            else progressCircle.visibility = View.GONE
        })
        //
        viewModel.homeCity.observe(viewLifecycleOwner, Observer {
            Log.d("[CITY]", "$it")
            if(viewModel.shouldLoadWeather){
                if (it == null) viewModel.getCurrentWeather("Gliwice")
                else viewModel.getCurrentWeatherInHome()
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simply_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.getCurrentWeather("Gliwice")
        txtDay.text = "${getString(R.string.todayIs)} ${viewModel.dayName}"
        txtDate.text = viewModel.currentDate

        //show home button handler
        showHome.setOnClickListener {
            if(viewModel.isSetHomeCity)
                viewModel.getCurrentWeatherInHome()
            else
                MaterialAlertDialogBuilder(requireContext()).setMessage(getString(R.string.homeCityNotSet))
                        .setNegativeButton(getString(R.string.no)){ dialog, which ->
                            dialog.dismiss()
                        }.setPositiveButton(getString(R.string.yes)){ dialog, whihc ->
                            viewModel.setCurrentAsHome()
                            dialog.dismiss()
                        }.show() //show dialog which informs that home city is not set
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.simpletopbar, menu)

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
                view?.findNavController()?.navigate(R.id.action_simplyWeatherFragment_to_weatherFragment)
                true
            }
            R.id.setHome -> {
                viewModel.setCurrentAsHome()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}