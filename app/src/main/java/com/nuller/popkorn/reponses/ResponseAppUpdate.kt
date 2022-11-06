package com.nuller.popkorn.reponses

import com.google.gson.annotations.SerializedName

data class ResponseAppUpdate(

	@field:SerializedName("download_link")
	val downloadLink: String? = null,

	@field:SerializedName("app_version")
	val appVersion: Double? = null,

	@field:SerializedName("doUpdate")
	val doUpdate: Boolean? = null,

	@field:SerializedName("isForce")
	val isForce: Boolean? = null,

	@field:SerializedName("versionName")
	val versionName: Double? = null,

	@field:SerializedName("versionCode")
	val versionCode: Int? = null
)
