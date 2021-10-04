package com.spartancookie.formatter

class Formatter private constructor() {

    companion object {

        @Deprecated(
            "Use DataType#format directly. For example, DataType.JSON.format(content)",
            ReplaceWith("dataType.format(content)")
        )
        @JvmStatic
        fun format(dataType: DataType, content: String): String = dataType.format(content)

    }

}