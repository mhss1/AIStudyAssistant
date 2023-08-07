package com.mo.sh.studyassistant.presentation.summarize

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.domain.model.MessageSection
import com.mo.sh.studyassistant.presentation.MainViewModel
import com.mo.sh.studyassistant.presentation.common_components.AttachmentType
import com.mo.sh.studyassistant.presentation.common_components.BaseChatSurface

@Composable
fun SummarizeScreen(
    sharedPdfUri: Uri?,
    viewModel: MainViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.updateSection(MessageSection.Summarizer)
    }
    var text by rememberSaveable {
        mutableStateOf("")
    }
    var pdfUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val chats by viewModel.chats.collectAsState()
    val networkState by viewModel.loadingState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (sharedPdfUri != null) {
            pdfUri = sharedPdfUri
        }
    }

    BaseChatSurface(
        textFieldText = text,
        title = stringResource(R.string.lecture_summaries_title),
        visibleAttachmentItems = listOf(
            AttachmentType.Pdf,
        ),
        selectedPdfUri = pdfUri,
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
        onPdfSelected = {
            pdfUri = it
        },
        onTextChanged = { text = it },
        textFieldHint = stringResource(R.string.attach_or_paste),
        onSubmit = {
            if (it.isBlank() && pdfUri == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.provide_content),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.sendPrompt(message = it, pdfUri = pdfUri)
            }
            pdfUri = null
            text = ""
        }
    )
}