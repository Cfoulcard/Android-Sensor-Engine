package com.sensors.sensorMechanics

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LightSensor

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class PressureSensor

    @Provides
    @Singleton
    @LightSensor
    fun provideLightSensor(app: Application) : ObserveSensor {
        return LightSensor(app)
    }

    @Provides
    @Singleton
    @PressureSensor
    fun providePressureSensor(app: Application) : ObserveSensor {
        return PressureSensor(app)
    }

//    @Provides
//    @Singleton
//    fun provideAmbientTemperatureSensor(app: Application) : ObserveSensor {
//        return AmbientTemperatureSensor(app)
//    }
//
//    @Provides
//    @Singleton
//    fun provideHumiditySensor(app: Application) : ObserveSensor {
//        return HumiditySensor(app)
//    }
}