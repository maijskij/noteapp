package com.task.noteapp.di

import com.task.noteapp.data.NotesRepositoryImpl
import com.task.noteapp.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class NotesModule {

    @Binds
    @ViewModelScoped
    abstract fun bindNotesRepository(notesRepositoryImpl: NotesRepositoryImpl): NotesRepository

}