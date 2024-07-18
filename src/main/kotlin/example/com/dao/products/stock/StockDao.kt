package example.com.dao.products.stock

interface StockDao{
    suspend fun increaseStocks(productId:String,shopId:String,quantity:Int):StockEntity?
    suspend fun decreaseStocks(productId:String,shopId:String,quantity:Int):StockEntity?
    suspend fun getStocks(productId:String,shopId:String):StockEntity?

}