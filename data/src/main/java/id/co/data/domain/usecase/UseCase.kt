package id.co.data.domain.usecase

import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import kotlinx.coroutines.flow.Flow

interface UseCase {
    fun getHospital(): Flow<ResponseState<List<Hospital>>>
}