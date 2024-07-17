package example.com.model

import example.com.dao.review.ReviewEntity
import kotlinx.serialization.Serializable

@Serializable
data class AddReview(
    val productId: String,
    val caption: String,
    val rating: Int,
)

@Serializable
data class EditReview(
    val productId: String,
    val caption: String,
    val rating: Int
)
@Serializable
data class ReviewResponse(
    val success:Boolean,
    val message:String,
    val review:ReviewEntity?=null,
    val reviews:List<ReviewEntity> = emptyList()
)