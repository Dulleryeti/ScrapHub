package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scraphub.Scrap

@Database(entities = [ Scrap::class ], version = 1)


abstract class ScrapDatabase: RoomDatabase() {
    abstract fun scrapDao(): ScrapDao


}