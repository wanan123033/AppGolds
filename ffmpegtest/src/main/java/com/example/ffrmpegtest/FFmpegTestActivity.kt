package com.example.ffrmpegtest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.appgodx.router.Router
import com.appgodx.router.RouterParameterManager
import com.godx.annotation.router.ARouter
import com.godx.annotation.router.RouterField

@ARouter(path = "/ffmpegtest/main")
open class FFmpegTestActivity:AppCompatActivity(), View.OnClickListener {
    @RouterField
    var name:String? = null
    @RouterField
    var age:Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg)
        RouterParameterManager.getInstance().bindParameter(this)
        Log.e("TAG","name="+name+",age="+age)

        findViewById<Button>(R.id.btn_opengl).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Router.with(this)
            .from("name","opengl")
            .from("age",18)
            .to("/opengltest/main")
            .router()
    }
}