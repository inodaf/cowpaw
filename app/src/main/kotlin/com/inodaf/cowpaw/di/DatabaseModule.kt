package com.inodaf.cowpaw.di

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.inodaf.cowpaw.utils.DbHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDbHelper(@ApplicationContext context: Context): SQLiteOpenHelper {
        return DbHelper(context)
    }
}

