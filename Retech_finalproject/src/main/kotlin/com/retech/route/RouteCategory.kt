package com.retech.route

import com.retech.model.Category
import com.retech.mysql.DbConnection
import com.retech.mysql.entity.EntityCategory
import com.retech.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*





fun Application.routeCategory() {
    val db: Database = DbConnection.getDatabaseInstance()

    routing {
        get("/1") {
            call.respondText("Welcome to Ktor Mysql")
        }

        post("/category/register") {
            try {
                val category: Category = call.receive()

                if (category.nome.isNullOrBlank() || category.descricao.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Nome e descrição são obrigatórios.")
                    )
                    return@post
                }

                val noOfRowsAffected = db.insert(EntityCategory) {
                    set(it.nome, category.nome)
                    set(it.descricao, category.descricao)
                    set(it.quantidade, category.quantidade ?: 0)
                }

                if (noOfRowsAffected > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$noOfRowsAffected categoria(s) cadastrada(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        GenericResponse(false, "Erro ao cadastrar a categoria.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro durante o cadastro: ${e.message}")
                )
            }
        }

        get("/categories/list") {
            try {
                val categories = db.from(EntityCategory)
                    .select()
                    .map { row ->
                        Category(
                            nome = row[EntityCategory.nome] ?: "",
                            descricao = row[EntityCategory.descricao] ?: "",
                            quantidade = row[EntityCategory.quantidade] ?: 0
                        )
                    }
                call.respond(HttpStatusCode.OK, categories)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao buscar categorias: ${e.message}")
                )
            }
        }

        put("/categories/update/{categoryid}") {
            try {
                println("Recebendo requisição para update de ID: ${call.parameters["nome"]}")
                val categoryid = call.parameters["categoryid"]?.toIntOrNull()
                if (categoryid == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "ID de categoria inválido ou ausente.")
                    )
                    return@put
                }

                val category: Category = call.receive()
                println("Nome: ${category.nome}, Descrição: ${category.descricao}, Quantidade: ${category.quantidade}")
                if (category.nome.isNullOrBlank() || category.descricao.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Nome e descrição são obrigatórios.")
                    )
                    return@put
                }

                val rowsAffected = db.update(EntityCategory) {
                    set(it.nome, category.nome)
                    set(it.descricao, category.descricao)
                    set(it.quantidade, category.quantidade)
                    where { it.categoryid eq categoryid }
                }

                if (rowsAffected > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsAffected categoria(s) atualizada(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "Categoria com ID: $categoryid não encontrada.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao atualizar categoria: ${e.message}")
                )
            }
        }

        delete("/categories/delete/{categoryid}") {
            try {
                val categoryid = call.parameters["categoryid"]?.toIntOrNull()
                if (categoryid == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "ID de categoria inválido ou ausente.")
                    )
                    return@delete
                }

                val rowsDeleted = db.delete(EntityCategory) {
                    it.categoryid eq categoryid
                }

                if (rowsDeleted > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsDeleted categoria(s) excluída(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "Categoria com ID: $categoryid não encontrada.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao excluir categoria: ${e.message}")
                )
            }
        }
    }
}


// -------------------------------------------------------------------------

/*fun Application.routeCategory() {
    // Inicia a conexão com o banco via Ktorm
    val db: Database = DbConnection.getDatabaseInstance()

    routing {
        // -------------------------------------------------------------------------
        // Rota de teste, para verificar se está tudo funcionando
        // -------------------------------------------------------------------------
        get("/1") {
            call.respondText("Welcome to Ktor Mysql")
        }

        // -------------------------------------------------------------------------
        // CREATE (POST) -> /category/register
        // Exemplo no Postman:
        // POST http://localhost:8080/category/register

        post("/category/register") {
            val category: Category = call.receive()

            val noOfRowsAffected = db.insert(EntityCategory) {
                set(it.nome, category.nome)
                set(it.descriçao, category.descriçao)
                set(it.quantidade, category.quantidade)
            }

            if (noOfRowsAffected > 0) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(true, "$noOfRowsAffected row(s) affected")
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(false, "Error to register the category")
                )
            }
        }

        // -------------------------------------------------------------------------
        // READ (GET) -> /categories/list
        // Exemplo no Postman:
        // GET http://localhost:8080/categories/list
        // -------------------------------------------------------------------------
        get("/categories/list") {
            try {
                val categories = db
                    .from(EntityCategory)
                    .select()
                    .map { row ->
                        Category(
                            nome = row[EntityCategory.nome] ?: "",
                            descriçao = row[EntityCategory.descriçao] ?: "",
                            quantidade = row[EntityCategory.quantidade] ?: 0
                        )
                    }
                call.respond(HttpStatusCode.OK, categories)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao buscar categorias: ${e.message}")
                )
            }
        }

        // -------------------------------------------------------------------------
        // UPDATE (PUT) -> /categories/update/{id}
        // Exemplo no Postman:
        // PUT http://localhost:8080/categories/update/1
        // Body (JSON):
        // {
        //   "nome": "Eletrônicos Atualizados",
        //   "descriçao": "Descrição nova",
        //   "quantidade": 99
        // }
        // -------------------------------------------------------------------------
        put("/categories/update/{id}") {
            val categoryId = call.parameters["id"]?.toIntOrNull()
            if (categoryId == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(false, "Invalid or missing category ID")
                )
                return@put
            }

            val category: Category = call.receive()

            val rowsAffected = db.update(EntityCategory) {
                set(it.nome, category.nome)
                set(it.descriçao, category.descriçao)
                set(it.quantidade, category.quantidade)
                where { it.id eq categoryId }
            }

            if (rowsAffected > 0) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(true, "$rowsAffected row(s) updated successfully!")
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    GenericResponse(false, "Category ID: $categoryId not found.")
                )
            }
        }

        // -------------------------------------------------------------------------
        // DELETE -> /categories/delete/{id}
        // Exemplo no Postman:
        // DELETE http://localhost:8080/categories/delete/1
        // -------------------------------------------------------------------------
        delete("/categories/delete/{id}") {
            val categoryId = call.parameters["id"]?.toIntOrNull()
            if (categoryId == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(false, "Invalid or missing category ID")
                )
                return@delete
            }

            val rowsDeleted = db.delete(EntityCategory) {
                it.id eq categoryId
            }

            if (rowsDeleted > 0) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(true, "$rowsDeleted row(s) deleted successfully!")
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    GenericResponse(false, "Category ID: $categoryId not found.")
                )
            }
        }
    }
}*/