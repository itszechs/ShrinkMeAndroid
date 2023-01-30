package zechs.shrinkme.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import zechs.shrinkme.R
import zechs.shrinkme.databinding.ActivityMainBinding
import zechs.shrinkme.utils.copyTextToClipboard

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShorten.setOnClickListener {
            val longUrl = binding.textField.editText!!.text.toString()
            Log.i(TAG, "Shorten button clicked: $longUrl")
            Log.d(TAG, "Is input field empty: ${longUrl.isEmpty()}")

            if (longUrl.isNotEmpty()) {
                viewModel.shortenLink(longUrl)
            } else {
                showSnackBar("Please enter a valid url")
            }
        }

        binding.btnCopy.setOnClickListener {
            val shortUrl = binding.outputText.text.toString()

            if (shortUrl.isNotEmpty()) {
                copyTextToClipboard(this, "Shorten url", shortUrl)
                showSnackBar("Copied to clipboard")
            } else {
                showSnackBar("No shorten url to copy")
            }
        }

        setupShortenLinkObserver()
    }


    @Keep
    private data class SnackBarAction(
        @StringRes val resId: Int,
        val listener: View.OnClickListener
    )

    private fun showSnackBar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        action: SnackBarAction? = null
    ) {
        Log.i(TAG, "Showing snackbar: $message")

        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).also {
            action?.let { a ->
                it.setAction(a.resId, a.listener)
            }
            it.duration = duration
        }.show()
    }

    private fun setupShortenLinkObserver() {
        viewModel.shortenLinkState.observe(this) { state ->
            when (state) {
                ShortenLinkUiState.Loading -> {
                    hasLink(false)
                    isLoading(true)
                }
                is ShortenLinkUiState.Failure -> {
                    hasLink(false)
                    isLoading(false)
                    showSnackBar(state.message)
                }
                is ShortenLinkUiState.Success -> {
                    hasLink(true)
                    isLoading(false)
                    binding.outputText.text = state.shortLink
                    showSnackBar(
                        message = "Link shortened successfully",
                        action = SnackBarAction(
                            resId = R.string.visit
                        ) {
                            openLink(state.shortLink)
                        }
                    )
                }

            }
        }
    }

    private fun openLink(shortLink: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = shortLink.toUri()
            startActivity(this)
        }
    }

    private fun isLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.textField.editText!!.isEnabled = !loading
        binding.btnShorten.isEnabled = !loading
    }

    private fun hasLink(has: Boolean) {
        binding.outputCard.visibility = if (has) View.VISIBLE else View.GONE
    }

}