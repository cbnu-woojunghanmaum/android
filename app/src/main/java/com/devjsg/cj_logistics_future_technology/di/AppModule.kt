package com.devjsg.cj_logistics_future_technology.di

import android.content.Context
import com.devjsg.cj_logistics_future_technology.data.biometric.KeystoreHelper
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideKeystoreHelper(@ApplicationContext context: Context): KeystoreHelper {
        return KeystoreHelper(context)
    }
}