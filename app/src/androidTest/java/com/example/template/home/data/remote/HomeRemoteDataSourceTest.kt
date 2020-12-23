package com.example.template.home.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Article
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class HomeRemoteDataSourceTest {


    val sampleArticle = Article(
        "1",
        "dini",
        "darse dini ziba nistziba nistziba nistziba nistziba nist ",
        "mamd",
        13,
        true
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var userServiceApi: UserDataSource

    @Inject
    lateinit var articleServiceApi: ArticleDataSource

    @Inject
    lateinit var commentServiceApi: CommentDataSource

    @Inject
    lateinit var homeRepository: HomeRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun getUserListFromMockFakeDataSource_iteSizeMustBeEqualToThree() = runBlockingTest {
        val result = userServiceApi.getUsers(1)
        assertTrue(result.data.size == 3)
    }

    @Test
    fun getArticlesFromMockDataSource_iteSizeMustBeEqualToSeventeen() = runBlockingTest {
        val result = articleServiceApi.getArticle(1)
        assertFalse(result.size != 17)
    }

    @Test
    fun getArticlesFromMockDataSource_mustContainArticleThatDefineTopOfClass() = runBlockingTest {
        val result = articleServiceApi.getArticle(1)
        assertThat(result).contains(sampleArticle)
    }

    @Test
    fun getCommentsFromMockDataSource_mustBeEqualToFifteen() = runBlockingTest {
        val result = commentServiceApi.getComments("")
        assertTrue(result.size == 15)
    }

}


