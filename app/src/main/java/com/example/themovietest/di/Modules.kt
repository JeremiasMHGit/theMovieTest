package com.example.themovietest.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.themovietest.BuildConfig
import com.example.themovietest.model.local.roomdb.DatabaseRepository
import com.example.themovietest.model.local.roomdb.DatabaseRepositoryImp
import com.example.themovietest.model.local.roomdb.MovieDatabase
import com.example.themovietest.model.local.sharedpref.SharedPreferenceHelper
import com.example.themovietest.model.local.sharedpref.SharedPreferenceHelperImp
import com.example.themovietest.model.remote.Repository
import com.example.themovietest.model.remote.RepositoryImp
import com.example.themovietest.model.remote.network.APIService
import com.example.themovietest.model.remote.network.NetworkInterceptorConnection
import com.example.themovietest.model.remote.network.RemoteRepository
import com.example.themovietest.model.remote.network.RemoteRepositoryImp
import com.example.themovietest.viewmodel.MoviesViewModel
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val repositoryModule = module {
    single<RemoteRepository> { RemoteRepositoryImp(get()) }
    single<SharedPreferenceHelper> { SharedPreferenceHelperImp(get()) }
    single<DatabaseRepository> { DatabaseRepositoryImp(get()) }
    single<Repository> {
        RepositoryImp(
            remoteRepository = get(),
            sharedPreferenceRepository = get(),
            databaseRepository = get()
        )
    }
}

val viewModelModule = module {
    viewModel { MoviesViewModel(get()) }
    //viewModel { UpcomingMoviesViewModel(get()) }
    //viewModel { TopRatedMoviesViewModel(get()) }
    //viewModel { FavoriteMoviesViewModel(get()) }
    //viewModel { (movie_id: Int) -> MovieOverviewViewModel(get(), movie_id) }
}

val apiServiceModule = module {
    fun getApiServiceModule(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }
    single { getApiServiceModule(get()) }
}

val retrofitModule = module {

    fun provideCacheFile(context: Context): File {
        return File(context.cacheDir, "okHttp_cache")
    }

    fun provideCache(cacheFile: File): Cache {
        return Cache(cacheFile, 3 * 1024 * 1024)
    }

    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    fun provideNetworkConnInterceptor(context: Context): NetworkInterceptorConnection {
        return NetworkInterceptorConnection(context)
    }

    fun provideHttpClient(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: NetworkInterceptorConnection
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(networkInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .client(client)
            .build()
    }

    single { provideCacheFile(androidContext()) }
    single { provideCache(get()) }
    single { provideLoggingInterceptor() }
    single { provideNetworkConnInterceptor(androidContext()) }

    single {
        provideHttpClient(
            cache = get(),
            loggingInterceptor = get(),
            networkInterceptor = get()
        )
    }
    single { provideRetrofit(client = get()) }
}

val databaseModule = module {
    fun getMovieDatabaseInstance(application: Context): MovieDatabase {
        return Room.databaseBuilder(application, MovieDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { getMovieDatabaseInstance(androidContext()) }
}
