package com.task.noteapp.di

import com.task.noteapp.data.local.LocalNotesRepository
import com.task.noteapp.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotesRepository(localNotesRepository: LocalNotesRepository): NotesRepository
}