package com.mo.sh.studyassistant.di

import android.content.Context
import androidx.room.Room
import com.mo.sh.studyassistant.data.local.DB_NAME
import com.mo.sh.studyassistant.data.local.MessagesDao
import com.mo.sh.studyassistant.data.local.MessagesDatabase
import com.mo.sh.studyassistant.data.network.PalmApi
import com.mo.sh.studyassistant.util.MLManager
import com.mo.sh.studyassistant.util.PDFManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import com.mo.sh.studyassistant.data.repository.*
import com.mo.sh.studyassistant.domain.repository.ChatRepository
import com.mo.sh.studyassistant.domain.repository.PreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            MessagesDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideMessagesDao(db: MessagesDatabase) = db.messagesDao()

    @Provides
    @Singleton
    fun provideMLManager(
        @ApplicationContext context: Context
    ) = MLManager(context)

    @Provides
    @Singleton
    fun providePDFManager(
        @ApplicationContext context: Context
    ) = PDFManager(context)

    @Provides
    @Singleton
    fun provideChatRepository(
        dao: MessagesDao,
        palmApi: PalmApi,
        ml: MLManager,
        pdf: PDFManager
    ): ChatRepository = ChatRepositoryImpl(
        dao,
        palmApi,
        ml,
        pdf
    )

    @Provides
    @Singleton
    fun provideKtorClient() = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    @Provides
    @Singleton
    fun providePalmApi(
        client: HttpClient
    ): PalmApi = PalmApi(client)

    @Provides
    @Singleton
    fun providePreferencesRepository(
        @ApplicationContext context: Context
    ): PreferencesRepository = DataStoreRepository(
        context
    )

}