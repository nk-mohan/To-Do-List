package com.makeover.todolist.di.modules

import android.content.Context
import com.makeover.todolist.databinding.SettingsDataBinding
import com.makeover.todolist.view.customviews.AlertDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class UtilityModule {

//    @Provides
//    @Singleton
//    fun provideSettingsDataBinding(@ApplicationContext app: Context) = SettingsDataBinding(app)
//
//    @Provides
//    @Singleton
//    fun provideAlertDialog(@ApplicationContext app: Context) = AlertDialog(app)
}