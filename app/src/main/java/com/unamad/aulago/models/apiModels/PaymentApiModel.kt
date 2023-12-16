package com.unamad.aulago.models.apiModels

data class PaymentApiModel(
    val id: String ,
    val termName: String ? = null,
    val description: String,
    val issueDate: String,
    val paymentDate: String?,
    val total: Double,
    val isBankPayment: Boolean,
    val status: Int,
    val createdBy: String?,
    val conceptCode: String?
)