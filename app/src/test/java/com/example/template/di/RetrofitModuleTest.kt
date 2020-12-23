package com.example.template.di

import com.example.template.core.util.Constants.BASE_URL
import com.example.template.home.data.remote.HomeApi
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class RetrofitModuleTest {

    private lateinit var retrofitModule: RetrofitModule

    @Before
    fun setUp() {
        retrofitModule = RetrofitModule
    }

    @Test
    fun `verify Provided HttpLoggingInterceptor`() {
        val interceptor = retrofitModule.provideLoggingInterceptor()
        assertEquals(HttpLoggingInterceptor.Level.BODY, interceptor.level)
    }

    @Test
    fun `verify Provided Http Client`() {
        val interceptor = mockk<HttpLoggingInterceptor>()
        val httpClient = retrofitModule.provideOkHttpClient(interceptor)

        assertEquals(1, httpClient.interceptors.size)
        assertEquals(2, httpClient.networkInterceptors.size)

    }

    @Test
    fun `verify Provided Retrofit Builder`() {
        val retrofit =
            retrofitModule.provideRetrofit(retrofitModule.provideOkHttpClient(retrofitModule.provideLoggingInterceptor()))

        assertEquals(BASE_URL, retrofit.baseUrl().toUrl().toString())
    }

    @Test
    fun `verify Provided Home Api`() {
        val retrofit = mockk<Retrofit>()
        val homeApi = mockk<HomeApi>()
        val serviceClassCapture = slot<Class<*>>()

        every { retrofit.create<HomeApi>(any()) } returns homeApi

        retrofitModule.provideUserApi(retrofit)

        verify { retrofit.create(capture(serviceClassCapture)) }
        assertEquals(HomeApi::class.java, serviceClassCapture.captured)
    }


}