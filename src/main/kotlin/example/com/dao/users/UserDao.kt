package example.com.dao.users

import example.com.dao.users.entity.UserEntity

interface UserDao {
    suspend fun insertUser(userEntity: UserEntity): UserEntity?
    suspend fun findUserByEmail(email: String): UserEntity?
}