package example.com.mappers


import example.com.dao.users.entity.UserEntity
import example.com.model.SignUpParams
import example.com.security.hashPassword

fun SignUpParams.toUserEntity() =
    UserEntity(
        name = name,
        email = email,
        password = hashPassword(password)
    )