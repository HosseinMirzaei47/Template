package com.example.template.core

object DefaultErrorMapper : ErrorMapper {
    override fun map(exception: Exception): Exception {
        return exception
    }

    fun getMapper(): ErrorMapper {
        TODO()
    }

    fun setMapper(errorMapperImpl: DefaultErrorMapper) {

    }
}