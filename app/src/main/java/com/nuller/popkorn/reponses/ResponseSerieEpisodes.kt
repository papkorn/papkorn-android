package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseSerieEpisodes(

	@field:SerializedName("ResponseSerieEpisodes")
	val responseSerieEpisodes: List<ResponseSerieEpisodesItem>
)

data class ResponseSerieEpisodesItem(

	@field:SerializedName("episode_no")
	val episodeNo: Int,

	@field:SerializedName("id")
	val id: Int
)
