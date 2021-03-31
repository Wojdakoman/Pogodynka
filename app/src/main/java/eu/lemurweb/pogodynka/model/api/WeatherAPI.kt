package eu.lemurweb.pogodynka.model.api

import eu.lemurweb.pogodynka.model.entity.Current
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY: String = "317a8ebd8a2c37babbd93ce1a4bf18aa" //317a8ebd8a2c37babbd93ce1a4bf18aa //3aa09f21d6c61c817b25075fbaf29495 //6f308e77e37f32e7b8fb59ad02264ad4
const val API: String = "?appid=$API_KEY&units=metric&lang=pl"
interface WeatherAPI {
    @GET("weather$API")
    fun getCityMyName(@Query("q") city: String): Call<Current>

    @GET("weather$API")
    fun getCityMyId(@Query("id") city: Int): Call<Current>
}