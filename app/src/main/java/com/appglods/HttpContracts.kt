package com.appglods

import com.appgodx.retrofit.Converter
import com.appgodx.retrofit.GsonConverterFactory
import com.appgodx.rxjava.Observable
import com.godx.annotation.retrofit.*
import com.google.gson.Gson
import okhttp3.OkHttpClient

@HttpModel(baseUrl = "www.baidu.com")
interface HttpContracts {
    companion object{
        @OKHTTP
        val okHttpClient:OkHttpClient? = OkHttpClient.Builder().build()
        @IConverter
        val converterF = GsonConverterFactory<Any>(Gson())
    }
    @HTTP(url = "/app/search")
    fun search(@Field("q")q:String):Observable<SearchBean>

}
