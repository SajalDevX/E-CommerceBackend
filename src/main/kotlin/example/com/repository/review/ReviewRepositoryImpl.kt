package example.com.repository.review

import example.com.dao.review.ReviewDao
import example.com.dao.users.UserDao
import example.com.model.AddReview
import example.com.model.EditReview
import example.com.model.ReviewResponse
import example.com.utils.Response
import io.ktor.http.*

class ReviewRepositoryImpl(
    private val userDao: UserDao,
    private val reviewDao: ReviewDao
) : ReviewRepository {
    override suspend fun addReview(addReview: AddReview,userId:String): Response<ReviewResponse> {
        val user = userDao.findUserById(userId )
        return if (user == null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ReviewResponse(
                    success = false,
                    message = "Could not find user with id $userId"
                )
            )
        } else {
            val result = reviewDao.addReview(
                userId = userId,
                productId = addReview.productId,
                caption = addReview.caption,
                rating = addReview.rating,
                imageUrl = user.imageUrl,
                username = user.name
            )
            if (result != null) {
                Response.Success(
                    ReviewResponse(
                        success = true,
                        review = result,
                        message = "Review Added successfully"
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    ReviewResponse(
                        success = false,
                        message = "Could not add review"
                    )
                )
            }
        }
    }

    override suspend fun editReview(editReview: EditReview,userId:String): Response<ReviewResponse> {
        val result = reviewDao.editReview(
            userId = userId,
            productId = editReview.productId,
            caption = editReview.caption,
            rating = editReview.rating,
        )
        return if (result != null) {
            Response.Success(
                ReviewResponse(
                    success = true,
                    review = result,
                    message = "Review edited successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ReviewResponse(
                    success = false,
                    message = "Could not edit review"
                )
            )
        }
    }

    override suspend fun getAllReview(productId: String): Response<ReviewResponse> {
        val result = reviewDao.getAllReviews(productId)
        return if (result.isNotEmpty()) {
            Response.Success(
                ReviewResponse(
                    success = true,
                    reviews = result,
                    message = "Reviews fetched successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ReviewResponse(
                    success = false,
                    message = "Could not fetch reviews"
                )
            )
        }
    }

    override suspend fun deleteReview(userId: String, productId: String): Response<ReviewResponse> {
        val result = reviewDao.deleteReview(userId, productId)
        return if (result) {
            Response.Success(
                ReviewResponse(
                    success = true,
                    message = "Review deleted successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ReviewResponse(
                    success = false,
                    message = "Could not delete review"
                )
            )
        }
    }
}