package com.unamad.aulago.api

import com.unamad.aulago.classes.ResponseData
import com.unamad.aulago.models.apiModels.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IApiStudent {
    @GET("api/mobile/student/sections/{termId}")
    suspend fun getStudentCourseSection(
        @Path("termId") termId: String,
        @Header("Authorization") token: String
    ):List<StudentCourseSectionApiModel>

    @GET("api/mobile/student/schedule/{userId}/{termId}")
    suspend fun getStudentSchedule(
        @Path("userId") userId: String,
        @Path("termId") termId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<ScheduleApiModel>>

    @GET("api/mobile/studentSection/{sectionId}")
    suspend fun getStudentSection(
        @Path("sectionId") sectionId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<StudentSectionApiModel>>

    @GET("api/mobile/student/homework/{userId}/{termId}")
    suspend fun getStudentHomework(
        @Path("userId") userId: String,
        @Path("termId") termId: String,
        @Header("Authorization") token: String
    ): ResponseData<List<HomeworkStudentApiModel>>

    @GET("api/mobile/student/homework")
    suspend fun getStudentHomeworkV2(
        @Header("Authorization") token: String
    ): List<CurrentHomeworkStudentApiModel>

    @GET("api/mobile/student/assistance-list")
    suspend fun getStudentAssistance(
        @Header("Authorization") token: String
    ): List<AssistanceApiModel>

    @GET("api/mobile/student/profile")
    suspend fun getStudentProfile(
        @Header("Authorization") token: String
    ): StudentProfileApiModel

    @GET("api/mobile/student/justifications-list")
    suspend fun listStudentAbsenceJustification(
        @Header("Authorization") token: String
    ): List<StudentAbsenceJustificationApiModel>
}