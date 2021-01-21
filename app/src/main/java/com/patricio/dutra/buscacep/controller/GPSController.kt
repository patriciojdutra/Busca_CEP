package com.patricio.dutra.buscacep.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.patricio.dutra.buscacep.R
import com.patricio.dutra.buscacep.model.Endereco
import com.patricio.dutra.buscacep.utils.Endpoint
import com.patricio.dutra.buscacep.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class GPSController(var act:Activity) {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun buscarLocalizacao(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(act)

        if(verificarPermissao()){

            if(gpsEstaLigado()){

                act.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->

                    var location: Location? = task.result

                    if(location == null){
                        NewLocationData()
                    }else{
                        buscarEndereco(location.latitude, location.longitude)
                    }
                }

            }else{
                Toast.makeText(act,"É necessário ligar o GPS",Toast.LENGTH_LONG).show()
            }
        }else{
            RequestPermission()
        }
    }

    fun verificarPermissao():Boolean{
            if(
                ActivityCompat.checkSelfPermission(act,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(act,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ){
                return true
            }
            return false
        }

    fun RequestPermission(){
            ActivityCompat.requestPermissions(act, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION), 1010)
        }

    fun gpsEstaLigado():Boolean{
            var locationManager = act.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
        }

    fun NewLocationData(){

            var locationRequest =  LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 0
            locationRequest.fastestInterval = 0
            locationRequest.numUpdates = 1
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(act)
            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return }
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
            )
        }

    private val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {

                var location: Location = locationResult.lastLocation
                buscarEndereco(location.latitude, location.longitude)
            }
        }

    private fun buscarEndereco(lat: Double,long: Double){

            var geoCoder = Geocoder(act, Locale.getDefault())
            var Adress = geoCoder.getFromLocation(lat,long,1)

            if(Adress != null) {

                setEstado(Adress.get(0).adminArea)
                setCidade(Adress.get(0).subAdminArea)
                setBairro(Adress.get(0).subLocality)
                setRua(Adress.get(0).thoroughfare)
                setCEP(Adress.get(0).postalCode)
            }

            act.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
        }

    fun setCEP(cep:String){
        try {
            act.findViewById<EditText>(R.id.edtCep).visibility = View.VISIBLE
            act.findViewById<EditText>(R.id.edtCep).setText(cep.replace("-",""))
        }catch (e:Exception){}
    }

    fun setRua(rua:String){
        try {
            act.findViewById<EditText>(R.id.edtRua).visibility = View.VISIBLE
            act.findViewById<EditText>(R.id.edtRua).setText(rua)
        }catch (e:Exception){}
    }

    fun setBairro(bairro:String){
        try {
            act.findViewById<EditText>(R.id.edtBairro).visibility = View.VISIBLE
            act.findViewById<EditText>(R.id.edtBairro).setText(bairro)
        }catch (e:Exception){}
    }

    fun setCidade(cidade:String){
        try {
            act.findViewById<EditText>(R.id.edtCidade).visibility = View.VISIBLE
            act.findViewById<EditText>(R.id.edtCidade).setText(cidade)
        }catch (e:Exception){}
    }

    fun setEstado(estado:String){
        try {
            act.findViewById<EditText>(R.id.edtEstado).visibility = View.VISIBLE
            act.findViewById<EditText>(R.id.edtEstado).setText(estado)
        }catch (e:Exception){}
    }


}

