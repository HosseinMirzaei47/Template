package com.example.template.home.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Article
import com.example.template.testutils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetArticleUseCaseTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var articlesList: List<Article>

    @Before
    fun setup() {
        articlesList = listOf(
            Article("1", "test title 1", "test body 1", "john", 2018, true),
            Article("2", "test title 2", "test body 2", "amir", 2018, false),
            Article("3", "test title 3", "test body 3", "ali", 2018, true),
            Article("4", "test title 4", "test body 4", "mahdi", 2018, false),
            Article("5", "test title 5", "test body 5", "reza", 2018, true)
        )
    }

    @Test
    fun loadUserList() = runBlocking {

        val homeRepository = mockk<HomeRepository>(relaxed = true)
        val getArticleUseCase = GetArticleUseCase(
            homeRepository, coroutineRule.testDispatcher
        )
        coEvery { homeRepository.getArticles(1) } returns articlesList
        val articlesData = getArticleUseCase(1)
        Assert.assertTrue((articlesData as Result.Success).data.size == 5)


    }
}