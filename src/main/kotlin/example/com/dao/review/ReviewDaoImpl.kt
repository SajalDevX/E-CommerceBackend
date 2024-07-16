package example.com.dao.review

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase

class ReviewDaoImpl(
    db: CoroutineDatabase
) : ReviewDao {
    private val review = db.getCollection<ReviewEntity>("review")
    override suspend fun addReview(
        userId: String,
        productId: String,
        caption: String,
        rating: Int,
        imageUrl: String,
        username: String
    ): ReviewEntity? {
        val data = ReviewEntity(
            userId = userId,
            productId = productId,
            caption = caption,
            profilePic = imageUrl,
            rating = rating,
            username = username
        )
        val result = review.insertOne(data)
        return if (result.wasAcknowledged()) {
            data
        } else {
            null
        }
    }
    override suspend fun editReview(userId: String, productId: String, caption: String, rating: Int): ReviewEntity? {
        val filter = Filters.and(
            Filters.eq("userId", userId),
            Filters.eq("productId", productId)
        )
        val update1 = Updates.set("caption", caption)
        val update2 = Updates.set("rating", rating)
        val updateResult = Updates.combine(update1, update2)
        val updated: UpdateResult = review.updateOne(filter, updateResult)
        return if (updated.modifiedCount > 0) {
            val result = review.findOne(filter)
            result
        } else {
            null
        }
    }

    override suspend fun getAllReviews(productId: String): List<ReviewEntity> {
        val filter = Filters.eq("productId",productId)
        val reviews = review.find(filter).toList()
        return reviews
    }

    override suspend fun deleteReview(userId: String, productId: String): Boolean {
        val filter = Filters.and(
            Filters.eq("userId", userId),
            Filters.eq("productId", productId)
        )
        return withContext(Dispatchers.IO) {
            val result = review.deleteOne(filter)
            result.deletedCount > 0
        }
    }
}