package id.co.data.data.network

import id.co.data.data.model.Hospital
import id.co.data.data.response.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("tampil_rs.php")
    suspend fun getHospital(): ResponseData<List<Hospital>>

    @GET("tampil_rs_by_id.php")
    suspend fun getHospitalById(
        @Query("id_rs") idHospital: String
    ): ResponseData<Hospital>

    @GET("astar_metode.php")
    suspend fun getAStarMethod(
        @Query("location_user") locationUser: String,
        @Query("lat_user") latUser: String,
        @Query("long_user") longUser: String
    ): ResponseData<List<Hospital>>

    @GET("bellman_metode.php")
    suspend fun getBellmanMethod(
        @Query("location_user") locationUser: String,
        @Query("lat_user") latUser: String,
        @Query("long_user") longUser: String
    ): ResponseData<List<Hospital>>

}