package com.spartancookie.hermeslogger.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.spartancookie.hermeslogger.debugToaster.HermesHandler
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.models.LogDataHolder
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


private const val PLAIN_TEXT_INTENT_TYPE = "text/plain"
private const val TEXT_FILE_INTENT_TYPE = "text/*"

private const val FILE_NAME = "hl_log_stack"
private const val HEADER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
private const val FOOTER = "\n\n-------------------------- END OF STACK --------------------------"

private val creationDateFormat = SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault())

private val headerContent
    get() = "Hermes logger stack dump captured " +
            "at ${SimpleDateFormat(HEADER_DATE_FORMAT, Locale.getDefault()).format(Date())}"

private val logCountContent
    get() = "Log count: Success:${infoHolder.getNumberOfLogsByType(LogType.Success)} " +
            "Info:${infoHolder.getNumberOfLogsByType(LogType.Info)} " +
            "Warning:${infoHolder.getNumberOfLogsByType(LogType.Warning)} " +
            "Error:${infoHolder.getNumberOfLogsByType(LogType.Error)} " +
            "Debug:${infoHolder.getNumberOfLogsByType(LogType.Debug)}"

private val infoHolder = HermesHandler.infoHolder

/**
 * Share entire log stack
 */
internal fun shareLogDump(context: Context) {
    val file = createLogDumpFile(context)
    dumpLogStack(file)
    shareFile(context, file)
}

/**
 * Share a single log
 */
internal fun shareLog(context: Context, log: LogDataHolder) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = PLAIN_TEXT_INTENT_TYPE
        putExtra(Intent.EXTRA_SUBJECT, "${log.type} log captured at ${log.creationDate}- ${log.message}")
        putExtra(Intent.EXTRA_TEXT, buildInfoContentOnly(log))
    }
    (context as? Activity)?.run { startActivity(Intent.createChooser(intent, "Share log")) }
}

private fun createLogDumpFile(context: Context) = File(context.filesDir, FILE_NAME).also {
    if (it.createNewFile()) {
        println("$FILE_NAME was created successfully.")
    } else {
        println("$FILE_NAME already exists.")
    }
}

private fun dumpLogStack(file: File) {
    val fileContent = buildString {
        append("$headerContent\n")
        append("$logCountContent\n")
        append("\n    Logs    \n")
        infoHolder.logList.forEach { log ->
            append("\n\n|| ${creationDateFormat.format(log.creationDate)} || ${log.type}-${log.id}\nShort Message:${log.message}\nExtra Information:${log.extraInfo}\n")
        }
        append(FOOTER)
    }
    file.bufferedWriter().use { out -> out.write(fileContent) }
}

private fun shareFile(context: Context, file: File) {

    val tempFile = createTempFile(context, file)

    // Work around to ignore file exposure
    StrictMode.setVmPolicy(VmPolicy.Builder().build())

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = TEXT_FILE_INTENT_TYPE
        putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile))
    }
    (context as? Activity)?.run { startActivity(Intent.createChooser(intent, "Share log stack")) }
}

/**
 * Create a temp file that is a copy from [file]
 * @param context Context
 * @param file File that will be copied
 */
private fun createTempFile(context: Context, file: File): File {

    val tempFile = File.createTempFile(FILE_NAME, ".txt", context.externalCacheDir)

    val fw = FileWriter(tempFile)

    val fr = FileReader(file)
    var c: Int = fr.read()
    while (c != -1) {
        fw.write(c)
        c = fr.read()
    }
    fr.close()

    fw.flush()
    fw.close()

    return tempFile
}