package com.mo.sh.studyassistant.presentation.writer

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.domain.model.MessageSection
import com.mo.sh.studyassistant.presentation.MainViewModel
import com.mo.sh.studyassistant.presentation.common_components.BaseChatSurface

@Composable
fun WriterScreen(
    viewModel: MainViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.updateSection(MessageSection.Writer)
    }
    var text by rememberSaveable {
        mutableStateOf("")
    }
    val chats by viewModel.chats.collectAsState()
    val networkState by viewModel.loadingState.collectAsState()
    val context = LocalContext.current

    BaseChatSurface(
        textFieldText = text,
        title = stringResource(R.string.writer_title),
        visibleAttachmentItems = emptyList(),
        chats = chats,
        loadingState = networkState,
        onWritePdf = { message, uri ->
            viewModel.writePdfFile(
                message,
                uri
            ) {
                Toast.makeText(
                    context,
                    if (it) {
                        context.getString(R.string.pdf_saved)
                    } else context.getString(R.string.error_creating_pdf),
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onReset = {
            viewModel.resetChat()
        },
        onDeleteAll = {
            viewModel.clearAllChats()
        },
        onImageSelected = {},
        onPdfSelected = {},
        onTextChanged = { text = it },
        textFieldHint = stringResource(R.string.essay_subject),
        onSubmit = {
            if (it.isBlank()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.subject_cant_be_empty),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.sendPrompt(message = it)
            }
            text = ""
        }
    )
}