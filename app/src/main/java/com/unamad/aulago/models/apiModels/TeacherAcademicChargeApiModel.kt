package com.unamad.aulago.models.apiModels

import com.google.gson.annotations.SerializedName


data class TeacherAcademicChargeApiModel(
    @SerializedName("sectionId") val sectionId: String,
    @SerializedName("sectionCode") val sectionCode: String,
    @SerializedName("teacherId") val teacherId: String,
    @SerializedName("name") val name: String,
    @SerializedName("paternalSurname") val paternalSurname: String,
    @SerializedName("maternalSurname") val maternalSurname: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("sex") val sex: Int,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("isPrincipal") val isPrincipal: Boolean,
    @SerializedName("isDirectedCourse") val isDirectedCourse: Boolean,
    @SerializedName("courseCode") val courseCode: String,
    @SerializedName("courseName") val courseName: String,
    @SerializedName("courseId") val courseId: String,

    val credits: String,
    val careerName: String,
    val careerCode: String,
    val careerId: String,
    val courseCurriculumCode: String,

    val academicYearCourse: Int,
    val isElective: Boolean,
    val vacancies: Int,
    val teacherSectionId: String,
    val representativeStudentUserId: String? = null,
    val externalGroupLink: String? = null,

    )