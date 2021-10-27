package com.dan.newsfeedswithinmvvm.ui.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dan.newsfeedswithinmvvm.ui.models.Article

/**
 * Created by Dan Kim
 */
@Database(entities = [Article::class], version = 2)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        const val DATABASE_NAME: String = "article_db.db"

    }

    //Room DB is defined as Hilt injection module

//    companion object {
//
//        @Volatile
//        private var instance: ArticleDatabase? = null
//        private val lock = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
//            instance ?: createDatabase(context).also { instance = it }
//        }
//
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                ArticleDatabase::class.java,
//                "article_db.db"
//            ).build()
//    }
}