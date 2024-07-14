package example.com.dao.users

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.users.entity.UserEntity
import example.com.model.UpdateProfile
import example.com.model.UpdateUserAddress
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.updateOne

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


    override suspend fun updateProfileAddress(userId: String, updateAddress: UpdateUserAddress): UserEntity? {
        // Filter to find the user by _id
        val filter = Filters.eq("_id", userId)

        // Prepare the updates for the UserAddress
        val updates = mutableListOf<Bson>()
        updateAddress.fatherName.let { updates.add(Updates.set("userDetails.addresses.0.fatherName", it)) }
        updateAddress.motherName.let { updates.add(Updates.set("userDetails.addresses.0.motherName", it)) }
        updateAddress.pin.let { updates.add(Updates.set("userDetails.addresses.0.pin", it)) }
        updateAddress.mobileNumber.let { updates.add(Updates.set("userDetails.addresses.0.mobileNumber", it)) }
        updateAddress.otherMobileNumber?.let { updates.add(Updates.set("userDetails.addresses.0.otherMobileNumber", it)) }
        updateAddress.city.let { updates.add(Updates.set("userDetails.addresses.0.city", it)) }
        updateAddress.road.let { updates.add(Updates.set("userDetails.addresses.0.road", it)) }
        updateAddress.state.let { updates.add(Updates.set("userDetails.addresses.0.state", it)) }

        // Combine the updates
        val update = Updates.combine(updates)

        // Perform the update
        val updateResult: UpdateResult = users.updateOne(filter, update)

        return if (updateResult.matchedCount > 0) {
            // Return the updated user entity
            findUserById(userId)
        } else {
            null
        }
    }

}