package com.patricio.dutra.buscacep.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.patricio.dutra.buscacep.R
import com.patricio.dutra.buscacep.controller.CepController
import com.patricio.dutra.buscacep.controller.GPSController
import kotlinx.android.synthetic.main.activity_cep.*

class CepActivity : AppCompatActivity() {

    lateinit var cepController:CepController
    lateinit var gpsController:GPSController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cep)

        cepController = CepController(this)
        gpsController = GPSController(this)

        btnLocalizacao.setOnClickListener {
            gpsController.buscarLocalizacao()
        }
        btnBuscar.setOnClickListener {
            cepController.buscarDadoPeloCEP(edtCep.text.toString())
        }

    }

    override fun onResume() {
        super.onResume()

        if(gpsController.gpsEstaLigado()) {
            radioGroup.check(R.id.rbLigado)
        }else{
            radioGroup.check(R.id.rbDesligado)
        }

    }
}