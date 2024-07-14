package example.com.dao.shipping

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.model.AddShipping
import example.com.model.UpdateShipping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase

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
        updateShipping.shipAddress?.let { updates.add(Updates.set("shipAddress", it)) }
        updateShipping.shipCity?.let { updates.add(Updates.set("shipCity", it)) }
        updateShipping.shipPhone?.let { updates.add(Updates.set("shipPhone", it)) }
        updateShipping.shipName?.let { updates.add(Updates.set("shipName", it)) }
        updateShipping.shipEmail?.let { updates.add(Updates.set("shipEmail", it)) }
        updateShipping.shipCountry?.let { updates.add(Updates.set("shipCountry", it)) }

        if (updates.isEmpty()) {
            return null // No updates to be made
        }

        val updateData = Updates.combine(updates)

        return withContext(Dispatchers.IO) {
            val result: UpdateResult = shipping.updateOne(filter, updateData)
            if (result.modifiedCount > 0) {
                shipping.findOne(filter)
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