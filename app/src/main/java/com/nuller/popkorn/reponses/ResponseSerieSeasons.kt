package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseSerieSeasons(

	@field:SerializedName("ResponseSerieSeasons")
	val responseSerieSeasons: List<ResponseSerieSeasonsItem>
)

data class ResponseSerieSeasonsItem(

	@field:SerializedName("season_no")
	val seasonNo: Int
)
