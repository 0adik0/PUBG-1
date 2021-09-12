package battle.royale.bestapps.favorites

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM product WHERE photo LIKE :photo")
    fun findByPhoto(photo: String): List<Product>

    @Insert
    fun insertAll(vararg product: Product)

    @Delete
    fun delete(product: Product)
    
    @Update
    fun updateProduct(vararg product: Product)
}