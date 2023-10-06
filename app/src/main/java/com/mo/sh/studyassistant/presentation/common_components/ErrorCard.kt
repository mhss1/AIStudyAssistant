package com.mo.sh.studyassistant.presentation.common_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mo.sh.studyassistant.R
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun ErrorCard(error: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        modifier = Modifier.padding(
            horizontal = 12.dp
        )
    ){
        MarkdownText(
            markdown = error,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center,
            fontResource = R.font.app_font_medium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}