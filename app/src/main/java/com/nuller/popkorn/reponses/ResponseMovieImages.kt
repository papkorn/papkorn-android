package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseMovieImages(

	@field:SerializedName("images")
	val images: List<ImagesItem?>? = null,

	@field:SerializedName("w500")
	val w500: String? = null,

	@field:SerializedName("base_path")
	val basePath: String? = null
)

data class ImagesItem(

	@field:SerializedName("aspect_ratio")
	val aspectRatio: Double? = null,

	@field:SerializedName("file_path")
	val filePath: String? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Double? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: Any? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null
)
