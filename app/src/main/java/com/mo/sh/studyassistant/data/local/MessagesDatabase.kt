package com.mo.sh.studyassistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mo.sh.studyassistant.domain.model.Chat
import com.mo.sh.studyassistant.domain.model.Message

const val DB_NAME = "messages_database"

@Database(entities = [Message::class, Chat::class], version = 1)
abstract class MessagesDatabase: RoomDatabase() {

    abstract fun messagesDao(): MessagesDao
}
