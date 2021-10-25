package com.dan.newsfeedswithinmvvm.ui.db

import androidx.room.TypeConverter
import com.dan.newsfeedswithinmvvm.ui.models.Source

/**
 * Created by Dan Kim
 */
class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}