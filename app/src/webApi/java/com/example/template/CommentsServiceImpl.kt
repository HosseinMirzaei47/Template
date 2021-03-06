package com.example.template

import com.example.template.core.util.bodyOrThrow
import com.example.template.home.data.remote.CommentDataSource
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.servicemodels.Comment
import kotlinx.coroutines.delay
import retrofit2.Response

class CommentServiceImpl(private val api: HomeApi) : CommentDataSource {


    private lateinit var stuff: Response<List<Comment>>

    private fun returnResponse(responseIsSuccess: Boolean): List<Comment> {
        return if (responseIsSuccess) {
            returnSuccessResponse()
        } else {
            returnErrorResponse()
        }
    }

    private fun returnSuccessResponse(): List<Comment> {
        val commentList = mutableListOf(
            Comment("1", "comment 1 ", "mamd", 13, true),
            Comment("2", "comment 2", "reaq", 14, false),
            Comment("3", "comment 3", "hosein", 14, true),
            Comment("4", "comment 4", "asghar", 15, true),
            Comment("5", "comment 4", "kamran", 16, false),
            Comment("6", "comment 5", "amir", 16, true),
            Comment("7", "comment 6", "sajad", 17, false),
            Comment("8", "comment 7", "zahra", 18, true),
            Comment("9", "comment 8", "fateme", 19, false),
            Comment("10", "comment 9", "nadia", 20, true),
            Comment("11", "comment 190", "hashem", 21, false),
            Comment("12", "comment 2354", "javad", 22, true),
            Comment("13", "comment 34", "amin", 22, false),
            Comment("14", "comment 54", "kobra", 23, true),
            Comment("15", "comment 32", "soosan", 24, false),
            Comment("16", "comment 27", "narges", 25, true),
            Comment("17", "comment 26", "behnoosh", 26, false),


            )
        stuff = Response.success(
            commentList
        )
        return stuff.bodyOrThrow()
    }

    private fun returnErrorResponse(): List<Comment> {
        throw Exception("error")
    }

    override suspend fun getComments(articleId: String): List<Comment> {
        delay(3000L)
        return returnResponse(false)
    }


}