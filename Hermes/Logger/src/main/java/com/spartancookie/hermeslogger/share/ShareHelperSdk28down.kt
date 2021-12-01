package com.spartancookie.hermeslogger.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.spartancookie.hermeslogger.share.ShareConstants.FILE_NAME
import com.spartancookie.hermeslogger.share.ShareConstants.PLAIN_TEXT_INTENT_TYPE
import com.spartancookie.hermeslogger.utils.createTempFileFromFile
import java.io.File

internal object ShareHelperSdk28down: IFileSharable {

    override fun shareLogDump(context: Context) {
        val file = createLogDumpFile(context)

        // Fill file with the data from the log stack
        file.bufferedWriter().use { out -> out.write(ShareHelperCommon.getDumpLogContent()) }

        shareFile(context, file)
    }

    private fun createLogDumpFile(context: Context) = File(context.filesDir, FILE_NAME).also {
        if (it.createNewFile()) {
            Log.v("HermesShare", "$FILE_NAME was created successfully.")
        } else {
            Log.v("HermesShare", "$FILE_NAME already exists.")
        }
    }

    private fun shareFile(context: Context, file: File) {
        val tempFile = createTempFileFromFile(context, FILE_NAME, file)

        // Work around to ignore file exposure
        StrictMode.setVmPolicy(VmPolicy.Builder().build())

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = PLAIN_TEXT_INTENT_TYPE
            putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile))
        }
        (context as? Activity)?.run { startActivity(Intent.createChooser(intent, "Share log stack")) }
    }
}