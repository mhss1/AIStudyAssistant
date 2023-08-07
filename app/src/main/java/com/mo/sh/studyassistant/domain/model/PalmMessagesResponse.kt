package com.mo.sh.studyassistant.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class PalmMessagesResponse(
    val candidates: List<PalmMessage>
)