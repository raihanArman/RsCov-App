package id.co.rscov.deeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import id.co.home.module.HomeDeepLink
import id.co.home.module.HomeDeepLinkLoader
import id.co.list_rs.module.ListHospitalLink
import id.co.list_rs.module.ListHospitalLinkLoader
import id.co.rs_detail.module.DetailHospitalLink
import id.co.rs_detail.module.DetailHospitalLinkLoader

@DeepLinkHandler(
    AppDeepLinkModule::class,
    HomeDeepLink::class,
    ListHospitalLink::class,
    DetailHospitalLink::class
)
class DeepLinkRouterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLinkDelegate = DeepLinkDelegate(
            AppDeepLinkModuleLoader(),
            HomeDeepLinkLoader(),
            ListHospitalLinkLoader(),
            DetailHospitalLinkLoader()
        )
        deepLinkDelegate.dispatchFrom(this)
        finish()
    }
}