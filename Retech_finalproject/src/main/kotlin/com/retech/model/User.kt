package com.retech.model

import com.google.gson.annotations.SerializedName

data class User(
	@field:SerializedName("id")
	val userid: Int? = null,
	@field:SerializedName("apelido")
	val apelido: String? = null,
	@field:SerializedName("password")
	val password: String? = null,
	@field:SerializedName("email")
	val email: String? = null,
	@field:SerializedName("nome")
	val nome: String? = null
)

