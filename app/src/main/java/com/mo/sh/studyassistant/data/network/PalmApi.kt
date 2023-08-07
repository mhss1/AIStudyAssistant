package com.mo.sh.studyassistant.data.network

import com.mo.sh.studyassistant.domain.model.PalmMessagesResponse
import com.mo.sh.studyassistant.domain.model.PalmTextPrompt
import com.mo.sh.studyassistant.domain.model.PalmTextResponse
import com.mo.sh.studyassistant.domain.model.PalmeMessagePrompt
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody


class PalmApi(
    private val client: HttpClient
) {

    suspend fun generateMessage(
         prompt: PalmeMessagePrompt,
         apiKey: String
    ): PalmMessagesResponse {
        return client.post(
            NetworkConstants.BASE_URL +
                    NetworkConstants.PALM_MESSAGES_MODEL +
                    apiKey
        ) {
            headers {
                append("Content-Type", "application/json")
            }
            setBody(prompt)
        }.body()
    }

    suspend fun generateText(
        prompt: PalmTextPrompt,
        apiKey: String
    ): PalmTextResponse {
        return client.post(
            NetworkConstants.BASE_URL +
                    NetworkConstants.PALM_TEXT_MODEL +
                    apiKey
        ) {
            headers {
                append("Content-Type", "application/json")
            }
            setBody(prompt)
        }.body()
    }

}