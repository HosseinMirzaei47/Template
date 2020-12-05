package com.example.template

import com.example.template.home.data.remote.UserDataSource
import com.example.template.home.data.servicemodels.Ad
import com.example.template.home.data.servicemodels.Data
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.delay
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.Retrofit


class UserServiceImpl(private val retrofit: Retrofit) : UserDataSource {

    private lateinit var stuff: Response<UserRes>

    init {
        returnResponse(true)
    }

    private fun returnResponse(responseIsSuccess: Boolean) {
        if (responseIsSuccess) {
            returnSuccessResponse()
        } else {
            returnErrorResponse()
        }
    }

    private fun returnSuccessResponse() {
        val dataList = mutableListOf(
            Data(
                id = 0,
                avatar = "https://api.time.com/wp-content/uploads/2019/07/jennifer-lopez-birthday-party.jpg",
                email = "jenniferlopez@gmail.com",
                first_name = "Jennifer",
                last_name = "Lopez"
            ),
            Data(
                id = 1,
                avatar = "https://i.pinimg.com/originals/ae/38/4a/ae384a97f1fc3bbd1767e0e2561a7740.jpg",
                email = "shakira@gmail.com",
                first_name = "Shakira",
                last_name = "Khanoom"
            ),
            Data(
                id = 2,
                avatar = "https://mcmscache.epapr.in/post_images/website_13/post_16913054/thumb.jpg",
                email = "scarlettjohansson@gmail.com",
                first_name = "Scarlett",
                last_name = "Johansson"
            ),
        )
        stuff = Response.success(
            UserRes(
                ad = Ad("partsoftware", "part", "partsoftware.com"),
                dataList,
                0,
                0,
                0,
                0
            )
        )
    }

    private fun returnErrorResponse() {
        stuff = Response.error(404, "error happened".toResponseBody())
    }

    override suspend fun getUsers(page: Int): Response<UserRes> {
        delay(3000)
        return stuff
    }

}