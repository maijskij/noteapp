package com.task.noteapp.di

import android.content.Context
import androidx.room.Room
import com.task.noteapp.data.local.NotesDao
import com.task.noteapp.data.local.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoroutineDispatcherIO


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @CoroutineDispatcherIO
    @Provides
    fun provideIoCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): NotesDao {
        return Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            "Notes.db"
        ).build().notesDao()
    }
}