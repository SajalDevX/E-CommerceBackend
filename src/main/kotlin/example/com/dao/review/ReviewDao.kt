package example.com.dao.review

interface ReviewDao {
    suspend fun addReview(
        userId: String,
        productId: String,
        caption: String,
        rating: Int,
        imageUrl: String,
        username: String
    ): ReviewEntity?

    suspend fun editReview(userId: String, productId: String, caption: String, rating: Int): ReviewEntity?
    suspend fun getAllReviews(productId: String): List<ReviewEntity>

    suspend fun deleteReview(userId: String, productId: String): Boolean
}