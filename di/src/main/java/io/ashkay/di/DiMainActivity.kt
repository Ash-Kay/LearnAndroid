package io.ashkay.di

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import io.ashkay.di.dagger.DaggerMainComponent
import io.ashkay.di.models.UpdateManager
import io.ashkay.di.models.UpdateManagerSameTTL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiMainActivity : AppCompatActivity() {

    @Inject
    lateinit var updateManager: UpdateManager

    @Inject
    lateinit var updateManagerSameTTL: UpdateManagerSameTTL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_di_main)

        val daggerComponent = DaggerMainComponent.factory().create(this)
        daggerComponent.inject(this)

        lifecycleScope.launch {
            repeat(50) {
                updateManager.update()
                updateManagerSameTTL.update()
                delay(1000)
            }
        }
    }
}