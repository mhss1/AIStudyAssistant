package com.mo.sh.studyassistant.presentation.common_components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.domain.model.ChatWithMessages
import com.mo.sh.studyassistant.domain.model.Message
import com.mo.sh.studyassistant.domain.model.NetworkResult

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BaseChatSurface(
    textFieldText: String,
    title: String,
    visibleAttachmentItems: List<AttachmentType>,
    selectedImageUri: Uri? = null,
    selectedPdfUri: Uri? = null,
    onReset: () -> Unit,
    onDeleteAll: () -> Unit,
    chats: List<ChatWithMessages>,
    loadingState: NetworkResult,
    onWritePdf: (Message, Uri) -> Unit,
    onImageSelected: (Uri?) -> Unit,
    onPdfSelected: (Uri?) -> Unit,
    textFieldHint: String,
    onTextChanged: (String) -> Unit,
    onSubmit: (String) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri -> onImageSelected(uri) }

    val pickPdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        onPdfSelected(uri)
    }

    var showDeleteDialog by remember { mutableStateOf(false) }


    LaunchedEffect(loadingState) {
        if (chats.isNotEmpty()) {
            lazyListState.animateScrollToItem(0)
        }
    }
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colorStops = arrayOf(
                                0f to MaterialTheme.colorScheme.surface,
                                0.5f to MaterialTheme.colorScheme.surface
                                    .copy(alpha = 0.8f)
                                    .compositeOver(
                                        MaterialTheme.colorScheme.tertiary
                                    ),
                                1f to MaterialTheme.colorScheme.surface
                                    .copy(alpha = 0.8f)
                                    .compositeOver(
                                        MaterialTheme.colorScheme.primary
                                    )
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
                    .padding(vertical = 4.dp)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    if (chats.isNotEmpty()) {
                        IconButton(onClick = {
                            showDeleteDialog = true
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = stringResource(R.string.delete),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colorStops = arrayOf(
                                0f to MaterialTheme.colorScheme.surface,
                                0.4f to MaterialTheme.colorScheme.surface
                                    .copy(alpha = 0.8f)
                                    .compositeOver(
                                        MaterialTheme.colorScheme.tertiary
                                    ),
                                1f to MaterialTheme.colorScheme.surface
                                    .copy(alpha = 0.8f)
                                    .compositeOver(
                                        MaterialTheme.colorScheme.primary
                                    )
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.End
            ) {
                AnimatedVisibility(selectedImageUri != null || selectedPdfUri != null) {
                    AttachmentCard(
                        uri = selectedImageUri ?: selectedPdfUri,
                        isImage = selectedImageUri != null
                    ) {
                        onImageSelected(null)
                        onPdfSelected(null)
                    }
                }
                if (selectedImageUri != null) {
                    SuggestionsBar {
                        onSubmit(it)
                    }
                }
                ChatBar(
                    text = textFieldText,
                    hint = textFieldHint,
                    visibleAttachmentItems = visibleAttachmentItems,
                    showAddAttachment = visibleAttachmentItems.isNotEmpty() && selectedImageUri == null && selectedPdfUri == null,
                    buttonEnabled = loadingState != NetworkResult.Loading,
                    showReset = chats.firstOrNull()
                        ?.let { !it.chat.done && it.messages.isNotEmpty() } ?: false,
                    onReset = { onReset() },
                    onAttachment = {
                        if (it == AttachmentType.Image)
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        else pickPdfLauncher.launch(arrayOf("application/pdf"))
                    },
                    onTextChange = { onTextChanged(it) }
                ) {
                    onSubmit(textFieldText)
                }
            }
        }
    ) { paddingValues ->
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(
                        text = stringResource(R.string.clear_chat),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.clear_chat_description),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        onDeleteAll()
                        showDeleteDialog = false
                    }) {
                        Text(
                            text = stringResource(R.string.delete),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                    }) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )
        }
        if (chats.isEmpty()) {
            NotesCard()
        }
        LeftToRight {
            LazyColumn(
                Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .padding(
                        horizontal = 4.dp
                    )
                    .testTag("messages-list"),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = true,
                contentPadding = PaddingValues(bottom = 26.dp)
            ) {
                if (loadingState is NetworkResult.Loading) {
                    item("loading") {
                        ChatLoadingAnimation(modifier = Modifier.padding(vertical = 24.dp))
                    }
                }
                if (loadingState is NetworkResult.Error) {
                    item("error") {
                        ErrorCard(error = loadingState.message)
                        Spacer(Modifier.height(32.dp))
                    }
                }
                chats.forEach {
                    if (it.chat.done) {
                        item("context${it.chat.id}") {
                            ContextDivider()
                        }
                    }
                    items(it.messages, key = { m -> m.id }) { message ->
                        MessageCard(message = message) { uri ->
                            onWritePdf(message, uri)
                        }
                    }
                }
            }
        }
    }
}