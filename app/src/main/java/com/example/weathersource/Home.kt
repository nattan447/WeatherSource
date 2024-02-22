package com.example.weathersource

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        fun checkIsEmpty(content:String):Boolean{
            val regex  =  Regex("^\\S+$")

            val nonSpaceList =  content.split("").filter {  regex.matches(it) === true }

            val taskLength = nonSpaceList.joinToString(separator = "").length

            if(taskLength>=1){


                return false
            }

            return true
        }

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val input = view.findViewById<TextInputEditText>(R.id.inputCity)

        val mainLayout = view.findViewById<LinearLayout>(R.id.mainLayoutHome)

        val btn = view.findViewById<Button>(R.id.searchPlaceBtn)

        btn.setOnClickListener {




            val inputTxt = input.text.toString()

            if(inputTxt.isNotEmpty() && !checkIsEmpty(inputTxt))
            {

                btn.isEnabled = false

                val weather = WeatherApi(inputTxt,"brazil")


                weather.conectApi()


                val routine = lifecycleScope.launch{

                    val progressBarLayoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT

                    )
                    val progressbar : ProgressBar = ProgressBar(requireContext());

                    progressbar.layoutParams = progressBarLayoutParams

                    progressBarLayoutParams.gravity = Gravity.CENTER_HORIZONTAL

                    mainLayout.addView(progressbar)

                    weather.openApi();


                    println("API was openned")

                    println("coroutine has completed")


                    mainLayout.removeView(progressbar)

                }

                routine.invokeOnCompletion {

                    println("after coroutine")

                    try {
                        val placeNotFound = view.findViewById<TextView>(R.id.placeNotFoundTxt)

                        val gson = Gson()

                        val weatherData =  weather?.data


                        if(weatherData!=null){
                            placeNotFound.setText("")

                            val bundle = Bundle()

                            bundle.putString("weatherData",gson.toJson(weatherData))

                            findNavController().navigate(R.id.action_home_to_weatherDetails, bundle)

                        }
                        else{

                            btn.isEnabled = true

                            placeNotFound.setText("place was not found")

                            println("place was not found")
                        }

                    }
                    catch (e:Exception){

                        println("error ocurred $e")
                    }
                }



            }
            else {

                println("digite uma cidade")

            }


        }






        return view
    }


}