package com.unamad.aulago.models.apiModels

data class StudentTeacher(
    val teacherId: String,
    val isPrincipal: Boolean,
    val teacherSectionId: String,
    val name: String,
    val paternalSurname: String,
    val maternalSurname: String,
    val email: String,
    val phoneNumber: String,
    val sex: Int
)

data class StudentCourseSectionApiModel(
    val sectionId: String,
    val userName: String,
    val sectionCode: String,
    val courseName: String,
    val courseId: String,
    val termId: String,
    val isElective: Boolean,
    val careerCode: String,
    val careerName: String,
    val credits: Int,
    val careerId: String,
    val courseCurriculumCode: String,
    val isDirectedCourse: Boolean,
    val academicYearCourse: Int,
    val vacancies: Int,
    val courseCode: String,
    val studentSectionId: String,
    val representativeStudentUserId: String,
    val externalGroupLink: String,
    val teachers: List<StudentTeacher>
)