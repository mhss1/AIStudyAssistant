package com.mo.sh.studyassistant.presentation.common_components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mo.sh.studyassistant.R

@Composable
fun AttachmentCard(uri: Uri?, isImage: Boolean, onDelete: () -> Unit) {
    LeftToRight {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete",
                    modifier = Modifier
                        .padding(6.dp)
                        .size(16.dp)
                        .clickable {
                            onDelete()
                        }
                )
            }
            Spacer(Modifier.width(12.dp))
            Card(
                Modifier
                    .heightIn(0.dp, 170.dp)
                    .widthIn(0.dp, 250.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ){
                if (isImage){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uri)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                }else {
                    uri?.let {
                        val context = LocalContext.current
                        val name by remember {
                            derivedStateOf {
                                DocumentFile.fromSingleUri(context, it)?.name
                            }
                        }
                        Row(
                            Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_pdf),
                                contentDescription = "pdf",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = name ?: stringResource(R.string.unknown_name),
                                style = MaterialTheme.typography.labelMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.widthIn(0.dp, 200.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

