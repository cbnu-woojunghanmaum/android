package com.devjsg.cj_logistics_future_technology.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @Provides
    fun provideBiometricPromptHelper(
        @ActivityContext context: Context,
        activity: FragmentActivity
    ): BiometricPromptHelper {
        return BiometricPromptHelper(context, activity)
    }
}