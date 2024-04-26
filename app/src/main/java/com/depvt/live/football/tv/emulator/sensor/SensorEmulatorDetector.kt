package com.depvt.live.football.tv.emulator.sensor

import android.content.Context
import com.depvt.live.football.tv.emulator.DeviceState
import com.depvt.live.football.tv.emulator.EmulatorDetector
import com.depvt.live.football.tv.emulator.VerdictSource

internal class SensorEmulatorDetector(
    context: Context,
    sensorType: Int,
    private val eventsCount: Int
) : EmulatorDetector() {

    private val sensorDataProcessor = SensorDataValidator()
    private val sensorEventProducer = SensorEventProducer(context, sensorType)

    override suspend fun getState(): DeviceState {
        val sensorEvents = sensorEventProducer.getSensorEvents(eventsCount)
            .onFailure { return DeviceState.NotEmulator }
            .getOrThrow()

        return if (sensorDataProcessor.isEmulator(sensorEvents)) {
            DeviceState.Emulator(VerdictSource.Sensors(sensorEvents))
        } else {
            DeviceState.NotEmulator
        }
    }
}