package zechs.shrinkme.di

import com.squareup.moshi.Moshi
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import zechs.shrinkme.data.remote.ShrinkMeApi
import zechs.shrinkme.data.repository.ShrinkMeRepository
import zechs.shrinkme.utils.Constants.Companion.SHRINKME_API
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
object ShrinkMeModule {

    @Provides
    @Singleton
    fun provideShrinkMe(
        client: OkHttpClient,
        moshi: Moshi
    ): ShrinkMeApi {
        return Retrofit.Builder()
            .baseUrl(SHRINKME_API)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ShrinkMeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShrinkMeRepository(
        shrinkMeApi: ShrinkMeApi
    ): ShrinkMeRepository {
        return ShrinkMeRepository(shrinkMeApi)
    }

}