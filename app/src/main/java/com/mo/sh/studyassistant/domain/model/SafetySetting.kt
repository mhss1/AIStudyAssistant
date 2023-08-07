package com.mo.sh.studyassistant.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SafetySetting(
    val category: String,
    val threshold: Int = 3
)

enum class SafetyCategory {
    HARM_CATEGORY_DEROGATORY,
    HARM_CATEGORY_TOXICITY,
    HARM_CATEGORY_VIOLENCE,
    HARM_CATEGORY_SEXUAL,
    HARM_CATEGORY_MEDICAL,
    HARM_CATEGORY_DANGEROUS
}