package com.retech.route

import com.retech.model.Product
import com.retech.mysql.DbConnection
import com.retech.mysql.entity.EntityProduct
import com.retech.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*


fun Application.routeProduct() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        get("/2") {
            call.respondText("Welcome to Ktor Mysql")
        }

        post("/product/register") {
            try {
                val product: Product = call.receive()

                if (product.nome.isNullOrBlank() || product.preço == null || product.descricao.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Nome, preço e descrição são obrigatórios.")
                    )
                    return@post
                }

                val noOfRowsAffected = db.insert(EntityProduct) {
                    set(it.nome, product.nome)
                    set(it.preço, product.preço)
                    set(it.descricao, product.descricao)
                    set(it.stock, product.stock ?: 0)
                }

                if (noOfRowsAffected > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$noOfRowsAffected produto(s) cadastrado(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        GenericResponse(false, "Erro ao cadastrar o produto.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro durante o cadastro: ${e.message}")
                )
            }
        }

        get("/products/list") {
            try {
                val products = db.from(EntityProduct)
                    .select()
                    .map { row ->
                        Product(
                            nome = row[EntityProduct.nome] ?: "",
                            descricao = row[EntityProduct.descricao] ?: "",
                            preço = row[EntityProduct.preço] ?: 0
                        )
                    }
                call.respond(HttpStatusCode.OK, products)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao buscar produtos: ${e.message}")
                )
            }
        }

        put("/product/update/{productid}") {
            try {
                val productid = call.parameters["productid"]?.toIntOrNull()
                if (productid == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "ID de produto inválido ou ausente.")
                    )
                    return@put
                }

                val product: Product = call.receive()
                if (product.nome.isNullOrBlank() || product.preço == null || product.descricao.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Nome e preço são obrigatórios.")
                    )
                    return@put
                }

                val rowsAffected = db.update(EntityProduct) {
                    set(it.nome, product.nome)
                    set(it.preço, product.preço)
                    set(it.descricao, product.descricao ?: "")
                    set(it.stock, product.stock ?: 0)
                    where { it.productid eq productid }
                }

                if (rowsAffected > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsAffected produto(s) atualizado(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "Produto com ID: $productid não encontrado.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao atualizar produto: ${e.message}")
                )
            }
        }

        delete("/product/delete/{productid}") {
            try {
                val productid = call.parameters["productid"]?.toIntOrNull()
                if (productid == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "ID de produto inválido ou ausente.")
                    )
                    return@delete
                }

                val rowsDeleted = db.delete(EntityProduct) {
                    it.productid eq productid
                }

                if (rowsDeleted > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsDeleted produto(s) excluído(s) com sucesso.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "Produto com ID: $productid não encontrado.")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenericResponse(false, "Erro ao excluir produto: ${e.message}")
                )
            }
        }
    }
}


/*fun Application.routeProduct() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        get("/2")
        {
            call.respondText("Welcome to Ktor Mysql")
        }

        post("/product/register")
        {
            val product: Product = call.receive()
            val noOfRowsAffected = db.insert(EntityProduct)
            {
                set(it.nome, product.nome)
                set(it.preço, product.preço)
                set(it.descriçao, product.descriçao)
                set(it.stock, product.stock)

            }

            if (noOfRowsAffected > 0) {
                //success
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(isSuccess = true, data = "$noOfRowsAffected rows are affected")
                )
            } else {
                //fail
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(isSuccess = true, data = "Error to register the product")
                )
            }
        }

            get("/products/list") {
                try {
                    val product = db
                        .from(EntityProduct)
                        .select()
                        .map { row ->
                            Product(
                                nome = row[EntityProduct.nome] ?: "",
                                descriçao = row[EntityProduct.descriçao] ?: "",
                                preço = row [EntityProduct.preço]  ?: 0
                            )
                        }
                    call.respond(HttpStatusCode.OK, product)
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        GenericResponse(false, "Erro ao proecurar os produtos: ${e.message}")
                    )
                }
            }


            // (UPDATE) PUT: Atualizar um produto
            // URL: /product/update/{id}
            put("/product/update/{id}") {
                val productId = call.parameters["id"]?.toIntOrNull()
                if (productId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Invalid or missing product ID")
                    )
                    return@put
                }

                // Dados enviados no corpo da requisição
                val product: Product = call.receive()

                val rowsAffected = db.update(EntityProduct) {
                    set(it.nome, product.nome)
                    set(it.preço, product.preço)
                    set(it.descriçao, product.descriçao)
                    set(it.stock, product.stock)
                    where { it.id eq productId }
                }

                if (rowsAffected > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsAffected row(s) updated successfully!")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "product ID: $productId not found.")
                    )
                }
            }

            // (DELETE) DELETE: Excluir uma categoria
            // URL: /product/delete/{id}
            delete("/product/delete/{id}") {
                val productId = call.parameters["id"]?.toIntOrNull()
                if (productId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(false, "Invalid or missing product ID")
                    )

                    return@delete
                }

                val rowsDeleted = db.delete(EntityProduct) {
                    it.id eq productId
                }

                if (rowsDeleted > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(true, "$rowsDeleted row(s) deleted successfully!")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericResponse(false, "Product ID: $productId not found.")
                    )
                }
            }
    }
}*/