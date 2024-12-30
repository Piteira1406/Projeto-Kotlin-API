package com.retech.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityProduct: Table<Nothing>(tableName = "product") {
    val productid = int("productid").primaryKey()
    val nome = varchar("nome")
    val preço = int("preço")
    val descricao = varchar( "descricao")
    val stock = int("stock")

}
