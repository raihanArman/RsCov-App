package id.co.rscov.deeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import id.co.home.module.HomeDeepLink
import id.co.home.module.HomeDeepLinkLoader

@DeepLinkHandler(
    AppDeepLinkModule::class,
    HomeDeepLink::class
)
class DeepLinkRouterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLinkDelegate = DeepLinkDelegate(
            AppDeepLinkModuleLoader(),
            HomeDeepLinkLoader()
        )
        deepLinkDelegate.dispatchFrom(this)
        finish()
    }
}