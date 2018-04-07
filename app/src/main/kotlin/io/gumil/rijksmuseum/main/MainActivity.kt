package io.gumil.rijksmuseum.main

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.gumil.rijksmuseum.R

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
