package com.unamad.aulago.classes

class ValidationData<T>(
    override val isValid: Boolean,
    override val message: String? = null,
    val data: T? = null
): Validation(isValid, message)