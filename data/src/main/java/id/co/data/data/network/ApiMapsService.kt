package id.co.data.data.network

import retrofit2.http.GET
import retrofit2.http.Url

interface ApiMapsService {
    @GET
    suspend fun getPathRouteMaps(
        @Url url: String
    ): String
}