package com.example.template.home.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.example.template.core.livatask.liveTask
import com.example.template.home.data.servicemodels.*
import com.example.template.home.domain.*
import com.example.template.testutils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    lateinit var getUserUseCase: GetUserUseCase

    @MockK(relaxed = true)
    lateinit var getCommentUseCase: GetCommentUseCase

    @MockK(relaxed = true)
    lateinit var getArticleUseCase: GetArticleUseCase

    @MockK(relaxed = true)
    lateinit var getUserFlowUseCase: GetUserFlowUseCase

    @MockK(relaxed = true)
    lateinit var testUseCase: TestUseCase

    lateinit var homeViewModel: HomeViewModel

    private lateinit var userRes: UserRes
    private lateinit var articles: List<Article>
    private lateinit var comments: List<Comment>

    private val userFlow = liveTask {
        emit(Result.Success(userRes))
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        initGetUserUseCase()
        initGetArticleUseCase()
        initGetCommentsUseCase()

        coEvery { getUserUseCase(1) } returns Result.Success(userRes)
        coEvery { getCommentUseCase("1") } returns Result.Success(comments)
        coEvery { getArticleUseCase(1) } returns Result.Success(articles)
        coEvery { getUserFlowUseCase.asLiveTask(5) } returns userFlow

        homeViewModel = HomeViewModel(
            getUserUseCase,
            getArticleUseCase,
            getCommentUseCase,
            getUserFlowUseCase,
            testUseCase
        )

    }

    @Test
    fun `Verify Get User`() = runBlocking {
        val user = homeViewModel.user1.run()
        delay(200L)
        assertTrue((user.result() as Result.Success).data.data.size == 3)
    }

    @Test
    fun `Verify Get Articles`() = runBlocking {
        val articles = homeViewModel.articles.run()
        delay(200L)
        assertTrue((articles.result() as Result.Success).data.size == 5)
    }

    @Test
    fun `Verify Get Comments`() = runBlocking {
        val comments = homeViewModel.comments.run()
        delay(200L)
        assertTrue((comments.result() as Result.Success).data.size == 4)
    }

    @Test
    fun `Verify Get User Flow`() = runBlocking {
        val userFlow = homeViewModel.user2.run()
        delay(200L)
        assertTrue((userFlow.result() as Result.Success).data.data.size == 3)
    }

    private fun initGetUserUseCase() {
        userRes =
            UserRes(
                Ad("part", "test", "test.ir"),
                listOf(
                    Data("test avatar 1", "test email 1", "mehran", 1, "sf"),
                    Data("test avatar 2", "test email 2", "asghar", 2, "ls"),
                    Data("test avatar 3", "test email 3", "mamad", 3, "hf")
                ),
                1, 1, 1, 1
            )
    }

    private fun initGetArticleUseCase() {
        articles = listOf(
            Article("1", "test title 1", "test body 1", "john", 2018, true),
            Article("2", "test title 2", "test body 2", "amir", 2018, false),
            Article("3", "test title 3", "test body 3", "ali", 2018, true),
            Article("4", "test title 4", "test body 4", "mahdi", 2018, false),
            Article("5", "test title 5", "test body 5", "reza", 2018, true)
        )
    }

    private fun initGetCommentsUseCase() {
        comments = listOf(
            Comment("1", "test body", "sajjad", 2019, false),
            Comment("2", "test body 2", "ali", 2017, true),
            Comment("3", "test body 3", "ali reza", 2014, false),
            Comment("4", "test body 4", "mamad", 2016, true)
        )

    }

}