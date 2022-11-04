package com.itechevo.waraclecakes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itechevo.waraclecakes.ui.CakeFragment

class CakeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cake)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CakeFragment.newInstance())
                .commitNow()
        }
    }
}