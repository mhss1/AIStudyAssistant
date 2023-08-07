package com.mo.sh.studyassistant.domain.repository

import android.net.Uri
import com.mo.sh.studyassistant.domain.model.ChatWithMessages
import com.mo.sh.studyassistant.domain.model.Message
import com.mo.sh.studyassistant.domain.model.MessageSection
import com.mo.sh.studyassistant.domain.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun sendPrompt(content: String, section: MessageSection, apiKey: String, pdfUri: Uri?): NetworkResult

    suspend fun sendMessage(newMessageContent: String, apiKey: String, chat: ChatWithMessages?, imageUri: Uri?, pdfUri: Uri?): NetworkResult

    fun getSectionChats(section: Int): Flow<List<ChatWithMessages>>

    suspend fun clearChatContext()

    suspend fun resetAllChats(section: Int)

    suspend fun writePdfFile(message: Message, uri: Uri): Boolean

}