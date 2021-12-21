package com.spartancookie.tree

import com.spartancookie.tree.HermesTimberConstants.SUCCESS
import com.spartancookie.tree.HermesTimberConstants.TAG_SECTION_DELIMITER
import timber.log.Timber

class HermesForestWrapper(private val refTree: Timber.Tree, private val tags: MutableList<String>): Timber.Tree() {

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
        val tagsSection = buildString {
            tags.forEach {
                append("|$it|")
            }
            append(TAG_SECTION_DELIMITER)
        }

        return tagsSection + this
    }

    internal fun addTag(tag: String) = tags.add(tag)
}

/**
 * Create a Wrapper with the @receiver using the [HermesForestWrapper] class.
 * Additionally, the tags are stored in this wrapper.
 * @receiver instance of [Timber.Forest]
 */
fun Timber.Forest.hermesTags(vararg tags: String): HermesForestWrapper {
    return HermesForestWrapper(this, tags.toMutableList())
}

/**
 * Create a Wrapper with the @receiver using the [HermesForestWrapper] class.
 * Stores a single tag in this wrapper
 * @receiver instance of [Timber.Forest]
 */
fun Timber.Forest.addHermesTag(tag: String): HermesForestWrapper {
    return hermesTags(tag)
}

/**
 * Adds a tag into the already created [HermesForestWrapper] instance
 * @receiver instance of [HermesForestWrapper]
 */
fun HermesForestWrapper.addHermesTag(tag: String) = apply { addTag(tag) }