package com.mo.sh.studyassistant.presentation.common_components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mo.sh.studyassistant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBar(
    text: String,
    hint: String,
    showAddAttachment: Boolean,
    buttonEnabled: Boolean,
    visibleAttachmentItems: List<AttachmentType>,
    showReset: Boolean,
    onReset: () -> Unit,
    onAttachment: (AttachmentType) -> Unit,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var showDropdown by remember {
        mutableStateOf(false)
    }
    LeftToRight {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showReset) Icon(
                painter = painterResource(id = R.drawable.ic_broom),
                contentDescription = "Clear context",
                Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .clickable { onReset() }
            )
            TextField(
                value = text,
                textStyle = MaterialTheme.typography.bodyMedium,
                onValueChange = { onTextChange(it) },
                shape = RoundedCornerShape(32.dp),
                placeholder = {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 6.dp, bottom = 8.dp, start = 8.dp, end = 0.dp)
                    .heightIn(0.dp, 400.dp)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(32.dp))
                    .testTag("chat-text-field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    AttachmentPicker(
                        show = showDropdown,
                        visibleItems = visibleAttachmentItems,
                        onDismiss = { showDropdown = false },
                        onSelect = {
                            onAttachment(it)
                            showDropdown = false
                        })
                    if (showAddAttachment) Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Attachment",
                        Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .clickable { showDropdown = true },
                    )
                },
            )
            IconButton(
                onClick = { onSubmit() },
                enabled = buttonEnabled,
                modifier = Modifier.testTag("send-button")
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_send),
                    contentDescription = "Send",
                    modifier = Modifier
                        .size(24.dp),
                    tint =
                    if (buttonEnabled) MaterialTheme.colorScheme.primary
                    else Color.Gray
                )
            }
        }

    }
}

enum class AttachmentType {
    Image,
    Pdf
}