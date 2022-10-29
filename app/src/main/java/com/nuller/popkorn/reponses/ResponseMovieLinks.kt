package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseMovieLinks(

	@field:SerializedName("ResponseMovieLinks")
	val responseMovieLinks: List<ResponseMovieLinksItem?>? = null
)

data class ResponseMovieLinksItem(

	@field:SerializedName("ext")
	val ext: String? = null,

	@field:SerializedName("screen_shot")
	val screenShot: Any? = null,

	@field:SerializedName("file_name")
	val fileName: String? = null,

	@field:SerializedName("is_dubbed")
	val isDubbed: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("uri")
	val uri: String? = null,

	@field:SerializedName("main_url")
	val mainUrl: String? = null,

	@field:SerializedName("resolution")
	val resolution: String? = null,

	@field:SerializedName("encoder")
	val encoder: String? = null,

	@field:SerializedName("x265")
	val x265: Boolean? = null,

	@field:SerializedName("is_trailer")
	val isTrailer: Boolean? = null,

	@field:SerializedName("quality")
	val quality: String? = null,

	@field:SerializedName("is_hardsub")
	val isHardsub: Boolean? = null,

	@field:SerializedName("dubbed")
	val dubbed: Boolean? = null,

	@field:SerializedName("is_deleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("extra_info")
	val extraInfo: ExtraInfo? = null,

	@field:SerializedName("size")
	val size: String? = null,

	@field:SerializedName("is_softsub")
	val isSoftsub: Boolean? = null,

	@field:SerializedName("is_telegram_file")
	val isTelegramFile: Boolean? = null,

	@field:SerializedName("is_stream_link")
	val isStreamLink: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("is_torrent")
	val isTorrent: Boolean? = null
)
