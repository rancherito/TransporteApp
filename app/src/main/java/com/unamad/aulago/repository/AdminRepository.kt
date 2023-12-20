package com.unamad.aulago.repository

import android.content.Context
import android.widget.Toast
import com.unamad.aulago.Roles
import com.unamad.aulago.SystemDatabase
import com.unamad.aulago.models.database.UserModel
import com.unamad.aulago.models.database.UserRoleModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val database: SystemDatabase,
    private val generalRepository: GeneralRepository,
    private val scope: CoroutineScope,
    @ApplicationContext private val context: Context
) {

    fun registrarConductor(
        nombre: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        telefono: String,
        genero: String,
        fechaNacimiento: String,
        email: String,
        password: String,

        placa: String,
        modelo: String,
        marca: String,
        tipo: String,
        nroAsientos: Int,
        usuario: String
    ){
        Toast.makeText(context, "Registrando Conductor", Toast.LENGTH_SHORT).show()

        val id = UUID.randomUUID().toString()

        scope.launch {
            val conductor = UserModel(
                id = id,
                name = nombre,
                paternalSurname = apellidoPaterno,
                maternalSurname = apellidoMaterno,
                genero = genero,
                fechaNacimiento = fechaNacimiento,
                email = email,
                password = password,
                phoneNumber = telefono,
                modifyAt = "",
                userName = usuario,
            )
            database.adminDao().insertarComductor(conductor)

            val rol = UserRoleModel(
                id = UUID.randomUUID().toString(),
                userId = id,
                role = Roles.Conductor
            )
            database.adminDao().insertarRol(rol)


           /* val taxi = generalRepository.registrarTaxi(
                placa,
                modelo,
                marca,
                tipo,
                nroAsientos,
                conductor.id
            )
            generalRepository.registrarRol(
                conductor.id,
                "conductor"
            )*/

        }
    }

}
