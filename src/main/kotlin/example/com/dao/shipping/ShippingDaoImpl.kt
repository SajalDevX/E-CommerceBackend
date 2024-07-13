package example.com.dao.shipping

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.order.entity.OrderEntity
import example.com.dao.users.entity.UserEntity
import example.com.model.AddShipping
import example.com.model.UpdateShipping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.eq

class ShippingDaoImpl(
    db: CoroutineDatabase
) : ShippingDao {
    private val shipping = db.getCollection<ShippingEntity>("shipping")
    override suspend fun addShipping(userId: String, addShipping: AddShipping): ShippingEntity? {
        val exists = shipping.find(
            and(
                eq("userId", userId),
                eq("orderId", addShipping.orderId)
            )
        ).toList().singleOrNull()
        val ship = ShippingEntity(
            userId = userId,
            orderId = addShipping.orderId,
            shipCity = addShipping.shipCity,
            shipName = addShipping.shipName,
            shipEmail = addShipping.shipEmail,
            shipPhone = addShipping.shipPhone,
            shipAddress = addShipping.shippingAddress,
            shipCountry = addShipping.shipCountry
        )
        val result = shipping.insertOne(ship)
        return if (result.wasAcknowledged()) {
            ship
        } else {
            null
        }
    }

    override suspend fun getShipping(userId: String, orderId: String): ShippingEntity? {
        val exists = shipping.find(and(eq("userId", userId), eq("orderId", orderId))).toList().singleOrNull()
        return withContext(Dispatchers.IO) { exists }
    }

    override suspend fun updateShipping(userId: String, updateShipping: UpdateShipping): ShippingEntity? {
        val filter = Filters.and(
            eq("userId", userId),
            eq("orderId", updateShipping.orderId)
        )
        val updates = mutableListOf<Bson>()
        updateShipping.shipAddress.let { updates.add(Updates.set("shippingAddress", it)) }
        updateShipping.shipCity.let { updates.add(Updates.set("shippingAddress", it)) }
        updateShipping.shipPhone.let { updates.add(Updates.set("shippingAddress", it)) }
        updateShipping.shipName.let { updates.add(Updates.set("shippingAddress", it)) }
        updateShipping.shipEmail.let { updates.add(Updates.set("shippingAddress", it)) }
        updateShipping.shipCountry.let { updates.add(Updates.set("shippingAddress", it)) }
        val updateData = Updates.combine(updates)
        val result: UpdateResult = shipping.updateOne(filter, updateData)
        val shipItem = shipping.findOne(filter)
        return withContext(Dispatchers.IO) {
            if (result.modifiedCount > 0) {
                shipItem
            } else null
        }
    }

    override suspend fun deleteShipping(userId: String, orderId: String): Boolean {
        val filter = Filters.and(
            eq("userId", userId),
            eq("orderId", orderId)
        )
        return withContext(Dispatchers.IO) { shipping.deleteOne(filter).deletedCount > 0 }
    }
}