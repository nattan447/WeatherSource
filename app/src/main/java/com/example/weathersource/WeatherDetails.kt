package com.example.weathersource

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.weathersource.shapeApi.WeatherResponse
import com.google.gson.Gson


class WeatherDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_details, container, false)

        val backBtn = view.findViewById<Button>(R.id.backBtn)

        val bundle = arguments

        val gson = Gson()

        val weatherDataJson= bundle?.getString("weatherData")

        val weatherDataGson = gson.fromJson(weatherDataJson, WeatherResponse::class.java)

        val temperatureTxt = view.findViewById<TextView>(R.id.temperature)

        val temperatureInCelcius = kotlin.math.round( weatherDataGson.main.temp - 272.15)

        val placeName = view.findViewById<TextView>(R.id.placeName)

        val precipitaionsText = view.findViewById<TextView>(R.id.precipitations)

        val weatherSituation = weatherDataGson.weather[0].main

        val weatherSituationText = view.findViewById<TextView>(R.id.weatherSituation)

        val temperatureMax = kotlin.math.round( weatherDataGson.main.temp_max - 272.15).toString()

        val temperatureMin = kotlin.math.round( weatherDataGson.main.temp_min- 272.15).toString()

        val imageSituation = view.findViewById<ImageView>(R.id.imageSituation)

        weatherSituationText.text = weatherSituation

        placeName.text = weatherDataGson.name

        temperatureTxt.text= temperatureInCelcius.toString() + "°C"

        precipitaionsText.text = "Max :   $temperatureMax°C    Min:  $temperatureMin°C "


        when(weatherSituation){

            "Clouds" -> imageSituation.setImageResource(R.drawable.cloudy)
            "Snow"  -> imageSituation.setImageResource(R.drawable.snow)
            "Clear"  -> imageSituation.setImageResource(R.drawable.sun)
            "Rain"  -> imageSituation.setImageResource(R.drawable.rainingcloud)
            "Thunderstorm"  -> imageSituation.setImageResource(R.drawable.thunderstorm)

        }

        backBtn.setOnClickListener {
            requireActivity().onBackPressed()

        }


        return view
    }


}