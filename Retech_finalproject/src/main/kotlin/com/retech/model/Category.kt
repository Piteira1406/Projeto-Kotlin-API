package com.retech.model

import com.google.gson.annotations.SerializedName


data class Category(
    @field:SerializedName("categoryid")
    val categoryid: Int? = null,
    @field:SerializedName("nome")
    val nome: String? = null,
    @field:SerializedName("descricao")
    val descricao: String? = null,
    @field:SerializedName("quantidade")
    val quantidade: Int ? = null,
    )
