package com.patricio.dutra.buscacep.controller

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.patricio.dutra.buscacep.R
import com.patricio.dutra.buscacep.model.Endereco
import com.patricio.dutra.buscacep.utils.Endpoint
import com.patricio.dutra.buscacep.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CepController(var act:Activity) {

    private fun validar(cep:String):Boolean{

        if(cep.isEmpty() || cep.length != 8)
            return false

        return true
    }

    public fun buscarDadoPeloCEP(cep:String){

        act.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

        if(validar(cep)){

            val retrofitClient = NetworkUtils
                .getRetrofitInstance("https://viacep.com.br/ws/$cep/")

            val endpoint = retrofitClient.create(Endpoint::class.java)
            val callback = endpoint.getEndereco()

            callback.enqueue(object : Callback<Endereco> {

                override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {

                    act.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                    act.findViewById<EditText>(R.id.edtRua).visibility = View.VISIBLE
                    act.findViewById<EditText>(R.id.edtRua).setText(response.body()?.logradouro)

                    act.findViewById<EditText>(R.id.edtBairro).visibility = View.VISIBLE
                    act.findViewById<EditText>(R.id.edtBairro).setText(response.body()?.bairro)

                    act.findViewById<EditText>(R.id.edtCidade).visibility = View.VISIBLE
                    act.findViewById<EditText>(R.id.edtCidade).setText(response.body()?.localidade)

                    act.findViewById<EditText>(R.id.edtEstado).visibility = View.VISIBLE
                    act.findViewById<EditText>(R.id.edtEstado).setText(response.body()?.uf)

                }

                override fun onFailure(call: Call<Endereco>, t: Throwable) {
                    act.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    Toast.makeText(act, t.message, Toast.LENGTH_SHORT).show()
                }

            })

        }else{
            Toast.makeText(act,"CEP inválido", Toast.LENGTH_LONG).show()
        }

    }

    public fun buscarLocalizacao(){




    }

}