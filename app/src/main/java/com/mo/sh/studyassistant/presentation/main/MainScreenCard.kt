package com.mo.sh.studyassistant.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.domain.model.MessageSection
import com.mo.sh.studyassistant.ui.theme.StudyAssistantTheme
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun MainScreenCard(
    item: MainScreenItem,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = SquircleShape(
            radius = 42.dp,
            cornerSmoothing = CornerSmoothing.Medium
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(Modifier
            .background(
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to MaterialTheme.colorScheme.surface,
                        0.5f to MaterialTheme.colorScheme.surface.copy(alpha = 0.85f).compositeOver(
                            MaterialTheme.colorScheme.tertiary
                        ),
                        1f to MaterialTheme.colorScheme.surface.copy(alpha = 0.85f).compositeOver(
                            MaterialTheme.colorScheme.primary
                        )
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .clickable {
                onClick(item.route)
            }
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painterResource(item.icon),
                    contentDescription = stringResource(item.title),
                    modifier = Modifier.size(75.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(item.title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(item.description),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MainScreenCardPreview() {
    StudyAssistantTheme {
        MainScreenCard(
            item = MainScreenItem(
                title = R.string.personal_tutor_title,
                description = R.string.personal_tutor_description,
                route = "questions",
                icon = R.drawable.chatbot,
                section = MessageSection.Tutor
            ),
            onClick = {}
        )
    }
}