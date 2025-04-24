package com.example.scraphub

import android.content.Context
import androidx.room.Room
import database.ScrapDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.UUID

private const val DATABASE_NAME = "scrap-database"
class ScrapRepository private constructor(context: Context,
                                          private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: ScrapDatabase = Room.databaseBuilder(
        context.applicationContext,
        ScrapDatabase::class.java,
        DATABASE_NAME // name of db file you want Room to create for you
    )
        .build()

    fun getScraps(): Flow<List<Scrap>> = database.scrapDao().getScraps()

    suspend fun getScrap(id: UUID): Scrap = database.scrapDao().getScrap(id)

    // expose new function through repo
    // suspend no lonhger needed because repo handles managing work of interacting with coroutine scope
    fun updateScrap(scrap: Scrap) {
        coroutineScope.launch {
            database.scrapDao().updateScrap(scrap)
        }
    }

    // add scrap
    suspend fun addScrap(scrap: Scrap) {
        database.scrapDao().addScrap(scrap)
    }


    // create  singleton design pattern
    companion object {
        private var INSTANCE: ScrapRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ScrapRepository(context)
            }
        }
        // access the repo
        fun get(): ScrapRepository {
            return INSTANCE ?:
            throw IllegalStateException("ScrapRepostiory must be initialized")
        }
    }
}