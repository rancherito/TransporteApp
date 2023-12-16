package com.unamad.aulago.api

import com.unamad.aulago.classes.Response
import com.unamad.aulago.classes.ResponseData
import com.unamad.aulago.models.apiModels.AssistanceStudentApiModel
import com.unamad.aulago.models.apiModels.SaveAssistanceApiModel
import com.unamad.aulago.models.apiModels.ScheduleApiModel
import com.unamad.aulago.models.apiModels.ScheduleGradesPublicationApiModel
import com.unamad.aulago.models.apiModels.TeacherAcademicChargeApiModel
import com.unamad.aulago.models.apiModels.TeacherClassesApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface IApiTeacher {
    @GET("api/mobile/teacher/academicCharge/{userId}/{termId}")
    suspend fun getTeacherAcademicCharge(
        @Path("userId") userId: String,
        @Path("termId") termId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<TeacherAcademicChargeApiModel>>

    @GET("api/mobile/teacher/classes/assistance/list/{sectionId}/{classId}")
    suspend fun getTeacherAssistanceClass(
        @Path("sectionId") sectionId: String,
        @Path("classId") classId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<AssistanceStudentApiModel>>

    @POST("api/mobile/teacher/classes/assistance/save/v2/{ClassId}")
    suspend fun saveTeacherAssistanceClassNew(
        @Body data: List<SaveAssistanceApiModel>,
        @Path("ClassId") classId: String,
        @Header("Authorization") token: String
    ): String

    @GET("api/mobile/teacher/schedule/{userId}/{termId}")
    suspend fun getTeacherSchedule(
        @Path("userId") userId: String,
        @Path("termId") termId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<ScheduleApiModel>>

    @GET("api/mobile/teacher/classes/{userId}/{sectionId}")
    suspend fun getTeacherClasses(
        @Path("userId") userId: String,
        @Path("sectionId") sectionId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<TeacherClassesApiModel>>

    @GET("api/mobile/teacher/grades-publication")
    suspend fun scheduleGradesPublication(
        @Header("Authorization") token: String
    ): List<ScheduleGradesPublicationApiModel>

    //[HttpPost("representative-student-save/{studentUserId}")]
    @POST("api/mobile/teacher/representative-student-save/{sectionId}/{studentUserId}")
    suspend fun saveRepresentativeStudent(
        @Path("studentUserId") studentUserId: String,
        @Path("sectionId") sectionId: String,
        @Header("Authorization") token: String
    ): String

}