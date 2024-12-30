package com.retech.route

import com.retech.model.User
import com.retech.mysql.DbConnection
import com.retech.mysql.entity.EntityUser
import com.retech.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeUser() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        get("/") {
            call.respondText("Welcome to Ktor Mysql")
        }

        post("/register") {
            try {
                val user: User = call.receive()

                if (user.nome.isNullOrBlank() || user.email.isNullOrBlank() || user.password.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Nome, email e password são obrigatórios.")
                    )
                    return@post
                }

                val noOfRowsAffected = db.insert(EntityUser) {
                    set(it.nome, user.nome)
                    set(it.apelido, user.apelido ?: "")
                    set(it.email, user.email)
                    set(it.password, user.password)
                }

                if (noOfRowsAffected > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$noOfRowsAffected usuário(s) cadastrado(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        GenericResponse(false, "Erro ao cadastrar o usuário.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro durante o cadastro: ${e.message}")
                )
            }
        }

        get("/users/list") {
            try {
                val users = db.from(EntityUser)
                    .select()
                    .map { row ->
                        User(
                            nome = row[EntityUser.nome] ?: "",
                            apelido = row[EntityUser.apelido] ?: "",
                            email = row[EntityUser.email] ?: "",
                            password = row[EntityUser.password] ?: "",
                        )
                    }
                call.respond(HttpStatusCode.OK, users)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao buscar usuários: ${e.message}")
                )
            }
        }

        put("/users/update/{userid}") {
            try {
                val userid = call.parameters["userid"]?.toIntOrNull()
                if (userid == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "ID de usuário inválido ou ausente.")
                    )
                    return@put
                }

                val user: User = call.receive()
                if (user.nome.isNullOrBlank() || user.email.isNullOrBlank() || user.password.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Nome, email e password são obrigatórios.")
                    )
                    return@put
                }

                val rowsAffected = db.update(EntityUser) {
                    set(it.nome, user.nome)
                    set(it.apelido, user.apelido ?: "")
                    set(it.email, user.email)
                    set(it.password, user.password)
                    where { it.userid eq userid }
                }

                if (rowsAffected > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsAffected usuário(s) atualizado(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "Usuário com ID: $userid não encontrado.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao atualizar usuário: ${e.message}")
                )
            }
        }

        delete("/users/delete/{userid}") {
            try {
                val userid = call.parameters["userid"]?.toIntOrNull()
                if (userid == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "ID de usuário inválido ou ausente.")
                    )
                    return@delete
                }

                val rowsDeleted = db.delete(EntityUser) {
                    it.userid eq userid
                }

                if (rowsDeleted > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsDeleted usuário(s) excluído(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "Usuário com ID: $userid não encontrado.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao excluir usuário: ${e.message}")
                )
            }
        }
    }
}


