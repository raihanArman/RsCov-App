package id.co.rs_detail.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import id.co.data.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val useCase: UseCase): ViewModel() {
    fun getHospitalDetail(idHospital: String): LiveData<ResponseState<Hospital>> {
        return useCase.getHospitalById(idHospital).asLiveData()
    }

    fun getPathRouteMaps(url: String): LiveData<ResponseState<String>> {
        return useCase.getPathRouteMaps(url).asLiveData()
    }

}