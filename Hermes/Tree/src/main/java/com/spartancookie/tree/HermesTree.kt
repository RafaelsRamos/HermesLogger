package com.spartancookie.tree

import android.util.Log
import com.spartancookie.tree.HermesTimberConstants.SUCCESS
import com.spartancookie.tree.HermesTreeHelper.createStackElementTag
import com.spartancookie.hermeslogger.core.Hermes
import com.spartancookie.hermeslogger.core.HermesBuilder
import com.spartancookie.hermeslogger.core.HermesConfigurations
import timber.log.Timber

class HermesTree: Timber.Tree() {

    private val fqcnIgnore = listOf(
        Timber::class.java.name,
        Timber.Forest::class.java.name,
        Timber.Tree::class.java.name,
        Timber.DebugTree::class.java.name
    )

    private val extractTag: String
        get() = Throwable().stackTrace
            .first { it.className !in fqcnIgnore }
            .let(::createStackElementTag)

    override fun log(priority: Int, tag: String?, msg: String, t: Throwable?) {

        if (!HermesConfigurations.isEnabled) {
            return
        }

        // Process msg to split it into the real message + list of tags
        val (message, tags) = HermesTreeHelper.getAssociatedTags(msg)

        val hermesBuilder = if (tags.contains(SUCCESS)) {
            Hermes.success()
        } else {
            priority.toHermesBuilder()
        }
        hermesBuilder?.addContent(tag, message, t, tags)
    }

    private fun Int.toHermesBuilder() = when(this) {
        Log.VERBOSE -> Hermes.v()
        Log.DEBUG -> Hermes.d()
        Log.INFO -> Hermes.i()
        Log.WARN -> Hermes.w()
        Log.ERROR -> Hermes.e()
        Log.ASSERT -> Hermes.wtf()
        else -> null
    }

    private fun HermesBuilder.addContent(tag: String?, content: String, throwable: Throwable?, tags: List<String>) {

        val wholeContent = buildString {
            append(content)
            throwable?.let { t -> append("\n${t.message}") }
        }

        addTags(*tags.toTypedArray())
        message(tag ?: extractTag)
        extraInfo(wholeContent)
        submit()
    }

}