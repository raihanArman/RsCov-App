package id.co.rs_detail.ui

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.airbnb.deeplinkdispatch.DeepLink
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import id.co.data.util.AppLink
import id.co.data.util.Constant
import id.co.rs_detail.R
import id.co.rs_detail.databinding.ActivityDetailHospitalBinding
import id.co.rs_detail.module.DetailModule.detailModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@DeepLink(AppLink.DetailHospital.HOSPITAL_DETAIL)
class DetailHospitalActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: DetailViewModel by viewModel()
    private lateinit var dataBinding: ActivityDetailHospitalBinding
    var map: GoogleMap? = null
    var locationHospital: String ?= null
    var nameHospital: String ?= null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(detailModule)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Detail"
        
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_hospital)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val parameters = intent.extras?: Bundle()
            val id = parameters.getString(AppLink.DetailHospital.PARAM_HOSPITAL_ID)?: ""
            setupObserve(id)
        }

        dataBinding.rvMaps.setOnClickListener {
            val intent = Intent(this, MapsHospitalActivity::class.java)
            intent.putExtra("name", nameHospital)
            intent.putExtra("location", locationHospital)
            startActivity(intent)
        }
        
        
    }

    private fun setupObserve(id: String) {

        viewModel.getHospitalDetail(id).observe(this, Observer {response ->
            when(response){
                is ResponseState.Success -> {
                    setData(response.data)
                }
                is ResponseState.Error -> {
                    Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is ResponseState.Loading -> {
                }

            }
        })
    }

    private fun setData(data: Hospital) {
        with(dataBinding){
            tvName.text = data.name
            tvTelp.text = data.number_telp
            tvLocation.text = data.location

            nameHospital = data.name
            locationHospital = data.location


            Glide.with(this@DetailHospitalActivity)
                .load("${Constant.BASE_URL_IMAGE+data.image}")
                .into(imageView)
        }

        setupMaps(data)
        
    }

    private fun setupMaps(data: Hospital) {
        if (map != null) {
            map!!.clear()
        }

        val geoCoder = Geocoder(this)
        var address : List<Address>
        try{
            address = geoCoder.getFromLocationName(data.location, 5)
            if(address != null){
                val locationHospital = address[0]
                val latLng = LatLng(locationHospital.latitude, locationHospital.longitude)
                map?.addMarker(MarkerOptions().position(latLng)
                    .title(data.name))
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}