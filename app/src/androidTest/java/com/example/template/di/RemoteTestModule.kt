package com.example.template.di

import com.example.template.ArticleServiceImpl
import com.example.template.CommentServiceImpl
import com.example.template.UserServiceImpl
import com.example.template.core.util.Constants
import com.example.template.home.data.remote.ArticleDataSource
import com.example.template.home.data.remote.CommentDataSource
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
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object RemoteTestModule {
    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

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


    @Provides
    fun provideRetrofit(
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .build()

    @Provides
    @Named("user_service_api")
    fun provideDemoUserServiceApi(retrofit: Retrofit): UserDataSource = UserServiceImpl(retrofit)

    @Provides
    fun provideDemoArticleServiceApi(retrofit: Retrofit): ArticleDataSource =
        ArticleServiceImpl(retrofit)


    @Provides
    fun provideDemoCommentServiceApi(retrofit: Retrofit): CommentDataSource =
        CommentServiceImpl(retrofit)

}