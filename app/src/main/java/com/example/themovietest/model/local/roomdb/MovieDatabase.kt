package com.example.themovietest.model.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.themovietest.model.entity.Movie
import com.example.themovietest.model.local.roomdb.typeconverters.IntegerListConverter

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(value = [IntegerListConverter::class])
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}