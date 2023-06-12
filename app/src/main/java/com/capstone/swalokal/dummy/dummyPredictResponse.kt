package com.capstone.swalokal.dummy

import com.capstone.swalokal.api.response.PredictItem

data class DummyResponse(
    val error: Boolean,
    val data: List<PredictItem>
)

//data class DummyData(
//    val id: Int,
//    val name: String,
//    val price: Int,
//    val toko: String,
//    val latitude: Double,
//    val longitude: Double
//)

fun createDummyResponse(): DummyResponse {
    val dummyDataList = listOf(
        PredictItem(
            id = 1,
            name = "pop mie",
            price = 6000,
            toko = "indra keenz",
            latitude = 3.35729312896729,
            longtitude = 99.37930297851562
        ),
        PredictItem(
            id = 5,
            name = "pop mie",
            price = 6000,
            toko = "Chalil Happy",
            latitude = 3.35912704467773,
            longtitude = 99.38341522216797
        )
    )

    return DummyResponse(error = false, data = dummyDataList)
}

fun getDummyJsonResponse(): String {
    return """
            {
              "error": false,
              "data": [
                {
                  "id": 1,
                  "name": "pop mie",
                  "price": 6000,
                  "toko": "indra keenz",
                  "latitude": 3.35729312896729,
                  "longtitude": 99.37930297851562
                },
                {
                  "id": 5,
                  "name": "pop mie",
                  "price": 6000,
                  "toko": "Chalil Happy",
                  "latitude": 3.35912704467773,
                  "longtitude": 99.38341522216797
                }
              ]
            }
        """.trimIndent()
}
