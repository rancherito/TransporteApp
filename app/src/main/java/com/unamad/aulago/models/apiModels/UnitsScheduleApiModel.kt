package com.unamad.aulago.models.apiModels

class UnitsScheduleApiModel(
    val componentId: String,
    val component: Int,
    val numberUnit: Int,
    val data: StartEndDate? = null
)