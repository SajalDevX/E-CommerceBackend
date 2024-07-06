package com.mrsajal.dao.users

import com.mongodb.client.model.Filters
import example.com.dao.users.UserDao
import example.com.dao.users.entity.UserEntity
import org.litote.kmongo.coroutine.CoroutineDatabase

class UserDaoImpl(
    db: CoroutineDatabase
) : UserDao {
    private val users = db.getCollection<UserEntity>("users")

    override suspend fun insertUser(userEntity: UserEntity): UserEntity {
        users.insertOne(userEntity)
        return userEntity
    }

    override suspend fun findUserByEmail(email: String): UserEntity? =
        users.find(Filters.eq("email", email)).first()

}