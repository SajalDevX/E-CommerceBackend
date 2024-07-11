package example.com.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingData(
    val limit:Int,
    val offset:Int
)