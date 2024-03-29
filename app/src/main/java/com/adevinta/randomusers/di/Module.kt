package com.adevinta.randomusers.di

import androidx.room.Room
import com.adevinta.randomusers.BuildConfig
import com.adevinta.randomusers.allusers.factory.AllUsersFactory
import com.adevinta.randomusers.allusers.factory.AllUsersFactoryImpl
import com.adevinta.randomusers.allusers.repository.AllUsersRepository
import com.adevinta.randomusers.allusers.repository.AllUsersRepositoryImpl
import com.adevinta.randomusers.allusers.viewmodel.AllUsersViewModel
import com.adevinta.randomusers.allusers.viewmodel.AllUsersViewModelImpl
import com.adevinta.randomusers.common.database.AllUsersDataBase
import com.adevinta.randomusers.common.network.RandomUsersApiService
import com.adevinta.randomusers.singleuser.factory.SingleUserFactory
import com.adevinta.randomusers.singleuser.factory.SingleUserFactoryImpl
import com.adevinta.randomusers.singleuser.repository.SingleUserRepository
import com.adevinta.randomusers.singleuser.repository.SingleUserRepositoryImpl
import com.adevinta.randomusers.singleuser.viewmodel.SingleUserViewModel
import com.adevinta.randomusers.singleuser.viewmodel.SingleUserViewModelImpl
import com.adevinta.randomusers.splash.factory.SplashFactory
import com.adevinta.randomusers.splash.factory.SplashFactoryImpl
import com.adevinta.randomusers.splash.viewmodel.SplashViewModel
import com.adevinta.randomusers.splash.viewmodel.SplashViewModelImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun injectModule() = loadModule

private val loadModule by lazy {
    loadKoinModules(
        listOf(
            viewModelModule,
            factoryModule,
            repositoryModule,
            database
        )
    )
}

private val serviceProvider = initRetrofit()

val viewModelModule: Module = module {
    viewModel {
        SplashViewModelImpl(factory = get())
    } bind SplashViewModel::class
    viewModel {
        AllUsersViewModelImpl(factory = get())
    } bind AllUsersViewModel::class
    viewModel {
        SingleUserViewModelImpl(factory = get())
    } bind SingleUserViewModel::class
}

val factoryModule: Module = module {
    single { SplashFactoryImpl() } bind SplashFactory::class
    single { AllUsersFactoryImpl(repository = get()) } bind AllUsersFactory::class
    single { SingleUserFactoryImpl(repository = get()) } bind SingleUserFactory::class
}

val repositoryModule: Module = module {
    single { AllUsersRepositoryImpl(serviceProvider, database = get()) } bind AllUsersRepository::class
    single { SingleUserRepositoryImpl(database = get()) } bind SingleUserRepository::class
}

val database: Module = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AllUsersDataBase::class.java,
            "allusers.db"
        ).build()
    }
}

fun initRetrofit(): RandomUsersApiService {
    val client = OkHttpClient.Builder().apply {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(interceptor)
        addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl.newBuilder().build()

            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        })
    }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(RandomUsersApiService::class.java)
}