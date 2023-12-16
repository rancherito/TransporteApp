package com.unamad.aulago.api

import com.unamad.aulago.classes.ResponseData
import com.unamad.aulago.classes.UserCredential
import com.unamad.aulago.models.apiModels.*
import retrofit2.http.*

interface IApiGeneral: IApiStudent, IApiTeacher {
    @POST("api/login/jwt")
    suspend fun getTokenAccess(
        @Body credentials: UserCredential
    ): ResponseData<UserApiModel>

    @GET("api/mobile/terms")
    suspend fun getTerms(): ResponseData<List<TermApiModel>>

    @GET("api/mobile/common/debts/{userId}")
    suspend fun getDebt(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<PaymentApiModel>>

    @GET("api/mobile/common/general-schedule")
    suspend fun geGeneralSchedule(
        @Header("Authorization") token: String
    ): GeneralScheduleApiModel


    @GET("http://worldclockapi.com/api/json/utc/now")
    suspend fun getUtcDate(): UtcApiModel

    @POST("api/mobile/common/save-whatsapp-link")
    suspend fun saveWhatsappLink(
        @Header("Authorization") token: String,
        @Body whatsappLink: WhatsappLinkApiModel
    ): String

}