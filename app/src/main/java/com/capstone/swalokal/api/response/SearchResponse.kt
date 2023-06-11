package com.capstone.swalokal.api.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("detail")
	val detail: Detail? = null
)

data class Detail(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class DataItem(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("longtitude")
	val longtitude: Any? = null,

	@field:SerializedName("toko")
	val toko: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
