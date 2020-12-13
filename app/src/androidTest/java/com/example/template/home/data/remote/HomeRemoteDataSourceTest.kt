package com.example.template.home.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class HomeRemoteDataSourceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("user_service_api")
    lateinit var userServiceApi: UserDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun getUserListFromMockFakeDataSource() {
        userServiceApi.getUsers()
    }

}