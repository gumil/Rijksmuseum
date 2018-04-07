package io.gumil.rijksmuseum.main

import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.support.DaggerAppCompatActivity
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.list.RijksListFragment

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            goTo(RijksListFragment.newInstance(), false)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun goTo(fragment: Fragment, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right
                )
                .add(R.id.fragmentContainer, fragment, tag)
                .apply {
                    if (addToBackStack) {
                        addToBackStack(tag)
                    }
                }.commit()
    }
}
