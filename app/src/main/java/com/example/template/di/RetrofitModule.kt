package com.example.template.di

import com.example.template.ArticleServiceImpl
import com.example.template.CommentServiceImpl
import com.example.template.UserServiceImpl
import com.example.template.core.util.Constants.BASE_URL
import com.example.template.home.data.remote.ArticleDataSource
import com.example.template.home.data.remote.CommentDataSource
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.remote.UserDataSource
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
        OkHttpClient.Builder()
            .addNetworkInterceptor(Interceptor { chain -> chain.proceed(request = chain.request()) })
            .addInterceptor { chain ->
                val newBuilder = chain.request().newBuilder()
                val request = newBuilder.build()
                chain.proceed(request)
            }.addNetworkInterceptor(logging)
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): HomeApi = retrofit.create(HomeApi::class.java)

    @Singleton
    @Provides
    fun provideDemoUserServiceApi(homeApi: HomeApi): UserDataSource = UserServiceImpl(homeApi)

    @Singleton
    @Provides
    fun provideDemoArticleServiceApi(homeApi: HomeApi): ArticleDataSource =
        ArticleServiceImpl(homeApi)

    @Singleton
    @Provides
    fun provideDemoCommentServiceApi(homeApi: HomeApi): CommentDataSource =
        CommentServiceImpl(homeApi)
}
