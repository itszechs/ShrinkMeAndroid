package zechs.shrinkme.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import zechs.shrinkme.data.model.ShortenLinkRequest
import zechs.shrinkme.data.model.ShortenLinkResponse

interface ShrinkMeApi {

    @POST("/api/v1/links")
    suspend fun shortenLink(
        @Body shortenLinkRequest: ShortenLinkRequest
    ): ShortenLinkResponse

}