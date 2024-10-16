package com.devjsg.cj_logistics_future_technology.di.worker

import com.devjsg.cj_logistics_future_technology.data.work.HeartRateWorker
import com.devjsg.cj_logistics_future_technology.data.work.ReportLocationWorker
import com.devjsg.cj_logistics_future_technology.data.work.StepCounterWorker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {

    @Binds
    @IntoMap
    @StringKey("com.devjsg.cj_logistics_future_technology.data.work.ReportLocationWorker")
    abstract fun bindReportLocationWorker(factory: ReportLocationWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @StringKey("com.devjsg.cj_logistics_future_technology.data.work.HeartRateWorker")
    abstract fun bindHeartRateWorker(factory: HeartRateWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @StringKey("com.devjsg.cj_logistics_future_technology.data.work.StepCounterWorker")
    abstract fun bindStepCounterWorker(factory: StepCounterWorker.Factory): ChildWorkerFactory
}