package id.co.list_rs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import id.co.list_rs.databinding.ActivityHasilMetodeBinding
import id.co.list_rs.module.MethodModule.methodModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class HasilMetodeActivity : AppCompatActivity() {

    companion object{
        const val METODE_ASTAR = 98
        const val METODE_BELL = 99
    }
    private val adapter: HospitalAdapter by lazy{
        HospitalAdapter(this) { item ->
            goToDetail(item)
        }
    }

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var currentLocation: Location? = null

    private val viewModel: MethodViewModel by viewModel()
    private lateinit var dataBinding: ActivityHasilMetodeBinding
    private var metode: Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_hasil_metode)
        supportActionBar!!.title = ""
        loadKoinModules(methodModule)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupAdapter()
        setupPermission()

        metode = intent.getIntExtra("metode", 0)
        if(metode == METODE_ASTAR){
            dataBinding.tvTitle.text = "Hasil dari metode a star"
        }else{
            dataBinding.tvTitle.text = "Hasil dari metode a bellman ford"
        }
    }


    private fun setupPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                2121
            )
            return
        }
        fetchLocation()
    }


    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        val task: Task<Location> = fusedLocationProviderClient?.lastLocation as Task<Location>
        task.addOnSuccessListener {
            if (it != null){
                currentLocation = it
                val geoCoder = Geocoder(this)
                var address : List<Address>
                try{
                    address = geoCoder.getFromLocation(currentLocation!!.latitude, currentLocation!!.longitude, 5)
                    if(address != null){
                        val locationUser = address[0]
                        Log.d("Lokasi User", "fetchLocation: ${locationUser.getAddressLine(0)}")

                        if(metode == METODE_ASTAR){
                            getMethodAStar(locationUser.getAddressLine(0))
                        }else{
                            getMethodBellman(locationUser.getAddressLine(0))
                        }

                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getMethodBellman(addressLine: String?) {
        viewModel.getBellmanMethod(addressLine!!).observe(this, Observer {response ->
            when(response){
                is ResponseState.Success -> {
                    dataBinding.progressCircular.visibility = View.GONE
                    setData(response.data)
                }
                is ResponseState.Error -> {
                    dataBinding.progressCircular.visibility = View.GONE
                    Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is ResponseState.Loading -> {
                    dataBinding.progressCircular.visibility = View.VISIBLE
                }

            }
        })
    }

    private fun getMethodAStar(addressLine: String?) {
        viewModel.getAStarMethod(addressLine!!).observe(this, Observer {response ->
            when(response){
                is ResponseState.Success -> {
                    dataBinding.progressCircular.visibility = View.GONE
                    setData(response.data)
                }
                is ResponseState.Error -> {
                    dataBinding.progressCircular.visibility = View.GONE
                    Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is ResponseState.Loading -> {
                    dataBinding.progressCircular.visibility = View.VISIBLE
                }

            }
        })
    }

    private fun setData(data: List<Hospital>) {
        adapter.setListHospital(data)
    }
    private fun setupAdapter() {
        with(dataBinding){
            rvHospital.also{
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(this@HasilMetodeActivity)
                it.setHasFixedSize(true)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2121 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToDetail(hospital: Hospital){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("rscov://detail/${hospital.id}")))
    }

}