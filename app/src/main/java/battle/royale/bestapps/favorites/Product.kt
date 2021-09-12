package battle.royale.bestapps.favorites

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(

    @ColumnInfo(name = "id") var id: String,

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "photo") var photo: String,

    @ColumnInfo(name = "link") var link: String,
    @ColumnInfo(name = "title") var title: String

)