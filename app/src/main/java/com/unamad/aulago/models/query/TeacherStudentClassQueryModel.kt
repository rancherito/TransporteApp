package com.unamad.aulago.models.query

data class TeacherStudentClassQueryModel(
    var studentUserId: String,
    var fullName: String,
    var teacherClassId: String,
    var idTeacherStudentClass: String?,
    var isAbsent: Boolean?

)
