package com.example.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.remote.UserDataSource
import com.example.template.home.data.servicemodels.Ad
import com.example.template.home.data.servicemodels.Data
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random


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
        throw Exception("error")
    }

    override suspend fun getUsers(page: Int): UserRes {
        delay(Random.nextLong(1000, 4000))
        return if (Random.nextBoolean()) {
            returnResponse(true)

        } else {
            println("mmb error getusers")
            returnResponse(false)

        }
    }

    override fun getUsersLiveData(page: Int): LiveData<UserRes> = liveData {
        delay(Random.nextLong(1000, 4000))
        if (Random.nextBoolean()) {
            emit(returnSuccessResponse())
        } else {
            emit(returnSuccessResponse())
            println("mmb error getusersLiveData")
            // emit(returnErrorResponse())
        }
    }

    override suspend fun getUsersFlow(page: Int): Flow<UserRes> {
        return flow {
            delay(Random.nextLong(1000, 4000))
            if (Random.nextBoolean()) {
                emit(returnSuccessResponse())
            } else {
                println("mmb error getusersFlow")
                emit(returnErrorResponse())
            }
        }
    }


}