package com.mo.sh.studyassistant.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mo.sh.studyassistant.domain.model.Chat
import com.mo.sh.studyassistant.domain.model.ChatWithMessages
import com.mo.sh.studyassistant.domain.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Insert
    suspend fun addChat(chat: Chat): Long

    @Insert
    suspend fun addMessage(message: Message): Long
    @Transaction
    @Query("SELECT * FROM chats WHERE section = :section ORDER BY time DESC")
    fun allSectionChats(section: Int): Flow<List<ChatWithMessages>>

    @Update
    suspend fun updateMessage(message: Message)

    @Update
    suspend fun updateChat(chat: Chat)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessage(id: Long)

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChat(id: Long)

    @Query("DELETE FROM chats WHERE section = :section")
    suspend fun deleteSectionChats(section: Int)
}