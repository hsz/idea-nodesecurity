package mobi.hsz.idea.nodesecurity.scanner

import mobi.hsz.idea.nodesecurity.models.Advisory
import java.lang.ref.WeakReference

class AdvisoryReference(other: Advisory?) : WeakReference<Advisory?>(other) {
    constructor() : this(null)

    fun or(other: () -> Advisory?): AdvisoryReference = when {
        this.get() !== null -> this
        else -> AdvisoryReference(other())
    }
}
