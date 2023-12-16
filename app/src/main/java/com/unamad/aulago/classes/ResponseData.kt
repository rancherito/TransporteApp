package com.unamad.aulago.classes

class ResponseData<T>(
    val data: T? = null,
    val isSuccess: Boolean,
    val message: String?
)

