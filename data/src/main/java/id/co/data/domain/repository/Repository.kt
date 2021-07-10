package id.co.data.domain.repository

import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getHopital(): Flow<ResponseState<List<Hospital>>>
    fun getHospitalById(idHospital: String): Flow<ResponseState<Hospital>>
    fun getPathRouteMaps(url: String): Flow<ResponseState<String>>
    fun getAStarMethod(locationUser: String, latitude: String, longtitude: String): Flow<ResponseState<List<Hospital>>>
    fun getBellmanMethod(locationUser: String, latitude: String, longtitude: String): Flow<ResponseState<List<Hospital>>>
}