package io.ashkay.di.models

class UpdateManager(
    val ttl: Int,
    val updateUrl: String,
) {
    fun update() {
        println("ASHTEST: UpdateManager: update: $updateUrl $ttl")
    }
}