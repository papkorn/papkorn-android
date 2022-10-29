package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseMovieSubtitles(

	@field:SerializedName("ResponseMovieSubtitles")
	val responseMovieSubtitles: List<ResponseMovieSubtitlesItem>
)

data class ExtraInfo(

	@field:SerializedName("author_link")
	val authorLink: String,

	@field:SerializedName("imdb_id")
	val imdbId: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("desc")
	val desc: String
)

data class ResponseMovieSubtitlesItem(

	@field:SerializedName("file_name")
	val fileName: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("uri")
	val uri: String,

	@field:SerializedName("resolution")
	val resolution: String,

	@field:SerializedName("encoder")
	val encoder: Any,

	@field:SerializedName("x265")
	val x265: Boolean,

	@field:SerializedName("quality")
	val quality: String,

	@field:SerializedName("is_deleted")
	val isDeleted: Boolean,

	@field:SerializedName("extra_info")
	val extraInfo: ExtraInfo,

	@field:SerializedName("size")
	val size: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("page")
	val page: Int
)
