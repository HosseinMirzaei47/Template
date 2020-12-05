package com.example.template.core.util

class ServerException(val meta: Meta) : Exception(meta.message)