package id.co.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import id.co.data.util.AppLink
import id.co.data.util.Constant
import id.co.home.R

@DeepLink(AppLink.Home.HOME_LINK)
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}