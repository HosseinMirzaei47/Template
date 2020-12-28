package com.example.template.core

interface ErrorMapper {
    fun map(exception: Exception): Exception
}