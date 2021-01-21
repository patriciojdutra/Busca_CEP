package com.patricio.dutra.buscacep.utils

import com.patricio.dutra.buscacep.model.Endereco
import retrofit2.Call
import retrofit2.http.GET

interface Endpoint {

    @GET("json/")
    fun getEndereco() : Call<Endereco>

}