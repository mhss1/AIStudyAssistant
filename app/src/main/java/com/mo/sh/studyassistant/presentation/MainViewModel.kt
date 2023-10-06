package com.mo.sh.studyassistant.presentation

import android.net.Uri
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mo.sh.studyassistant.data.repository.DataStoreRepository
import com.mo.sh.studyassistant.domain.model.ChatWithMessages
import com.mo.sh.studyassistant.domain.model.Message
import com.mo.sh.studyassistant.domain.model.MessageSection
import com.mo.sh.studyassistant.domain.model.NetworkResult
import com.mo.sh.studyassistant.domain.repository.ChatRepository
import com.mo.sh.studyassistant.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val prefs: PreferencesRepository
) : ViewModel() {

    private val section = MutableStateFlow(MessageSection.Tutor)

    @OptIn(ExperimentalCoroutinesApi::class)
    val chats = section.flatMapLatest {
        repository.getSectionChats(it.ordinal)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val apiKey = prefs.get(stringPreferencesKey(DataStoreRepository.API_KEY), "")
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    private val _loadingState = MutableStateFlow<NetworkResult>(NetworkResult.Success)
    val loadingState: StateFlow<NetworkResult> = _loadingState

    fun sendMessage(
        message: String,
        chat: ChatWithMessages? = null,
        imageUri: Uri? = null,
        pdfUri: Uri? = null
    ) = viewModelScope.launch {
        _loadingState.update { NetworkResult.Loading }

        _loadingState.update {
            repository.sendMessage(message, apiKey.value, chat, imageUri, pdfUri)
        }
    }

    fun sendPrompt(message: String, pdfUri: Uri? = null) = viewModelScope.launch {
        _loadingState.update { NetworkResult.Loading }
        _loadingState.update {
            repository.sendPrompt(message, section.value, apiKey.value,pdfUri)
        }
    }


    fun updateSection(messageSection: MessageSection) = viewModelScope.launch {
        section.update { messageSection }
    }

    fun resetChat() = viewModelScope.launch {
        repository.clearChatContext()
    }

    fun clearAllChats() = viewModelScope.launch {
        repository.resetAllChats(section.value.ordinal)
    }

    fun writePdfFile(message: Message, uri: Uri, onComplete: (Boolean) -> Unit) =
        viewModelScope.launch {
            onComplete(repository.writePdfFile(message, uri))
        }

    fun <T> save(key: Preferences.Key<T>, value: T) = viewModelScope.launch{
        prefs.save(key, value)
    }

    fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return prefs.get(key, defaultValue)
    }
}