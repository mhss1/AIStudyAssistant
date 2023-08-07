package com.mo.sh.studyassistant.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PalmTextResponse(
    val candidates: List<PalmOutputText>
)
