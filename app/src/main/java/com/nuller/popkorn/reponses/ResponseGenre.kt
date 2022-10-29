package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseGenre(

	@field:SerializedName("ResponseGenre")
	val responseGenre: List<ResponseGenreItem?>? = null
)

data class ResponseGenreItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo")
	val photo: Any? = null,

	@field:SerializedName("persian_name")
	val persianName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
