package id.co.data.data.network

import id.co.data.data.model.Hospital
import id.co.data.data.response.ResponseData
import retrofit2.http.GET

interface ApiService {
    @GET("tampil_rs.php")
    suspend fun getHospital(): ResponseData<List<Hospital>>
}