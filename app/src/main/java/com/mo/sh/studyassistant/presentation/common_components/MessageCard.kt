package com.mo.sh.studyassistant.presentation.common_components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.domain.model.Message
import com.mo.sh.studyassistant.domain.model.MessageType
import com.mo.sh.studyassistant.ui.theme.TertiaryColor
import com.mo.sh.studyassistant.util.formatTime
import dev.jeziellago.compose.markdowntext.MarkdownText


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.MessageCard(
    message: Message,
    onWritePdf: (Uri) -> Unit
) {
    Row(
        horizontalArrangement = if (message.type == MessageType.User.ordinal)
            Arrangement.End
        else
            Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = if (message.type == MessageType.User.ordinal)
                    4.dp
                else 16.dp,
                start = if (message.type == MessageType.User.ordinal)
                    26.dp
                else 4.dp,
                bottom = 4.dp,
                top = 8.dp
            )
            .animateItemPlacement()

    ) {
        Card(
            shape = getShape(message),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            val context = LocalContext.current
            var dropDownExpanded by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colorStops = if (message.type == MessageType.User.ordinal)
                                arrayOf(
                                    0f to TertiaryColor,
                                    1f to MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                                        .compositeOver(
                                            TertiaryColor
                                        )
                                )
                            else
                                arrayOf(
                                    0f to MaterialTheme.colorScheme.surface,
                                    0.3f to MaterialTheme.colorScheme.surface
                                        .copy(alpha = 0.85f)
                                        .compositeOver(
                                            MaterialTheme.colorScheme.tertiary
                                        ),
                                    1f to MaterialTheme.colorScheme.surface
                                        .copy(alpha = 0.85f)
                                        .compositeOver(
                                            MaterialTheme.colorScheme.primary
                                        )
                                ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
                    .then(
                        if (message.contentIsPdf) Modifier
                        else Modifier.clickable {
                            dropDownExpanded = true
                        }
                    )
            ) {
                val chooseDirectoryLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
                        it?.let { uri ->
                            onWritePdf(uri)
                        }
                    }
                if (!(message.contentIsPdf && message.type == MessageType.User.ordinal)) {
                    MarkdownText(
                        markdown = if (message.contentIsPdf) "${
                            stringResource(
                                R.string.pdf_file,
                                message.pdfContentType
                            )
                        }:"
                        else message.content,
                        style = MaterialTheme.typography.bodyLarge,
                        onClick = {
                            dropDownExpanded = true
                        },
                        modifier = Modifier.padding(
                            top = 7.dp,
                            start = if (message.type == MessageType.User.ordinal) 12.dp else 8.dp,
                            end = if (message.type == MessageType.User.ordinal) 8.dp else 12.dp,
                        ),
                        color = if (message.type == MessageType.User.ordinal)
                            Color.White
                        else
                            MaterialTheme.colorScheme.onSurface,
                        maxLines = if (message.contentIsPdf) 1 else Int.MAX_VALUE,
                    )
                }
                if (message.attachment != null || message.contentIsPdf) {
                    AttachmentPreview(
                        uri = message.attachment?.toUri(),
                        isImage = message.attachmentType == AttachmentType.Image.ordinal,
                        fileName = message.attachmentFileName ?: ""
                    ) {
                        if (message.type != MessageType.User.ordinal) {
                            chooseDirectoryLauncher.launch(null)
                        }
                    }
                }
                Text(
                    text = message.time.formatTime(),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    color = if (message.type == MessageType.User.ordinal)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                DropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false },
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                ) {
                    Row(
                        Modifier.clickable {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(
                                "text",
                                message.content
                            )
                            clipboard.setPrimaryClip(clip)

                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.text_copied),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            dropDownExpanded = false
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_copy),
                            contentDescription = "Copy",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(18.dp),
                        )
                        Text(
                            text = stringResource(R.string.copy),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 10.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentPreview(uri: Uri?, isImage: Boolean, fileName: String = "", onClick: () -> Unit) {
    Card(
        Modifier
            .heightIn(0.dp, 200.dp)
            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        if (isImage) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        } else {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    Modifier
                        .clickable {
                            onClick()
                        }
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pdf),
                        contentDescription = "pdf",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = fileName,
                        style = MaterialTheme.typography.labelMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.widthIn(0.dp, 200.dp)
                    )
                }
            }
        }
    }
}

private fun getShape(message: Message) =
    RoundedCornerShape(
        topStart = if (message.type == MessageType.User.ordinal) {
            20.dp
        } else {
            4.dp
        },
        topEnd = if (message.type == MessageType.User.ordinal) {
            4.dp
        } else {
            20.dp
        },
        bottomStart = if (message.type == MessageType.User.ordinal) {
            24.dp
        } else {
            14.dp
        },
        bottomEnd = if (message.type == MessageType.User.ordinal) {
            14.dp
        } else {
            20.dp
        }
    )

@Preview
@Composable
fun MessageCardPreview() {
    LazyColumn {
        item {
            MessageCard(
                message = Message(
                    1,
                    1,
                    2,
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid dicta doloremque ducimus eaque eius error est explicabo facere id ipsum iste itaque laboriosam, laborum magnam molestiae non odit officia placeat quae qui quidem quo ullam voluptatum! Consequuntur distinctio nihil quod totam voluptate? Facere in ipsam itaque, quia sed similique voluptatum!",
                    1111111111
                )
            ) {}
        }
    }
}