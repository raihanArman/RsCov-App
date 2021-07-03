package id.co.data.domain.repository

import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getHopital(): Flow<ResponseState<List<Hospital>>>
}