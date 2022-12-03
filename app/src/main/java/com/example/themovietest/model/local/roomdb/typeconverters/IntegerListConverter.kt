package com.example.themovietest.model.local.roomdb.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntegerListConverter {

    @TypeConverter
    fun toString(values: List<Int>): String {
        var gson = Gson()
        return gson.toJson(values)
    }

    @TypeConverter
    fun fromString(value: String): List<Int> {
        var list = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson<List<Int>>(value, list)
    }
}