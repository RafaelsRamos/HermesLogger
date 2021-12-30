package com.spartancookie.tree

import android.util.Log
import com.spartancookie.tree.HermesTimberConstants.SUCCESS
import com.spartancookie.tree.HermesTreeHelper.createStackElementTag
import com.spartancookie.hermeslogger.core.Hermes
import com.spartancookie.hermeslogger.core.HermesBuilder
import com.spartancookie.hermeslogger.core.HermesConfigurations
import com.spartancookie.tree.HermesTimberConstants.TIMBER_TAG
import timber.log.Timber

/**
 * Hermes Timber tree, that can be used to map all the timber logs onto the Hermes event system.
 */
class HermesTree: Timber.Tree() {

    private val fqcnIgnore = listOf(
        Timber::class.java.name,
        Timber.Forest::class.java.name,
        Timber.Tree::class.java.name,
        Timber.DebugTree::class.java.name,
        HermesTree::class.java.name,
        HermesTreeWrapper::class.java.name,
    )

    private val extractTag: String
        get() = Throwable().stackTrace
            .first { it.className !in fqcnIgnore }
            .let(::createStackElementTag)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val hasDebugTree = Timber.forest().any { it is Timber.DebugTree }

        if (!hasDebugTree || !HermesConfigurations.isEnabled) {
            return
        }

        // Process msg to split it into the real message + list of tags
        val (msg, tags) = HermesTreeHelper.getAssociatedTags(message)

        val hermesBuilder = if (tags.contains(SUCCESS)) {
            Hermes.success()
        } else {
            priority.toHermesBuilder()
        }
        hermesBuilder?.addContent(tag, msg, t, tags)
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

        tag(TIMBER_TAG)
        tags(*tags.toTypedArray())
        message(tag ?: extractTag)
        description(wholeContent)
        submit()
    }

}