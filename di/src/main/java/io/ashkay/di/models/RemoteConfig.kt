package io.ashkay.di.models

import javax.inject.Inject
import kotlin.random.Random

class RemoteConfig @Inject constructor() {
    fun getConfig(): Int {
        return Random.nextInt()
    }
}