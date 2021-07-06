package id.co.data.domain.usecase

import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import kotlinx.coroutines.flow.Flow

interface UseCase {
    fun getHospital(): Flow<ResponseState<List<Hospital>>>
    fun getHospitalById(idHospital: String): Flow<ResponseState<Hospital>>
    fun getPathRouteMaps(url: String): Flow<ResponseState<String>>
}