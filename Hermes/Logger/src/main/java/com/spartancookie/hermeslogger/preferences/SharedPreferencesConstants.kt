package com.spartancookie.hermeslogger.preferences

internal object SharedPreferencesConstants {

    private const val BaseFilesName = "com.spartancookie.hermeslogger"
    const val FiltersFileName = "$BaseFilesName.filter"

    const val StoredFiltersByTypeKey = "filter_by_type_key"
    const val StoredFiltersByTagKey = "filter_by_tag_key"

}