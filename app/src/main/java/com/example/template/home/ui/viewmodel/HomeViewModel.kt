package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.util.RequestsObserver
import com.example.template.home.domain.GetArticleUseCase
import com.example.template.home.domain.GetCommentUseCase
import com.example.template.home.domain.GetUserUseCase

class HomeViewModel @ViewModelInject constructor(
    private val userUseCase: GetUserUseCase,
    private val articleUseCase: GetArticleUseCase,
    private val commentUseCase: GetCommentUseCase
) : ViewModel() {
    val status = RequestsObserver.getInstance()

}