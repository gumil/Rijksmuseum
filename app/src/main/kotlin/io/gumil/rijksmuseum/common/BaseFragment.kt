package io.gumil.rijksmuseum.common

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import io.gumil.kaskade.Action
import io.gumil.kaskade.State
import io.reactivex.Observable

internal abstract class BaseFragment<S : State, A : Action> : DaggerFragment() {

    abstract val layoutId: Int
    abstract val viewModel: BaseViewModel<S, A, *>
    protected val rxLifecycle = RxLifecycle()

    private val appActivity
        get() = (activity as? AppCompatActivity)

    var title: String?
        get() = appActivity?.title.toString()
        set(value) {
            appActivity?.title = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(rxLifecycle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(this, Observer<S> {
            it?.render()
        })

        viewModel.processActions(actions())

        initializeViews(view)
    }

    fun showSnackbarError(@StringRes stringRes: Int) {
        view?.let {
            Snackbar.make(it, stringRes, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun setToolbar(toolbar: Toolbar) {
        appActivity?.setSupportActionBar(toolbar)
    }

    open fun initializeViews(view: View) {}

    abstract fun actions(): Observable<A>

    abstract fun S.render()
}