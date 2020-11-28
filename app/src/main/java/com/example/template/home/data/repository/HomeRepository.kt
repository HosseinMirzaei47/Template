package com.example.template.home.data.repository

import com.example.template.home.data.remote.HomeRemote
import javax.inject.Inject

class HomeRepository  @Inject constructor(private val homeRemote: HomeRemote){
     suspend fun getUsers(page:Int)=homeRemote.getUsers(page)
}