package database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.scraphub.Scrap
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ScrapDao {
    @Query("SELECT * FROM scrap")
    fun getScraps(): Flow<List<Scrap>>

    @Query("SELECT * FROM scrap WHERE id=(:id)")
    suspend fun getScrap(id: UUID): Scrap

    @Update
    suspend fun updateScrap(scrap: Scrap)

    // add a way to insert scrap
    @Insert
    suspend fun addScrap(scrap: Scrap)


}