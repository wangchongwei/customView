package com.justin.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var xValue = arrayOf("一", "二", "san")
        var yValue = floatArrayOf(70f, 80f, 90f);
        myView.setData(yValue, xValue);
    }
}
