package com.example.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.template.core.util.bodyOrThrow
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.remote.UserDataSource
import com.example.template.home.data.servicemodels.Ad
import com.example.template.home.data.servicemodels.Data
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserServiceImpl(private val api: HomeApi) : UserDataSource {

    override fun getUsersLiveData(page: Int): LiveData<UserRes> = liveData {
        emit(
            UserRes(
                ad = Ad("partsoftware", "part", "partsoftware.com"),
                mutableListOf(
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
                ),
                0,
                0,
                0,
                0
            )
        )
    }

    override suspend fun getUsers(page: Int): UserRes {
        return api.getUsers(page).bodyOrThrow()
    }

    override suspend fun getUsersFlow(page: Int): Flow<UserRes> {
        return flow {
            emit(api.getUsers(3).bodyOrThrow())
        }
    }
}