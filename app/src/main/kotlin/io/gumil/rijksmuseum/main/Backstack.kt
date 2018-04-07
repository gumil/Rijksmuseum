package io.gumil.rijksmuseum.main

import android.support.v4.app.Fragment

internal interface Backstack {
    fun goTo(fragment: Fragment, addToBackStack: Boolean = true)
}