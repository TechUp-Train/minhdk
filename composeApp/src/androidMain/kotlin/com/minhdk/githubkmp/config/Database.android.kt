package com.minhdk.githubkmp.config

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.minhdk.githubkmp.data.core.storage.database.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("github_app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}