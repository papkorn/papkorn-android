package com.nuller.popkorn

import com.google.gson.annotations.SerializedName

data class ResponseHomePage(

	@field:SerializedName("movie")
	val movie: List<MovieItem?>? = null,

	@field:SerializedName("serie")
	val serie: List<SerieItem?>? = null
)

data class MovieItem(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class SerieItem(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
