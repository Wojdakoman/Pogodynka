package eu.lemurweb.pogodynka.model.entity

data class Current(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val sys: Sys,
    val timezone: Int,
    val name: String,
    val id: Int
)