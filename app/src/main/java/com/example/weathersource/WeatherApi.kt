package com.example.weathersource

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import com.example.weathersource.shapeApi.WeatherResponse

class WeatherApi (private  val city: String, private  val country : String ) {

    interface OpenWeatherApiService {
        @GET("weather")
        suspend fun getCurrentWeather(
            @Query("q") city: String,
            @Query("appid") apiKey: String
        ): Response<WeatherResponse>

    }


    private  val key = "f90eecc7a82b453d7ddc570ae56579c1"

    private val url = "https://api.openweathermap.org/data/2.5/"

    private  var apiService :  WeatherApi.OpenWeatherApiService? = null

     var data : WeatherResponse? = null

    fun conectApi(){

        val  retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()

         apiService = retrofit.create(OpenWeatherApiService::class.java)

    }

    suspend  fun openApi():Boolean?{

        if(apiService!=null){
            val response = apiService!!.getCurrentWeather(city, key )

            if(response.isSuccessful){

                val weatherResponse = response.body()

                data = weatherResponse

                return  true

            }
            else {

                println("error to run the API")

                return false

            }

        } else{
            return error("API service is null")

        }

    }


}