package example.com.utils

import kotlinx.serialization.Serializable

@Serializable
data class PagingData(
    val limit:Int,
    val offset:Int
)