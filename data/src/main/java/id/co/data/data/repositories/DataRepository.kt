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

    override fun getHospitalById(idHospital: String): Flow<ResponseState<Hospital>> {
        return remote.getHospitalById(idHospital)
    }

    override fun getPathRouteMaps(url: String): Flow<ResponseState<String>> {
        return remote.getRouteMaps(url)
    }

    override fun getAStarMethod(locationUser: String, latitude: String, longtitude: String): Flow<ResponseState<List<Hospital>>> {
        return remote.getAStarMetode(locationUser, latitude, longtitude)
    }

    override fun getBellmanMethod(locationUser: String, latitude: String, longtitude: String): Flow<ResponseState<List<Hospital>>> {
        return remote.getBellmanMetode(locationUser, latitude, longtitude)
    }

}