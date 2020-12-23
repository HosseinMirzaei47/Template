package com.example.template.home.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.LiveTaskResult
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Ad
import com.example.template.home.data.servicemodels.Data
import com.example.template.home.data.servicemodels.UserRes
import com.example.template.testutils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//
class GetUserUseCaseTest {
    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userRes: UserRes

    @Before
    fun setup() {
        userRes =
            UserRes(
                Ad("part", "test", "test.ir"),
                listOf(
                    Data("s", "s", "s.txt", 5, "sf"),
                    Data("b", "b", "sa", 4, "l")
                ),
                1, 1, 1, 1
            )
    }


    @Test
    fun loadUserList() = runBlocking {

        val homeRepository = mockk<HomeRepository>(relaxed = true)
        val getUserUseCase = GetUserUseCase(
            homeRepository, coroutineRule.testDispatcher
        )
        coEvery { homeRepository.getUsers(10) } returns userRes
        val userData = getUserUseCase(10)
        assertTrue((userData as LiveTaskResult.Success).data.data.size == 2)


    }
}

