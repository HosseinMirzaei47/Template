package com.example.template

import android.util.Log
import com.example.template.feature1.data.servicemodels.Ad
import com.example.template.feature1.data.servicemodels.Data
import com.example.template.feature1.data.servicemodels.UserRes
import retrofit2.Response
import retrofit2.Retrofit

class UserServiceImpl(private val retrofit: Retrofit) : UserDataSource {

    private var stuff : Response<UserRes>
    init {
        val dataList = mutableListOf<Data>(
            Data(id = 0, avatar = "", email = "", first_name= "Amir", last_name = "R"),
            Data(id = 1, avatar = "", email = "", first_name= "Amir", last_name = "RR"),
            Data(id = 2, avatar = "", email = "", first_name= "Amir", last_name = "RRR"),
        )
        stuff = Response.success(UserRes(ad = Ad("", "part", ""), dataList, 0, 0, 0, 0))
    }

    override suspend fun getUsers(): Response<UserRes> {
        return stuff
    }
}