package com.depvt.live.football.tv.emulator

sealed class DeviceState {

    class Emulator(
        val source: VerdictSource
    ) : DeviceState()

    object NotEmulator : DeviceState()
}