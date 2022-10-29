package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseMovie(

	@field:SerializedName("cover")
	val cover: String,

	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("tmdb_id")
	val tmdbId: String,

	@field:SerializedName("backdrop")
	val backdrop: String,

	@field:SerializedName("imdb_id")
	val imdbId: String,

	@field:SerializedName("year")
	val year: Int,

	@field:SerializedName("plot")
	val plot: String,

	@field:SerializedName("rate")
	val rate: Double,

	@field:SerializedName("voute_count")
	val vouteCount: Int,

	@field:SerializedName("series_years")
	val seriesYears: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("movie_type")
	val movieType: Int
)
