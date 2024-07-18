package example.com.repository.auth

import example.com.dao.users.UserDao
import example.com.mappers.toUserEntity
import example.com.model.AuthResponse
import example.com.model.AuthResponseData
import example.com.model.SignInParams
import example.com.model.SignUpParams
import example.com.plugins.generateToken
import example.com.security.hashPassword
import example.com.utils.JwtTokenBody
import example.com.utils.Response
import io.ktor.http.*

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {
    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAlreadyExists(params.email)) {
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "User already exists, please try logging in"
                )
            )
        } else {
            val insertedUser = userDao.insertUser(params.toUserEntity())
            if (insertedUser == null) {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Oops, sorry we couldn't register the user try later"
                    )
                )
            } else {

                Response.Success(
                    data = AuthResponse(
                        AuthResponseData(
                            userId = insertedUser.userId,
                            name = insertedUser.name,
                            imageUrl = insertedUser.imageUrl,
                            token = generateToken(
                                insertedUser.userId,
                                insertedUser.email,
                                insertedUser.userDetails.userRole
                            ),
                            userRole = insertedUser.userDetails.userRole
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        val user = userDao.findUserByEmail(params.email)
        return if (user == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "Invalid Credentials, No user with this email!"
                )
            )
        } else {
            val hashedPassword = hashPassword(params.password)
            if (user.password == hashedPassword) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            userId = user.userId,
                            name = user.name,
                            imageUrl = user.imageUrl,
                            token = generateToken(user.userId, user.email, user.userDetails.userRole),
                            userRole = user.userDetails.userRole

                        )
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = AuthResponse(
                        errorMessage = "Invalid Credentials, Wrong password! $hashedPassword != ${params.password}"
                    )
                )
            }
        }
    }

    private suspend fun userAlreadyExists(email: String): Boolean {
        return userDao.findUserByEmail(email) != null
    }
}