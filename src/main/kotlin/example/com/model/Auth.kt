package example.com.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpParams(
    val name: String, val email: String, val password: String,val userDetailsParams:UserDetailsParams
)

@Serializable
data class UserDetailsParams(
    val age: String? = null,
    val mobile: String,
    val gender: String,
    val userRole: String,
)
@Serializable
data class SignInParams(
    val email: String, val password: String
)

@Serializable
data class AuthResponse(
    val data: AuthResponseData? = null, val errorMessage: String? = null
)

@Serializable
data class AuthResponseData(
    val userId: String,
    val name: String,
    val imageUrl:String,
    val token: String,
)