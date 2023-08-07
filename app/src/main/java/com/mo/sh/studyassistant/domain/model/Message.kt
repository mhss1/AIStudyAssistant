package com.mo.sh.studyassistant.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["id"],
            childColumns = ["chat_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("chat_id")
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "chat_id") val chatId: Long,
    val type: Int,
    val content: String,
    val time: Long,
    val attachment: String? = null,
    @ColumnInfo(name = "attachment_type")
    val attachmentType: Int? = null,
    @ColumnInfo(name = "content_is_pdf")
    val contentIsPdf: Boolean = false,
    @ColumnInfo(name = "attachment_content")
    val attachmentContent: String = "",
    @ColumnInfo(name = "attachment_file_name")
    val attachmentFileName: String? = null,
    @ColumnInfo(name = "pdf_content_type")
    val pdfContentType: String = ""
)

enum class MessageType {
    User,
    Bot
}

enum class MessageSection {
    Tutor,
    Summarizer,
    Writer,
    Questions
}

enum class PDFContentType {
    Summary,
    Questions,
    Essay
}