package com.appglods

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.appgodx.router.Router
import com.godx.annotation.router.ARouter
import com.godx.annotation.router.RouterField

@ARouter(path = "/app/mo")
open class MainActivity:AppCompatActivity(), View.OnClickListener {
    @RouterField
    var age:String? = null
    @RouterField
    var name:Int? = 0
    @RouterField
    var gg:Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.appgodx.R.layout.activity_main)
        findViewById<Button>(com.appgodx.R.id.btn_ffmpeg).setOnClickListener(this)
        findViewById<Button>(com.appgodx.R.id.btn_opengl).setOnClickListener(this)
        findViewById<Button>(com.appgodx.R.id.btn_virtual).setOnClickListener(this)
        Log.e("TAG","${applicationContext.applicationContext}")

    }

    override fun onClick(v: View?) {
        when(v?.id){
            com.appgodx.R.id.btn_ffmpeg -> Router.with(this)
                .from("name","六花花")
                .from("age",16)
                .to("/ffmpegtest/main")
                .router()
            com.appgodx.R.id.btn_opengl -> Router.with(this)
                .from("name","opengl")
                .from("age",18)
                .to("/opengltest/main")
                .router()
            com.appgodx.R.id.btn_virtual->{
                val intent = Intent()
                intent.setComponent(ComponentName("com.example.plugin","com.example.plugin.TestActivity"))
                startActivity(intent)
            }
        }
    }
}