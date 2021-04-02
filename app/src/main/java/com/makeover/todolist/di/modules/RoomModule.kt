package com.makeover.todolist.di.modules

import android.content.Context
import androidx.room.Room
import com.makeover.todolist.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationComponent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "TODOLIST"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideTaskCategoryDao(db: AppDatabase) =
        db.taskCategoryDao() // The reason we can implement a Dao for the database
}