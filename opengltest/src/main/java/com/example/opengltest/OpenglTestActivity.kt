package com.example.opengltest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.appgodx.router.Call
import com.appgodx.router.RouterParameterManager
import com.godx.annotation.router.ARouter
import com.godx.annotation.router.RouterField

@ARouter(path = "/opengltest/main")
open class OpenglTestActivity :AppCompatActivity(){
    @RouterField
    var name:String? = null
    @RouterField
    var age:Int? = 0
    @RouterField("/ffmpegtest/ffcall")
    var call:Call? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl)
        RouterParameterManager.getInstance().bindParameter(this)
        Log.e("TAG","name="+name+",age="+age+",call="+call.toString())
    }
}