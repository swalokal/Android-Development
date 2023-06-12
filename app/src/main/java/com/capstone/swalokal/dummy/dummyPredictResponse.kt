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

//fun createDummyResponse(): DummyResponse {
//    val dummyDataList = listOf(
//        PredictItem(
//            id = 1,
//            name = "pop mie",
//            price = 6000,
//            toko = "indra keenz",
//            latitude = 3.35729312896729,
//            longtitude = 99.37930297851562
//        ),
//        PredictItem(
//            id = 5,
//            name = "pop mie",
//            price = 6000,
//            toko = "Chalil Happy",
//            latitude = 3.35912704467773,
//            longtitude = 99.38341522216797
//        )
//    )
//
//    return DummyResponse(error = false, data = dummyDataList)
//}

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
                  "photoUrl": "https://images.unsplash.com/photo-1534723452862-4c874018d66d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80",
                  "latitude": 3.35729312896729,
                  "longtitude": 99.37930297851562
                },
                {
                  "id": 5,
                  "name": "pop mie",
                  "price": 6000,
                  "toko": "Chalil Happy",
                  "photoUrl": "https://images.unsplash.com/photo-1534723452862-4c874018d66d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80",
                  "latitude": 3.35912704467773,
                  "longtitude": 99.38341522216797
                },
                {
                  "id": 7,
                  "name": "pop mie",
                  "price": 6000,
                  "toko": "pamu ganteng",
                  "photoUrl": "https://images.unsplash.com/photo-1534723452862-4c874018d66d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80",
                  "latitude": 3.5612669925,
                  "longtitude": 98.7177522655
                }
              ]
            }
        """.trimIndent()
}
