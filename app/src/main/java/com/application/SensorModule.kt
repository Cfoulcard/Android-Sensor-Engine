package com.application

import android.app.Application
import com.sensors.sensorMechanics.LightSensor
import com.sensors.sensorMechanics.ObserveSensor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideLightSensor(app: Application) : ObserveSensor {
        return LightSensor(app)
    }
}