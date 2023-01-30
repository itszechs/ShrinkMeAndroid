package zechs.shrinkme.data.model

import androidx.annotation.Keep

@Keep
data class ShortenLinkRequest(
    val originalUrl: String,
    val shortenUrl: String? = null
)