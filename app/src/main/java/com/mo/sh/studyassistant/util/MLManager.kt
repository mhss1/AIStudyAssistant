package com.mo.sh.studyassistant.util

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mo.sh.studyassistant.R
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MLManager(private val context: Context) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private var image: InputImage? = null

    suspend fun recognizeText(uri: Uri): MLResult {
         return suspendCoroutine { continuation ->
            try {
                image = InputImage.fromFilePath(context, uri)
                recognizer.process(image!!)
                    .addOnSuccessListener { result ->

                        if (result.text.trim().count { it != ' ' } < 25) {
                            continuation.resume(MLResult.Failure(context.getString(R.string.no_text_recognized)))
                        } else {
                            continuation.resume(MLResult.Success(result.text))
                        }

                    }
                    .addOnFailureListener {
                        continuation.resume(MLResult.Failure(context.getString(R.string.error_extracting_text)))
                    }
            } catch (e: IOException) {
                continuation.resume(MLResult.Failure(context.getString(R.string.error_extracting_text)))
                e.printStackTrace()
            }
        }
    }

    sealed class MLResult(val text: String) {
        data class Success(val content: String) : MLResult(content)
        data class Failure(val message: String) : MLResult(message)
    }
}