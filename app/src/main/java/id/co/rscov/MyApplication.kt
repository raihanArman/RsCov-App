package id.co.rscov

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import id.co.data.di.CoreModule.networkModule
import id.co.data.di.CoreModule.repositoryModule
import id.co.home.module.HomeModule.homeModule
import id.co.rscov.di.AppModule.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(listOf(
                networkModule,
                repositoryModule,
                useCaseModule
            ))
        }
    }
}