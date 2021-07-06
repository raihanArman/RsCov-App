package id.co.data.domain.iterator

import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import id.co.data.domain.repository.Repository
import id.co.data.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class Iterator (
    private val repository: Repository
): UseCase {
    override fun getHospital(): Flow<ResponseState<List<Hospital>>> = repository.getHopital()
    override fun getHospitalById(idHospital: String): Flow<ResponseState<Hospital>> = repository.getHospitalById(idHospital)
    override fun getPathRouteMaps(url: String): Flow<ResponseState<String>> = repository.getPathRouteMaps(url)

}