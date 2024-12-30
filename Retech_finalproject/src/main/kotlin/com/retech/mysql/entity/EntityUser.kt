package com.retech.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityUser:Table<Nothing>(tableName = "user") {
    val userid = int(name = "userid").primaryKey()
    val nome = varchar(name = "nome")
    val apelido = varchar(name = "apelido")
    val email = varchar(name = "email")
    val password = varchar(name = "password")
}