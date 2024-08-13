package example.com.dao.users

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.users.entity.UserEntity
import example.com.model.AddUserAddress
import example.com.model.UpdateProfile
import org.bson.conversions.Bson
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

    override suspend fun findUserById(userId: String): UserEntity? =
        users.find(Filters.eq("_id", userId)).first()


    override suspend fun updateUserProfile(userId: String, updateProfile: UpdateProfile): UserEntity? {
        // Filter to find the user by _id
        val filter = Filters.eq("_id", userId)

        // Prepare the updates
        val updates = mutableListOf<Bson>()

        updateProfile.age.let { updates.add(Updates.set("userDetails.age", it)) }
        updateProfile.name.let { updates.add(Updates.set("name", it)) }
        updateProfile.mobile.let { updates.add(Updates.set("userDetails.mobile", it)) }
        updateProfile.imageUrl.let { updates.add(Updates.set("imageUrl", it)) }
        updateProfile.gender.let { updates.add(Updates.set("userDetails.gender", it)) }

        // Combine the updates
        val update = Updates.combine(updates)

        // Perform the update
        val updateResult: UpdateResult = users.updateOne(filter, update)

        return if (updateResult.wasAcknowledged()) {
            // Return the updated user entity
            findUserById(userId)
        } else {
            null
        }
    }

    override suspend fun addProfileAddress(userId: String, addAddress: AddUserAddress): UserEntity? {
        val filter = Filters.eq("_id", userId)

        val update = Updates.push("userDetails.addresses", addAddress)

        val updateResult: UpdateResult = users.updateOne(filter, update)

        return if (updateResult.matchedCount > 0) {
            findUserById(userId)
        } else {
            null
        }
    }
    override suspend fun deleteAddress(userId: String, index: Int): UserEntity? {
        val filter = Filters.eq("_id", userId)
        val update = Updates.unset("userDetails.addresses.$index")

        val updateResult = users.updateOne(filter, update)

        return if (updateResult.matchedCount > 0) {
            users.updateOne(filter, Updates.pull("userDetails.addresses", null))
            findUserById(userId)
        } else {
            null
        }
    }

}