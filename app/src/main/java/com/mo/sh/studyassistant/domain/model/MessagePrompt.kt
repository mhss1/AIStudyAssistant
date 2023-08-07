package com.mo.sh.studyassistant.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MessagePrompt(
    val context: String,
    val messages: List<PalmMessage>
)
