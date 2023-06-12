package com.capstone.swalokal.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PredictionResponse(

	@field:SerializedName("data")
	val data: List<PredictItem>,

	@field:SerializedName("error")
	val error: Boolean? = null
)

@Parcelize
data class PredictItem(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("longtitude")
	val longtitude: Double,

	@field:SerializedName("toko")
	val toko: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable
