package com.coding.flickertask.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coding.flickertask.R
import com.facebook.drawee.backends.pipeline.Fresco

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, PhotoListFragment.newInstance(), PhotoListFragment::class.qualifiedName)
            .commit()

    }
}
