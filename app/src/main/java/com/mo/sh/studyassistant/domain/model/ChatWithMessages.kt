package com.mo.sh.studyassistant.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class ChatWithMessages(
    @Embedded val chat: Chat,
    @Relation(
        parentColumn = "id",
        entityColumn = "chat_id"
    )
    val messages: List<Message>
)
