package com.mo.sh.studyassistant.presentation.common_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.mo.sh.studyassistant.R

@Composable
fun AttachmentPicker(show: Boolean, visibleItems: List<AttachmentType>, onDismiss: () -> Unit, onSelect: (AttachmentType) -> Unit) {
    DropdownMenu(
        expanded = show,
        onDismissRequest = { onDismiss() },
        offset = DpOffset(x = (-64).dp, y = (-98).dp)
    ) {
        val items by remember(visibleItems) {
            mutableStateOf(
                    listOf(
                        AttachmentItem(
                            R.drawable.ic_image,
                            R.string.text_from_image,
                            AttachmentType.Image
                        ),
                        AttachmentItem(
                            R.drawable.ic_pdf,
                            R.string.pdf_file,
                            AttachmentType.Pdf
                        )
                    ).filter {
                        visibleItems.contains(it.type)
                    }
            )
        }
        items.forEach {
            Row(
                Modifier.padding(vertical = 8.dp, horizontal = 12.dp).clickable {
                    onSelect(it.type)
                }
            ) {
                Icon(
                    painter = painterResource(it.icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(it.text, ""),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}