package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseMovieByUrl(

	@field:SerializedName("next")
	val next: String,

	@field:SerializedName("previous")
	val previous: String,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("results")
	val results: List<ResultsItem>,

	@field:SerializedName("facets")
	val facets: Facets
)

data class ResultsItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("total_download_link")
	val totalDownloadLink: Int,

	@field:SerializedName("backdrop")
	val backdrop: String,

	@field:SerializedName("imdb_id")
	val imdbId: Int,

	@field:SerializedName("year")
	val year: Int,

	@field:SerializedName("title_fa")
	val titleFa: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("movie_status")
	val movieStatus: MovieStatus,

	@field:SerializedName("plot_fa")
	val plotFa: String,

	@field:SerializedName("crew")
	val crew: List<CrewItem>,

	@field:SerializedName("cover")
	val cover: String,

	@field:SerializedName("cast")
	val cast: List<CastItem>,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("plot")
	val plot: String,

	@field:SerializedName("rate")
	val rate: Double,

	@field:SerializedName("popularity")
	val popularity: Int,

	@field:SerializedName("voute_count")
	val vouteCount: Double,

	@field:SerializedName("genre")
	val genre: List<GenreItem>,

)

data class MovieType(

	@field:SerializedName("name")
	val name: String
)

data class CastItem(

	@field:SerializedName("people_id")
	val peopleId: Int,

	@field:SerializedName("character_name")
	val characterName: String,

	@field:SerializedName("people_name")
	val peopleName: String
)

data class GenreItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("name_fa")
	val nameFa: String
)

data class MovieStatus(

	@field:SerializedName("slug")
	val slug: String
)

data class CrewItem(

	@field:SerializedName("people_id")
	val peopleId: Int,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("people_name")
	val peopleName: String
)

data class Facets(
	val any: Any? = null
)
