package com.spartancookie.tree

import com.spartancookie.hermeslogger.core.HermesConfigurations
import com.spartancookie.tree.HermesTimberConstants.SUCCESS
import timber.log.Timber

/**
 * Timber tree wrapper, used to make it easier to add Hermes tags and other functionalities to the logs being fired.
 */
class HermesTreeWrapper internal constructor(private val refTree: Timber.Tree, private val tags: MutableList<String>): Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message.addTags(), t)
    }

    // Success logs - Mapped to info

    fun s(message: String?, vararg args: Any?) {
        addTag(SUCCESS)
        refTree.i(message?.addTags(), args)
    }

    // Custom logs

    override fun log(priority: Int, message: String?, vararg args: Any?) = refTree.log(priority, message?.addTags(), *args)

    override fun log(priority: Int, t: Throwable?) = refTree.log(priority, t)

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) = refTree.log(priority, message?.addTags(), args)

    // Verbose logs

    override fun v(message: String?, vararg args: Any?) = refTree.v(message?.addTags(), *args)

    override fun v(t: Throwable?) = refTree.v(t)

    override fun v(t: Throwable?, message: String?, vararg args: Any?) = refTree.v(t, message?.addTags(), *args)

    // Info logs

    override fun i(message: String?, vararg args: Any?) = refTree.i(message?.addTags(), *args)

    override fun i(t: Throwable?) = refTree.i(t)

    override fun i(t: Throwable?, message: String?, vararg args: Any?) = refTree.i(t, message, *args)

    // Debug logs

    override fun d(message: String?, vararg args: Any?) = refTree.d(message?.addTags(), *args)

    override fun d(t: Throwable?) = refTree.d(t)

    override fun d(t: Throwable?, message: String?, vararg args: Any?) = refTree.d(t, message?.addTags(), *args)

    // Warning logs

    override fun w(message: String?, vararg args: Any?) = refTree.w(message?.addTags(), *args)

    override fun w(t: Throwable?) = refTree.w(t)

    override fun w(t: Throwable?, message: String?, vararg args: Any?) = refTree.w(t, message, *args)

    // Error logs

    override fun e(message: String?, vararg args: Any?) = refTree.e(message?.addTags(), *args)

    override fun e(t: Throwable?) = refTree.e(t)

    override fun e(t: Throwable?, message: String?, vararg args: Any?) = refTree.e(t, message, *args)

    // WTF logs

    override fun wtf(message: String?, vararg args: Any?) = refTree.wtf(message, *args)

    override fun wtf(t: Throwable?) = refTree.wtf(t)

    override fun wtf(t: Throwable?, message: String?, vararg args: Any?) = refTree.wtf(t, message, *args)

    private fun String.addTags(): String {
        val delimiter = HermesConfigurations.tagDelimiter
        return buildString {
            tags.forEach {
                append("$delimiter$it$delimiter")
            }
            append(this@addTags)
        }
    }

    internal fun addTag(tag: String) = tags.add(tag)
}

/**
 * Create a Wrapper with the @receiver using the [HermesTreeWrapper] class.
 * Additionally, the tags are stored in this wrapper.
 * @receiver instance of [Timber.Forest]
 */
fun Timber.Forest.hermesTags(vararg tags: String): HermesTreeWrapper {
    return HermesTreeWrapper(this, tags.toMutableList())
}

/**
 * Create a Wrapper with the @receiver using the [HermesTreeWrapper] class.
 * Stores a single tag in this wrapper
 * @receiver instance of [Timber.Forest]
 */
fun Timber.Forest.hermesTag(tag: String): HermesTreeWrapper {
    return hermesTags(tag)
}

/**
 * Adds a tag into the already created [HermesTreeWrapper] instance
 * @receiver instance of [HermesTreeWrapper]
 */
fun HermesTreeWrapper.hermesTag(tag: String) = apply { addTag(tag) }