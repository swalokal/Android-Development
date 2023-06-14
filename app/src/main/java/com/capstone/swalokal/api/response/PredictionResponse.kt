package com.capstone.swalokal.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PredictionResponse(

	@field:SerializedName("detail")
	val detail: Detail
)

@Parcelize
data class PredictItem(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("toko")
	val toko: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
) : Parcelable

data class Detail(

	@field:SerializedName("data")
	val data: List<PredictItem>,

	@field:SerializedName("eror")
	val eror: Boolean? = null
)
