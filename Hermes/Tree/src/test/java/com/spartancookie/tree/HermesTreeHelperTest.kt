package com.spartancookie.tree

import org.junit.Test

class HermesTreeHelperTest {

    @Test
    fun test_with_no_tags() {
        val noTagsMsg = "|My random message with no tags"
        val response = HermesTreeHelper.getAssociatedTags(noTagsMsg)

        assert(response.message == noTagsMsg)
    }

    @Test
    fun test_with_one_tag() {
        val noTagsMsg = "|MY TAG|||HT||My random message with tags"
        val response = HermesTreeHelper.getAssociatedTags(noTagsMsg)

        assert(response.tags.first() == "MY TAG")
        assert(response.message == "My random message with tags")
    }

    @Test
    fun test_with_multiple_tags_tag() {
        val noTagsMsg = "|MY TAG|MY SECOND TAG|GG|OP||HT||My random message with tags"
        val response = HermesTreeHelper.getAssociatedTags(noTagsMsg)

        assert(response.tags[0] == "MY TAG")
        assert(response.tags[1] == "MY SECOND TAG")
        assert(response.tags[2] == "GG")
        assert(response.tags[3] == "OP")
        assert(response.message == "My random message with tags")

    }
}