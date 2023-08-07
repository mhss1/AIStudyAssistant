package com.mo.sh.studyassistant.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PalmTextPrompt(
    val prompt: PalmText,
    val safety_settings: List<SafetySetting> = SafetyCategory.values().map { SafetySetting(it.name) },
    val candidate_count: Int = 1,
    // 1024 is the maximum number of tokens that can be generated for a single request.
    // https://developers.generativeai.google/models/language#model_metadata
    val max_output_tokens: Int = 1024,
    val temperature: Double = 0.4,
)
