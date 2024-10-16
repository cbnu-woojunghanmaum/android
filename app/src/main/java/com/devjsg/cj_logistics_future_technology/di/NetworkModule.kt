package com.devjsg.cj_logistics_future_technology.di

import android.content.Context
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.network.EmergencyReportApiService
import com.devjsg.cj_logistics_future_technology.data.network.EmergencyReportApiServiceImpl
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import com.devjsg.cj_logistics_future_technology.data.repository.EmergencyReportRepository
import com.devjsg.cj_logistics_future_technology.data.repository.FindAllMemberRepository
import com.devjsg.cj_logistics_future_technology.data.repository.HeartRateRepository
import com.devjsg.cj_logistics_future_technology.data.repository.ManageMemberRepository
import com.devjsg.cj_logistics_future_technology.data.repository.MemberRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        dataStoreManager: DataStoreManager
    ): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("api log: $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }

            defaultRequest {
                headers.append("Accept", "application/json")
            }
        }
    }

    @Provides
    @Singleton
    fun provideMemberApiService(httpClient: HttpClient): MemberApiService {
        return MemberApiService(httpClient)
    }

    @Provides
    @Singleton
    fun provideEmergencyReportApiService(httpClient: HttpClient): EmergencyReportApiService {
        return EmergencyReportApiServiceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideMemberRepository(
        apiService: MemberApiService,
        dataStoreManager: DataStoreManager
    ): MemberRepository {
        return MemberRepository(apiService, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideEmergencyReportRepository(
        apiService: EmergencyReportApiService,
        dataStoreManager: DataStoreManager
    ): EmergencyReportRepository {
        return EmergencyReportRepository(apiService, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideHeartRateRepository(
        @ApplicationContext context: Context
    ): HeartRateRepository {
        return HeartRateRepository(context)
    }

    @Provides
    @Singleton
    fun provideFindAllMemberRepository(
        apiService: MemberApiService,
        dataStoreManager: DataStoreManager
    ): FindAllMemberRepository {
        return FindAllMemberRepository(apiService, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideManageMemberRepository(
        apiService: MemberApiService,
        dataStoreManager: DataStoreManager
    ): ManageMemberRepository {
        return ManageMemberRepository(apiService, dataStoreManager)
    }
}