package com.spartancookie.hermeslogger.debugToaster

import com.spartancookie.hermeslogger.models.LogDataHolder

/**
 * Build extra info. Assess if there is extra info to be copied. If so, append GenericInfo...
 *
 * @param dataHolder    Data Holder that may contain extra info
 * @return  ToastDataHolder with either no extra info, or with extra info + generic info
 */
internal fun buildGenericInfo(dataHolder: LogDataHolder) : LogDataHolder {
    return dataHolder.apply {
        genericInfo = fetchSystemSnapshot()
    }
}

internal fun fetchSystemSnapshot(): String? {
    val buildable = HermesConfigurations.systemInfoBuildable
    return buildable?.buildSystemSnapshotInfo()
}