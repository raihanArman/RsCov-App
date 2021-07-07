package id.co.data.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import id.co.data.data.network.ApiMapsService
import id.co.data.data.network.ApiService
import id.co.data.data.repositories.DataRepository
import id.co.data.data.repositories.remote.RemoteDataSource
import id.co.data.domain.repository.Repository
import id.co.data.util.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object CoreModule {
    val networkModule = module{
        single {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build()
        }
        single {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
            retrofit.create(ApiService::class.java)
        }

        single {
            val retrofitMaps = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(get())
                .build()
            retrofitMaps.create(ApiMapsService::class.java)
        }
    }

    val repositoryModule = module{
        single {
            RemoteDataSource(get(), get())
        }
        single<Repository> {
            DataRepository(
                get()
            )
        }
    }

}