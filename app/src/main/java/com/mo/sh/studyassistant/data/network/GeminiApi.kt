package com.mo.sh.studyassistant.data.network

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig


class GeminiApi {

    private val safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
    )
    private val temp = 0.4f
    private val candidates = 1


    suspend fun generateMessage(
         history: List<Content>,
         message: String,
         key: String
    ): String? {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.0-pro-latest",
            apiKey = key,
            generationConfig = generationConfig {
                temperature = temp
                maxOutputTokens = 1024
                candidateCount = candidates
            },
            safetySettings = safetySettings
        )
        return generativeModel.startChat(
            history
        ).sendMessage(message).text
    }

    suspend fun generateText(
        prompt: String,
        key: String,
        ): String? {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.0-pro-latest",
            apiKey = key,
            generationConfig = generationConfig {
                temperature = temp
                maxOutputTokens = 2048
                candidateCount = candidates
            },
            safetySettings = safetySettings
        )
       return generativeModel.generateContent(prompt).text
    }

}