package io.gumil.rijksmuseum.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.list.RijksListFragment

class MainActivity : DaggerAppCompatActivity(), Backstack {

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

    override fun goTo(fragment: Fragment, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        supportFragmentManager.beginTransaction()
                .apply {
                    if (addToBackStack) {
                        addToBackStack(tag)
                        setCustomAnimations(
                                R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left,
                                R.anim.slide_in_from_left,
                                R.anim.slide_out_to_right
                        )
                    }
                }
                .add(R.id.fragmentContainer, fragment, tag)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
