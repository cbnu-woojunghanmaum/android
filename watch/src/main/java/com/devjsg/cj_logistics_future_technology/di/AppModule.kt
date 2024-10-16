package com.devjsg.cj_logistics_future_technology.di

import android.content.Context
import com.devjsg.cj_logistics_future_technology.data.repository.HeartRateRepository
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetHeartRateUseCase
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
    fun provideHeartRateRepository(
        @ApplicationContext context: Context
    ): HeartRateRepository {
        return HeartRateRepository(context)
    }

    @Provides
    @Singleton
    fun provideGetHeartRateUseCase(
        repository: HeartRateRepository
    ): GetHeartRateUseCase {
        return GetHeartRateUseCase(repository)
    }
}