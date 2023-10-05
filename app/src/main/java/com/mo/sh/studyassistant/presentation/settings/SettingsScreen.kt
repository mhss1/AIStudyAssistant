package com.mo.sh.studyassistant.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mo.sh.studyassistant.BuildConfig
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.presentation.MainViewModel
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME_DARK
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME_LIGHT
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME_SYSTEM
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.API_KEY
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }
        }
    ) { paddingValues ->
        val theme by viewModel.get(intPreferencesKey(THEME), THEME_SYSTEM)
            .collectAsState(THEME_SYSTEM)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 10.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SettingsItem(
                    title = stringResource(R.string.theme),
                    iconRes = R.drawable.ic_theme,
                    onClick = {
                        viewModel.save(
                            intPreferencesKey(THEME),
                            when (theme) {
                                THEME_LIGHT -> THEME_DARK
                                THEME_DARK -> THEME_SYSTEM
                                else -> THEME_LIGHT
                            }
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            when (theme) {
                                THEME_LIGHT -> R.drawable.ic_light
                                THEME_DARK -> R.drawable.ic_dark
                                else -> R.drawable.ic_auto
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            item {
                val apiKey by viewModel.apiKey.collectAsState()
                var showKeyDialog by remember {
                    mutableStateOf(false)
                }
                SettingsItem(
                    title = stringResource(R.string.api_key),
                    iconRes = R.drawable.ic_key,
                    onClick = {
                        showKeyDialog = true
                    }
                ) {
                    Text(
                        text = apiKey,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                if (showKeyDialog) {
                    var keyTextField by remember {
                        mutableStateOf(apiKey)
                    }
                    AlertDialog(
                        onDismissRequest = { showKeyDialog = false },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OutlinedTextField(
                                    value = keyTextField,
                                    onValueChange = { keyTextField = it },
                                    label = {
                                        Text(
                                            text = stringResource(R.string.api_key),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .semantics {
                                            testTagsAsResourceId = true
                                        }
                                        .testTag("api-key-text-field"),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                MarkdownText(
                                    markdown = stringResource(R.string.api_key_creation_message),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.surface,
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.save(
                                    stringPreferencesKey(API_KEY),
                                    keyTextField
                                )
                                showKeyDialog = false
                            }) {
                                Text(
                                    stringResource(R.string.save),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    )
                }
            }

            item {
                SettingsSection(stringResource(R.string.about))
            }

            item {
                SettingsLinkItem(
                    title = stringResource(R.string.app_version),
                    iconRes = R.drawable.ic_version,
                    url = "https://github.com/mhss1/AIStudyAssistant/releases"
                ) {
                    Text(
                        text = BuildConfig.VERSION_NAME,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            item {
                SettingsLinkItem(
                    title = stringResource(R.string.project_on_github),
                    iconRes = R.drawable.ic_github,
                    url = "https://github.com/mhss1/AIStudyAssistant"
                ) {}
            }

            item {
                SettingsLinkItem(
                    title = stringResource(R.string.request_feature_report_bug),
                    iconRes = R.drawable.ic_bulb,
                    url = "https://github.com/mhss1/AIStudyAssistant/issues"
                ){}
            }
            item {
                SettingsLinkItem(
                    title = stringResource(R.string.project_roadmap),
                    iconRes = R.drawable.ic_roadmap,
                    url = "https://github.com/users/mhss1/projects/3"
                ){}
            }
        }
    }
}