package com.localpk.app.dbFactory

import app.cash.sqldelight.db.SqlDriver

const val DATABASE_NAME = "DealCart.db"

expect class DatabaseFactory {
    suspend fun createDriver(): SqlDriver
}