package com.spartancookie.hermeslogger.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.spartancookie.hermeslogger.share.ShareConstants.FILE_NAME
import com.spartancookie.hermeslogger.share.ShareConstants.PLAIN_TEXT_INTENT_TYPE
import com.spartancookie.hermeslogger.share.ShareConstants.TEXT_FILE_INTENT_TYPE
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Share logic using [scoped storage][https://developer.android.com/about/versions/11/privacy/storage], necessary for Android 11+ and Advisable for Android 10
 */
internal object ShareHelperSdk29Up: IFileSharable {

    private lateinit var activityResultOnSavedFile: ActivityResultLauncher<Intent>

    override fun initializeFeature(context: Context) {
        val activity = context as? ComponentActivity

        // Register for activity response
        activity?.run {
            activityResultOnSavedFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    it.data?.data?.let { uri ->
                        // Ask user to select the app he wants to share with selected file
                        writeFileContent(uri)
                        shareFile(this, uri)
                    }
                }
            }
        }
    }

    override fun shareLogDump(context: Context) {
        // Save file locally. If successful, Share will be triggered by activityResultOnSavedFile implementation
        saveFileLocally()
    }

    private fun saveFileLocally() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = TEXT_FILE_INTENT_TYPE
        intent.putExtra(Intent.EXTRA_TITLE, "$FILE_NAME.txt")
        activityResultOnSavedFile.launch(intent)
    }

    /**
     * Ask the user to choose a App with which he/she wants to share the
     * file located at the [uri] with.
     */
    private fun shareFile(context: Context, uri: Uri) {
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = PLAIN_TEXT_INTENT_TYPE
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        (context as? Activity)?.run { startActivity(Intent.createChooser(intent, "Share log stack")) }
    }

    /**
     * Fill the file in the given [uri] with the Log dump data
     */
    private fun Context.writeFileContent(uri: Uri?) {
        try {
            val file = uri?.let { this.contentResolver.openFileDescriptor(it, "w") }

            file?.let {
                val content = ShareHelperCommon.getDumpLogContent()
                val fileOutputStream = FileOutputStream(it.fileDescriptor)
                fileOutputStream.write(content.toByteArray())
                fileOutputStream.close()

                it.close()
            }

        } catch (e: FileNotFoundException) {
            Log.w("HermesShare", "Target file not found. $e")
        } catch (e: IOException) {
            Log.w("HermesShare", e)
        }
    }

}