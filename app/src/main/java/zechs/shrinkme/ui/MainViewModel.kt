package zechs.shrinkme.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import zechs.shrinkme.data.model.ShortenLinkResponse
import zechs.shrinkme.data.repository.ShrinkMeRepository
import zechs.shrinkme.utils.createShortLink
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shrinkMeRepository: ShrinkMeRepository
) : ViewModel() {

    private val _shortenLinkState = MutableLiveData<ShortenLinkUiState>()
    val shortenLinkState: LiveData<ShortenLinkUiState> = _shortenLinkState

    fun shortenLink(
        longUrl: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        _shortenLinkState.postValue(ShortenLinkUiState.Loading)
        try {
            val response = shrinkMeRepository.getShortUrl(longUrl)
            if (response.isSuccessful) {
                _shortenLinkState.postValue(
                    ShortenLinkUiState.Success(shortLink = createShortLink(response.body()!!.message))
                )
            } else {
                _shortenLinkState.postValue(
                    ShortenLinkUiState.Failure(
                        response.body()?.message
                            ?: response.errorBody()?.string()?.let {
                                Gson().fromJson(it, ShortenLinkResponse::class.java).message
                            }
                            ?: "An unknown error occurred"
                    )
                )
            }
        } catch (e: Exception) {
            _shortenLinkState.postValue(
                ShortenLinkUiState.Failure(e.message ?: "Something went wrong!")
            )
        }
    }

}


sealed class ShortenLinkUiState {
    object Loading : ShortenLinkUiState()
    data class Success(val shortLink: String) : ShortenLinkUiState()
    data class Failure(val message: String) : ShortenLinkUiState()
}