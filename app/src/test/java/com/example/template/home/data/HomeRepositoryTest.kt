package com.example.template.home.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import com.example.template.home.data.remote.HomeRemoteDataSource
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.*
import com.example.template.testutils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()


    @RelaxedMockK
    private lateinit var homeDataSource: HomeRemoteDataSource

    private lateinit var homeRepository: HomeRepository

    private lateinit var userRes: UserRes
    private lateinit var articles: List<Article>
    private lateinit var comments: List<Comment>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        initGetUserUseCase()
        initGetArticleUseCase()
        initGetCommentsUseCase()
    }

    @Test
    fun `Verify Get User`() = runBlocking {
        homeRepository = HomeRepository(homeDataSource)
        coEvery { homeDataSource.getUsers(1) } returns userRes
        val result = homeRepository.getUsers(1)

        assertEquals(result, userRes)
    }

    @Test
    fun `Verify Get User FLow`() = runBlocking {
        val userFlow = flow {
            emit(userRes)
        }

        homeRepository = HomeRepository(homeDataSource)
        coEvery { homeDataSource.getUsersFlow(1) } returns userFlow
        val result = homeRepository.getUsersFlow(1)

        assertEquals(result, userFlow)
    }

    @Test
    fun `Verify Get Articles`() = runBlocking {
        homeRepository = HomeRepository(homeDataSource)
        coEvery { homeDataSource.getArticle(1) } returns articles
        val result = homeRepository.getArticles(1)

        assertEquals(result, articles)
    }

    @Test
    fun `Verify Get Comments`() = runBlocking {
        homeRepository = HomeRepository(homeDataSource)
        coEvery { homeDataSource.getComments("1") } returns comments
        val result = homeRepository.getComments("1")

        assertEquals(result, comments)
    }

    @Test
    fun `Verify Get User Live Data`() {
        val userLivaData = liveData {
            emit(userRes)
        }
        homeRepository = HomeRepository(homeDataSource)
        coEvery { homeDataSource.getUsersLiveData(1) } returns userLivaData
        val result = homeRepository.getUsersLiveData(1)

        assertEquals(result.value, userLivaData.value)

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