package io.gumil.rijksmuseum.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.gumil.kaskade.Action
import io.gumil.kaskade.Result
import io.gumil.kaskade.State
import io.gumil.kaskade.StateMachine
import io.gumil.kaskade.livedata.stateLiveData
import io.gumil.kaskade.rx.toDeferred
import io.reactivex.Observable

internal abstract class BaseViewModel<S: State, A: Action, R: Result<S>> : ViewModel() {

    protected abstract val stateMachine: StateMachine<S, A, R>

    val state: LiveData<S> get() = stateMachine.stateLiveData()

    fun processActions(action: Observable<A>) {
        stateMachine.processAction(action.toDeferred())
    }

    override fun onCleared() {
        super.onCleared()
        stateMachine.dispose()
    }
}