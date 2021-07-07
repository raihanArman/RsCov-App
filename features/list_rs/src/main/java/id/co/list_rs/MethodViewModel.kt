package id.co.list_rs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import id.co.data.domain.usecase.UseCase

class MethodViewModel(val usecase: UseCase): ViewModel() {
    fun getAStarMethod(locationUser: String): LiveData<ResponseState<List<Hospital>>> {
        return usecase.getAStarMethod(locationUser).asLiveData()
    }

    fun getBellmanMethod(locationUser: String): LiveData<ResponseState<List<Hospital>>> {
        return usecase.getBellmanMethod(locationUser).asLiveData()
    }

}