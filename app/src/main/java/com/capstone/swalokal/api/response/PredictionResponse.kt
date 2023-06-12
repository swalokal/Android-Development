package com.capstone.swalokal.api.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("data")
	val data: List<PredictItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class PredictItem(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("longtitude")
	val longtitude: Any? = null,

	@field:SerializedName("toko")
	val toko: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
