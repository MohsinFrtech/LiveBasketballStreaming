package com.depvt.live.football.tv.date

import com.depvt.live.football.tv.utils.objects.Constants
import se.simbio.encryption.Encryption

class ProcessingFile {

    fun getChannelType(strToDecrypt: String?):String
    {
        val iv = ByteArray(Constants.mySecretSize)
        val encryption: Encryption = Encryption.getDefault(
            Constants.myUserLock1,
            Constants.myUserCheck1, iv)

        return encryption.decryptOrNull(strToDecrypt)
    }
}