package zechs.shrinkme.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import zechs.shrinkme.utils.Constants.Companion.SHRINKME_API

fun createShortLink(path: String): String {
    return "${SHRINKME_API}/${path}"
}

fun copyTextToClipboard(context: Context, label: String, text: String) {
    val manager = context.getSystemService(
        Context.CLIPBOARD_SERVICE
    ) as ClipboardManager

    val clipData = ClipData.newPlainText(label, text)
    manager.setPrimaryClip(clipData)
}