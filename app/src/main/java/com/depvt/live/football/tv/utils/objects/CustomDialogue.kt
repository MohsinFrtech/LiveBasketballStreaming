package com.depvt.live.football.tv.utils.objects

import android.content.Context
import android.util.Log
import com.depvt.live.football.tv.utils.interfaces.DialogListener

class CustomDialogue(var dialogListener: DialogListener) {

    fun showDialog(
        context: Context?, title: String?, message: String?,
        positiveText: String?, negativeText: String?,key: String
    ) {
        try {
            context?.let {
                androidx.appcompat.app.AlertDialog.Builder(it)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(
                        positiveText
                    ) { _, _ -> dialogListener.onPositiveDialogText(key) }
                    .setNegativeButton(
                        negativeText
                    ) { _, _ -> dialogListener.onNegativeDialogText(key) }
                    .show()
            }

        }
        catch (e:Exception)
        {
            Log.d("Exception","msg"+e.message)
        }

    }

}