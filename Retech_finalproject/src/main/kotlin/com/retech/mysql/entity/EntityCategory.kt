package com.retech.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityCategory: Table<Nothing>(tableName = "category") {
    val categoryid = int("categoryid").primaryKey()
    val nome = varchar("nome")
    val descricao = varchar( "descricao")
    val quantidade = int("quantidade")

}

