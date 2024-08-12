package io.ashkay.learnandroid.misc

class MySdkProvider private constructor() {
    companion object {
        val INSTANCE: MySdkProvider by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MySdkProvider() }
    }
}

class MySdkProvider2 private constructor() {
    companion object {

        @Volatile
        private var instance: MySdkProvider2? = null // Volatile modifier is necessary

        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: MySdkProvider2().also { instance = it }
            }
    }
}