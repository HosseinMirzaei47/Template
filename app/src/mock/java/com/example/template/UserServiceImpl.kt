package com.example.template

import android.util.Log
import com.example.template.core.util.NoConnectionException
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.remote.UserDataSource
import com.example.template.home.data.servicemodels.Ad
import com.example.template.home.data.servicemodels.Data
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.delay
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit


class UserServiceImpl(private val api: HomeApi) : UserDataSource {

    private lateinit var stuff: UserRes

    private fun returnResponse(responseIsSuccess: Boolean): UserRes {
        return if (responseIsSuccess) {
            returnSuccessResponse()
        } else {
            returnErrorResponse()
        }
    }

    private fun returnSuccessResponse(): UserRes {
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
        stuff = UserRes(
            ad = Ad("partsoftware", "part", "partsoftware.com"),
            dataList,
            0,
            0,
            0,
            0
        )
        return stuff
    }

    private fun returnErrorResponse(): UserRes {
        throw NoConnectionException("error")
    }

    override suspend fun getUsers(page: Int): UserRes {
        delay(3000)
        return returnResponse(false)
    }


}