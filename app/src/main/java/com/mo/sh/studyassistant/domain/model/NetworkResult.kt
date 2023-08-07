package com.mo.sh.studyassistant.domain.model

sealed class NetworkResult {
    object Success: NetworkResult()
    sealed class Error(val message: String) : NetworkResult() {
        class Network(message: String) : Error(message)
        class Attachment(message: String) : Error(message)
        class TextLength(message: String) : Error(message)
        class Unknown(message: String) : Error(message)
    }
    object Loading: NetworkResult()
}
