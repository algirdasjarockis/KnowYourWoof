package com.ajrock.knowyourwoof

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.ajrock.knowyourwoof.data.IScoresRepository
import com.ajrock.knowyourwoof.data.OfflineScoresRepository
import com.ajrock.knowyourwoof.data.ScoreDatabase
import com.ajrock.knowyourwoof.data.api.IDogCeoApiService
import com.ajrock.knowyourwoof.data.repository.DogRepository
import com.ajrock.knowyourwoof.data.repository.IDogRepository
import com.ajrock.knowyourwoof.viewmodel.QuizViewModel
import com.ajrock.knowyourwoof.viewmodel.StatsViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class App: Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.15)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.05)
                    .directory(cacheDir)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }
}

internal val appModule = module {
    single<IScoresRepository> {
        OfflineScoresRepository(ScoreDatabase.getDatabase(androidContext()).scoreDao())
    }

    single<IDogCeoApiService> {
        val client = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .cache(
                Cache(
                    directory = File(androidApplication().cacheDir, "http_cache"),
                    maxSize = 20L * 1024L * 1024L
                )
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(IDogCeoApiService::class.java)
    }

    single<IDogRepository> {
        DogRepository(get())
    }

    viewModel<QuizViewModel>()
    viewModel<StatsViewModel>()
}