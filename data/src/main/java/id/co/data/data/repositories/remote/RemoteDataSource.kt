package id.co.data.data.repositories.remote

import id.co.data.data.model.Hospital
import id.co.data.data.network.ApiMapsService
import id.co.data.data.network.ApiService
import id.co.data.data.network.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource (
    private val apiService: ApiService,
    private val apiMapsService: ApiMapsService
){
    fun getHospital(): Flow<ResponseState<List<Hospital>>>{
        return flow {
            emit(ResponseState.Loading())
            try{
                val response = apiService.getHospital()
                val data = response.data
                if(data.isNotEmpty()){
                    emit(ResponseState.Success(data))
                }else{
                    emit(ResponseState.Empty)
                }
            }catch (e: Exception){
                emit(ResponseState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getHospitalById(idHospital: String): Flow<ResponseState<Hospital>>{
        return flow {
            emit(ResponseState.Loading())
            try{
                val response = apiService.getHospitalById(idHospital)
                val data = response.data
                if(data != null){
                    emit(ResponseState.Success(data))
                }else{
                    emit(ResponseState.Empty)
                }
            }catch (e: Exception){
                emit(ResponseState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getRouteMaps(url: String): Flow<ResponseState<String>>{
        return flow{
            emit(ResponseState.Loading())
            try{
                val response = apiMapsService.getPathRouteMaps(url)
                val data = response
                if(data != null){
                    emit(ResponseState.Success(data))
                }else{
                    emit(ResponseState.Empty)
                }
            }catch (e: Exception){
                emit(ResponseState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAStarMetode(locationUser: String): Flow<ResponseState<List<Hospital>>>{
        return flow {
            emit(ResponseState.Loading())
            try{
                val response = apiService.getAStarMethod(locationUser)
                val data = response.data
                if(data.isNotEmpty()){
                    emit(ResponseState.Success(data))
                }else{
                    emit(ResponseState.Empty)
                }
            }catch (e: Exception){
                emit(ResponseState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    fun getBellmanMetode(locationUser: String): Flow<ResponseState<List<Hospital>>>{
        return flow {
            emit(ResponseState.Loading())
            try{
                val response = apiService.getBellmanMethod(locationUser)
                val data = response.data
                if(data.isNotEmpty()){
                    emit(ResponseState.Success(data))
                }else{
                    emit(ResponseState.Empty)
                }
            }catch (e: Exception){
                emit(ResponseState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}