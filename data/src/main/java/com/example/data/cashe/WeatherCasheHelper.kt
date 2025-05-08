package com.example.data.cashe

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WeatherCacheHelper(context: Context) : SQLiteOpenHelper(context, "weather_db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE weather_cache (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                json TEXT NOT NULL,
                timestamp INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS weather_cache")
        onCreate(db)
    }

    fun saveWeatherData(json: String) {
        writableDatabase.execSQL("DELETE FROM weather_cache")
        val stmt = writableDatabase.compileStatement("INSERT INTO weather_cache(json, timestamp) VALUES (?, ?)")
        stmt.bindString(1, json)
        stmt.bindLong(2, System.currentTimeMillis())
        stmt.executeInsert()
    }

    fun getLastWeatherData(): String? {
        val cursor = readableDatabase.rawQuery("SELECT json FROM weather_cache LIMIT 1", null)
        return if (cursor.moveToFirst()) {
            val json = cursor.getString(0)
            cursor.close()
            json
        } else {
            cursor.close()
            null
        }
    }
}
