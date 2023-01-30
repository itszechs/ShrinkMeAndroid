package zechs.shrinkme.data.repository

import zechs.shrinkme.data.model.ShortenLinkRequest
import zechs.shrinkme.data.model.ShortenLinkResponse
import zechs.shrinkme.data.remote.ShrinkMeApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShrinkMeRepository @Inject constructor(
    private val api: ShrinkMeApi
) {

    suspend fun getShortUrl(
        longUrl: String
    ): ShortenLinkResponse {
        return api.shortenLink(
            ShortenLinkRequest(originalUrl = longUrl)
        )
    }

}