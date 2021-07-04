package id.co.list_rs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import id.co.data.util.AppLink


@DeepLink(AppLink.ListHospital.LIST_HOSPITAL_LINK)
class ListHospitalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_hospital)
    }
}