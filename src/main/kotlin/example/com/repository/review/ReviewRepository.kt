package example.com.repository.review

import example.com.model.AddReview
import example.com.model.EditReview
import example.com.model.ReviewResponse
import example.com.utils.Response

interface ReviewRepository {
    suspend fun addReview(addReview: AddReview,userId:String): Response<ReviewResponse>
    suspend fun editReview(editReview: EditReview,userId:String): Response<ReviewResponse>
    suspend fun getAllReview(productId: String): Response<ReviewResponse>
    suspend fun deleteReview(userId: String, productId: String): Response<ReviewResponse>
}