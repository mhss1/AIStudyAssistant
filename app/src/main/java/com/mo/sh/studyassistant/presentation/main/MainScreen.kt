package com.mo.sh.studyassistant.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.domain.model.MessageSection
import com.mo.sh.studyassistant.presentation.Screen
import com.mo.sh.studyassistant.ui.theme.StudyAssistantTheme

@Composable
fun MainScreen(
    navController: NavController,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .statusBarsPadding()
    ) {
        Spacer(Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.main_screen_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Icon(painter = painterResource(R.drawable.settings),
                contentDescription =
                stringResource(R.string.settings),
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.Settings.route)
                    }
                    .padding(4.dp)
                    .size(24.dp)
                    .testTag("settings-button")

            )
        }
        Text(
            text = stringResource(R.string.main_screen_subtitle),
            style = MaterialTheme.typography.bodyLarge
        )
        LazyColumn(
            Modifier.weight(1f)
        ) {
            items(items) { item ->
                MainScreenCard(item = item) { route ->
                    navController.navigate(route)
                }
            }
            item {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MainScreenPreview() {
    StudyAssistantTheme {
        LazyColumn {
            items(items) { item ->
                MainScreenCard(item = item) { }
            }
        }
    }
}

val items = listOf(
    MainScreenItem(
        title = R.string.personal_tutor_title,
        description = R.string.personal_tutor_description,
        route = Screen.Tutor.route,
        icon = R.drawable.chatbot,
        section = MessageSection.Tutor
    ),
    MainScreenItem(
        title = R.string.lecture_summaries_title,
        description = R.string.lecture_summaries_description,
        route = Screen.Summarize.route,
        icon = R.drawable.summarize,
        section = MessageSection.Summarizer
    ),
    MainScreenItem(
        title = R.string.writer_title,
        description = R.string.writer_description,
        route = Screen.Writer.route,
        icon = R.drawable.writer,
        section = MessageSection.Writer
    ),
    MainScreenItem(
        title = R.string.questions_title,
        description = R.string.questions_description,
        route = Screen.Questions.route,
        icon = R.drawable.qa,
        section = MessageSection.Questions
    )
)