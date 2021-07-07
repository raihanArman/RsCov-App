package id.co.list_rs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.airbnb.deeplinkdispatch.DeepLink
import id.co.data.util.AppLink
import id.co.list_rs.HasilMetodeActivity.Companion.METODE_ASTAR
import id.co.list_rs.HasilMetodeActivity.Companion.METODE_BELL
import id.co.list_rs.databinding.ActivityListHospitalBinding


@DeepLink(AppLink.ListHospital.LIST_HOSPITAL_LINK)
class ListHospitalActivity : AppCompatActivity() {

    lateinit var dataBinding: ActivityListHospitalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_hospital)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        dataBinding.btnStar.setOnClickListener {
            val intent = Intent(this, HasilMetodeActivity::class.java)
            intent.putExtra("metode", METODE_ASTAR)
            startActivity(intent)
        }

        dataBinding.btnBell.setOnClickListener {
            val intent = Intent(this, HasilMetodeActivity::class.java)
            intent.putExtra("metode", METODE_BELL)
            startActivity(intent)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}