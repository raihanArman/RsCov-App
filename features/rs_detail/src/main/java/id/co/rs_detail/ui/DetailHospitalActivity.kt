package id.co.rs_detail.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.airbnb.deeplinkdispatch.DeepLink
import id.co.data.util.AppLink
import id.co.rs_detail.R
import id.co.rs_detail.module.DetailModule.detailModule
import org.koin.core.context.loadKoinModules

@DeepLink(AppLink.DetailHospital.HOSPITAL_DETAIL)
class DetailHospitalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_hospital)
        loadKoinModules(detailModule)
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val parameters = intent.extras?: Bundle()
            val id = parameters.getString(AppLink.DetailHospital.PARAM_HOSPITAL_ID)?: ""
            Toast.makeText(this, "${id}", Toast.LENGTH_SHORT).show()
        }
    }
}