package com.unamad.aulago.models.apiModels


import com.google.gson.annotations.SerializedName


data class StudentTeacherSectionApiModel(
    val sectionId: String,
    val sectionCode: String,
    val courseName: String,
    val courseId: String,
    val courseCode: String,
    val termId: String,
    val isElective: Boolean,
    val academicYearCourse: Int,
    val isDirectedCourse: Boolean,
    val studentSectionId: String,
    val vacancies: Int,
    val credits: Float,
    val careerName: String,
    val careerCode: String,
    val careerId: String,
    val courseCurriculumCode: String,

    @SerializedName("name") val name: String? = null,
    @SerializedName("paternalSurname") val paternalSurname: String? = null,
    @SerializedName("maternalSurname") val maternalSurname: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("sex") val sex: Int? = null,
    @SerializedName("isPrincipal") val isPrincipal: Boolean? = null,
    @SerializedName("teacherId") val teacherId: String? = null,
    @SerializedName("teacherSectionId") val teacherSectionId: String? = null
)