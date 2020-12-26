package com.example.template.home.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.LiveTaskResult
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Comment
import com.example.template.testutils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCommentUseCaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var commentsList: List<Comment>

    @Before
    fun setup() {
        commentsList = listOf(
            Comment("1", "test body", "sajjad", 2019, false),
            Comment("2", "test body 2", "ali", 2017, true),
            Comment("3", "test body 3", "ali reza", 2014, false)
        )
    }

    @Test
    fun loadUserList() = runBlocking {

        val homeRepository = mockk<HomeRepository>(relaxed = true)
        val getCommentUseCase = GetCommentUseCase(
            homeRepository, coroutineRule.testDispatcher
        )
        coEvery { homeRepository.getComments("1") } returns commentsList
        val commentData = getCommentUseCase("1")
        assertTrue((commentData as LiveTaskResult.Success).data.size == 3)


    }
}