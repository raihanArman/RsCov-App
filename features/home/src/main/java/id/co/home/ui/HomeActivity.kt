package id.co.home.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.deeplinkdispatch.DeepLink
import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import id.co.data.util.AppLink
import id.co.data.util.Constant
import id.co.home.R
import id.co.home.databinding.ActivityHomeBinding
import id.co.home.module.HomeModule.homeModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@DeepLink(AppLink.Home.HOME_LINK)
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var dataBinding: ActivityHomeBinding

    private val adapter: HospitalAdapter by lazy{
        HospitalAdapter(this) { item ->
            goToDetail(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(homeModule)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

//        dataBinding.tvText.setOnClickListener {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("rscov://list")))
//        }

        setupAdapter()
        setupObserve()

        dataBinding.fbSearch.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,  Uri.parse("rscov://list")))
        }

    }

    private fun setupObserve() {
        viewModel.getHospital.observe(this, Observer {response ->
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

    private fun setupAdapter() {
        with(dataBinding){
            rvHospital.also{
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(this@HomeActivity)
                it.setHasFixedSize(true)
            }
        }
    }

    private fun goToDetail(hospital: Hospital){

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("rscov://detail/${hospital.id}")))
    }

    private fun setData(data: List<Hospital>) {
        adapter.setListHospital(data)
    }

}