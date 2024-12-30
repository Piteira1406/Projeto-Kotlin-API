package com.retech.model

import com.google.gson.annotations.SerializedName

data class Product(
    @field:SerializedName("id")
    val categoryid: Int? = null,
    @field:SerializedName("nome")
    val nome: String? = null,
    @field:SerializedName("preço")
    val preço: Int? = null,
    @field:SerializedName("descricao")
    val descricao: String? = null,
    @field:SerializedName("stock")
    val stock: Int? = null

)
