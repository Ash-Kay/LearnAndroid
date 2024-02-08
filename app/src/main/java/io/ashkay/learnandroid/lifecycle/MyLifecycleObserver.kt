package io.ashkay.learnandroid.lifecycle

import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.random.Random
import kotlin.random.nextInt

class MyLifecycleObserver : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Event) {
        println("ASHTEST: ${source} : ${event}")
    }

}