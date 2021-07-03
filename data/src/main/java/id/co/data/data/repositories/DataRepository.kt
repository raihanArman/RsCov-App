package id.co.data.data.repositories

import id.co.data.data.model.Hospital
import id.co.data.data.network.ResponseState
import id.co.data.data.repositories.remote.RemoteDataSource
import id.co.data.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class DataRepository(
    private val remote: RemoteDataSource
): Repository {
    override fun getHopital(): Flow<ResponseState<List<Hospital>>> {
        return remote.getHospital()
    }

}