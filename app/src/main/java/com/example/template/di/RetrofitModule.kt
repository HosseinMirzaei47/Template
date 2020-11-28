package com.example.template.di

import com.example.template.home.data.remote.UserService
import com.example.template.UserServiceImpl
import com.example.template.core.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor
    ) =
        OkHttpClient.Builder().addNetworkInterceptor(Interceptor { chain -> chain.proceed(request = chain.request()) }).addInterceptor { chain ->
            val newBuilder = chain.request().newBuilder()
            val request = newBuilder.build()
            chain.proceed(request)
        }.addNetworkInterceptor(logging)
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .build()

    @Singleton
    @Provides
    fun provideDemoServiceApi(retrofit: Retrofit) : UserService = UserServiceImpl(retrofit)
}
