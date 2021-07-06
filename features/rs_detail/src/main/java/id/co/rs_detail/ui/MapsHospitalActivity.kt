package id.co.rs_detail.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import id.co.data.data.network.ResponseState
import id.co.rs_detail.R
import id.co.rs_detail.databinding.ActivityMapsHospitalBinding
import id.co.rs_detail.module.DetailModule
import id.co.rs_detail.util.DirectionJSONParser
import id.co.rs_detail.util.ViewWeightAnimationWrapper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class MapsHospitalActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: DetailViewModel by viewModel()
    private lateinit var dataBinding: ActivityMapsHospitalBinding

    var latLngHospital : LatLng?= null
    private var mMap: GoogleMap? = null
    var mLastLocation: Location? = null

    var locationHospital: String? = null
    var nameHospital: String? = null
    var progressDialog: ProgressDialog? = null
    private val MY_PERMISSION_REQUEST_CODE = 732

    val UPDATE_INTERVAL = 5000
    val FASTEST_INTERVAL = 5000
    val DISPLACEMENT = 10

    private val MAP_LAYOUT_STATE_CONTRACTED = 0
    private val MAP_LAYOUT_STATE_EXPANDED = 1
    private var mMapLayoutState = 0

    private var mLocationReqeust: LocationRequest? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationCallback: LocationCallback? = null


    var mCurrentMarker: Marker? = null
    private var directionTracking: Polyline? = null
    var displayRute = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_maps_hospital)
        loadKoinModules(DetailModule.detailModule)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_studio) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        nameHospital = intent.getStringExtra("name")
        locationHospital = intent.getStringExtra("location")
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("GPS Loading ...")


        dataBinding.tvHospital.text = nameHospital
        dataBinding.tvLokasi.text = locationHospital

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        dataBinding.fbFullScreen.setOnClickListener(View.OnClickListener {
            if (mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED) {
                mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED
                expandMapAnimation()
            } else if (mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED) {
                mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED
                contractMapAnimation()
            }
        })

    }

    private fun expandMapAnimation() {
        val mapAnimationWrapper = ViewWeightAnimationWrapper(dataBinding.mapContainer)
        val mapAnimation: ObjectAnimator = ObjectAnimator.ofFloat(mapAnimationWrapper,
            "weight",
            70f,
            100f)
        mapAnimation.duration = 800
        val lvInfoEventAnimationWrapper = ViewWeightAnimationWrapper(dataBinding.lvInfoHospital)
        val infoAnimation: ObjectAnimator = ObjectAnimator.ofFloat(
            lvInfoEventAnimationWrapper,
            "weight",
            30f,
            0f
        )
        infoAnimation.duration = 800
        mapAnimation.start()
        infoAnimation.start()
    }

    private fun contractMapAnimation() {
        val mapAnimationWrapper = ViewWeightAnimationWrapper(dataBinding.mapContainer)
        val mapAnimation: ObjectAnimator = ObjectAnimator.ofFloat(mapAnimationWrapper,
            "weight",
            100f,
            70f)
        mapAnimation.duration = 800
        val lvInfoEventAnimationWrapper = ViewWeightAnimationWrapper(dataBinding.lvInfoHospital)
        val infoAnimation: ObjectAnimator = ObjectAnimator.ofFloat(lvInfoEventAnimationWrapper,
            "weight",
            0f,
            30f)
        infoAnimation.duration = 800
        mapAnimation.start()
        infoAnimation.start()
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.isTrafficEnabled = false
        mMap!!.isIndoorEnabled = false
        mMap!!.isBuildingsEnabled = false
        mMap!!.uiSettings.isZoomControlsEnabled = true


        buildLocationRequest()
        buildLocationCallback()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(mLocationReqeust, locationCallback, Looper.myLooper())

        setUpLocation()
        displayLocation()
    }

    fun displayLocation(): Unit {
        Log.d("MAPS MANTAO", "displayLocation: 13")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        Log.d("MAPS MANTAO", "displayLocation: 1")
        fusedLocationProviderClient!!.lastLocation
            .addOnSuccessListener { location ->
                mLastLocation = location
                if (mLastLocation != null) {
                    Log.d("MAPS MANTAO", "displayLocation: tidak null las location")
                    val latitudeUser: Double = mLastLocation!!.getLatitude()
                    val longitudeUser: Double = mLastLocation!!.getLongitude()
                    if (mCurrentMarker != null) {
                        mCurrentMarker!!.remove()
                    }
                    mMap!!.clear()
                    val builder = LatLngBounds.Builder()
                    builder.include(LatLng(latitudeUser, longitudeUser))
                    mCurrentMarker = mMap!!.addMarker(
                        MarkerOptions()
                        .position(LatLng(latitudeUser, longitudeUser))
                        .title("Your location"))
                    val geocoder = Geocoder(this)
                    val addresses: List<Address>
                    try {
                        addresses = geocoder.getFromLocationName(locationHospital, 5)
                        if (addresses.size > 0) {
                            val locationPangkalan = addresses[0]
                            latLngHospital = LatLng(locationPangkalan.latitude, locationPangkalan.longitude)
                            mMap!!.addMarker(
                                MarkerOptions().position(latLngHospital!!)
                                .icon(
                                    BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

                            val width = resources.displayMetrics.widthPixels
                            val height = resources.displayMetrics.heightPixels
                            val padding = (Math.min(width, height) * 0.2).toInt()
                            builder.include(latLngHospital)
                            val bounds = builder.build()
                            //                                                int padding = 500; // offset from edges of the map in pixels
                            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
                            mMap!!.moveCamera(cu)
                            mMap!!.animateCamera(cu)
                            directionTracking?.remove()

                            val requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                                    "mode=driving&" +
                                    "transit_routing_preference=less_driving&" +
                                    "origin=" + latitudeUser + "," + longitudeUser + "&" +
                                    "destination=" + latLngHospital?.latitude + "," + latLngHospital?.longitude + "&" +
                                    "key=" + resources.getString(R.string.google_direction_key)
                            Log.d("MANTAP DJIWA URL : ", requestApi)
                            viewModel.getPathRouteMaps(requestApi).observe(this, Observer { response ->
                                when(response){
                                    is ResponseState.Loading ->{

                                    }
                                    is ResponseState.Error ->{

                                    }
                                    is ResponseState.Success ->{
                                        response.data?.let {
                                            getDirection(it)
                                        }
                                    }
                                }
                            })

                        }
                    } catch (e: IOException) {
                        Log.d("ERROR ROUTE", "displayLocation: $e")
                    }
                } else {
                    Log.d("MAPS MANTAO", "displayLocation: null las location")
                    Log.d("ERROR", "displayLocation: Cannot get your location")
                }
            }
    }

    private fun getDirection(it: String) {
        val jsonObject = JSONObject(it)
        val routes: JSONArray = jsonObject.getJSONArray("routes")
        val `object` = routes.getJSONObject(0)
        val legs = `object`.getJSONArray("legs")
        val objectLegs = legs.getJSONObject(0)

        val distance = objectLegs.getJSONObject("distance")
        val distanceText = distance.getString("text")


        val time = objectLegs.getJSONObject("duration")
        val timeText = time.getString("text")

        dataBinding.tvJarak.setText(distanceText)
        dataBinding.tvWaktu.setText(timeText)

        progressDialog!!.dismiss()
        ParserTask(this).execute(it)

    }

    private fun setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), MY_PERMISSION_REQUEST_CODE)
        } else {
            buildLocationCallback()
            buildLocationRequest()
        }
    }

    private fun buildLocationRequest() {
        mLocationReqeust = LocationRequest()
        mLocationReqeust?.setInterval(UPDATE_INTERVAL.toLong())
        mLocationReqeust?.setFastestInterval(FASTEST_INTERVAL.toLong())
        mLocationReqeust?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationReqeust?.setSmallestDisplacement(DISPLACEMENT.toFloat())
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    mLastLocation = location
                }
            }
        }
    }

    inner class ParserTask(val context: Context) :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
        var mDialog = ProgressDialog(context)
        override fun onPreExecute() {
            super.onPreExecute()
            mDialog.setMessage("Please waiting ...")
            mDialog.show()
        }


        override fun onPostExecute(lists: List<List<HashMap<String, String>>>?) {
            super.onPostExecute(lists)
            mDialog.dismiss()
            var points: ArrayList<LatLng>? = null
            var polylineOptions: PolylineOptions? = null
            for (i in lists!!.indices) {
                points = ArrayList<LatLng>()
                polylineOptions = PolylineOptions()
                val path =
                    lists[i]
                for (k in path.indices) {
                    val point = path[k]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                polylineOptions.addAll(points)
                polylineOptions.width(10f)
                polylineOptions.color(Color.RED)
                polylineOptions.geodesic(true)
            }
            if (polylineOptions != null) {
                directionTracking = mMap?.addPolyline(polylineOptions)

            }
        }

        override fun doInBackground(vararg p0: String?): List<List<HashMap<String, String>>>? {
            val jsonObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? =
                null
            try {
                jsonObject = JSONObject(p0[0])
                val parser: DirectionJSONParser =
                    DirectionJSONParser()
                routes = parser.parse(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return routes
        }
    }
}