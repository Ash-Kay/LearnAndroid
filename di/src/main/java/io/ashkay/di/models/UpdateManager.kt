package io.ashkay.di.models

class UpdateManagerSameTTL(
    val ttl: Int,
    val updateUrl: String,
) {
    fun update() {
        println("ASHTEST: UpdateManagerSameTTL: update: $updateUrl $ttl")
    }
}

class UpdateManager(
    val remoteConfig: RemoteConfig,
    val updateUrl: String,
) {
    fun update() {
        println("ASHTEST: UpdateManager: update: $updateUrl ${remoteConfig.getConfig()}")
    }
}