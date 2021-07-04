package id.co.data.data.repositories.remote

import id.co.data.data.model.Hospital
import id.co.data.data.network.ApiService
import id.co.data.data.network.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource (
    private val apiService: ApiService
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
}