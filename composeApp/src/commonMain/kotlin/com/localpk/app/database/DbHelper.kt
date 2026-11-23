package com.localpk.app.database

import com.localpk.app.DealCartDb
import com.localpk.app.dbFactory.DatabaseFactory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DbHelper(
    private val driverFactory: DatabaseFactory
) {
    private var db:DealCartDb? = null
    private val mutex = Mutex()

    suspend fun <Result: Any> withDatabase(block: suspend (DealCartDb) -> Result) = mutex.withLock{
        if (db == null){
            db = DealCartDb(driverFactory.createDriver())
        }
        return@withLock block(db!!)
    }

    private suspend fun createDb(driverFactory: DatabaseFactory):DealCartDb {
        return DealCartDb(driverFactory.createDriver())
    }
}