package com.mo.sh.studyassistant.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME_DARK
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME_LIGHT
import com.mo.sh.studyassistant.data.repository.DataStoreRepository.Companion.THEME_SYSTEM
import com.mo.sh.studyassistant.presentation.main.MainScreen
import com.mo.sh.studyassistant.presentation.questions.QuestionsScreen
import com.mo.sh.studyassistant.presentation.settings.SettingsScreen
import com.mo.sh.studyassistant.presentation.summarize.SummarizeScreen
import com.mo.sh.studyassistant.presentation.tutor.TutorScreen
import com.mo.sh.studyassistant.presentation.writer.WriterScreen
import com.mo.sh.studyassistant.ui.theme.StudyAssistantTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val theme by mainViewModel.get(
                intPreferencesKey(THEME),
                THEME_SYSTEM
            ).collectAsState(null)
            if (theme != null) StudyAssistantTheme(
                darkTheme = when (theme) {
                    THEME_LIGHT -> false
                    THEME_DARK -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                val navController = rememberNavController()
                LaunchedEffect(Unit) {
                    if (
                        intent?.action == Intent.ACTION_SEND ||
                        intent?.action == Intent.ACTION_PROCESS_TEXT
                    ) {
                        if ("text/plain" == intent.type) {
                            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                                navController
                                    .navigate(
                                        Screen.Tutor.route.replace(
                                            "{text}",
                                            it
                                        )
                                    )
                            }
                            intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)?.let {
                                navController
                                    .navigate(
                                        Screen.Tutor.route.replace(
                                            "{text}",
                                            it
                                        )
                                    )
                            }
                        } else if (intent.type?.startsWith("image/") == true) {
                            intent.parcelable<Uri>(Intent.EXTRA_STREAM)?.let {
                                val imageUri = if (it.toString()
                                        .contains("com.google.android.apps.photos.contentprovider")
                                ) {
                                    val ff = contentResolver.openFileDescriptor(it, "r")
                                    val fis = FileInputStream(ff?.fileDescriptor)
                                    val file = File.createTempFile("image", ".jpg")
                                    file.outputStream().use { output ->
                                        fis.copyTo(output)
                                    }
                                    ff?.close()
                                    file.toURI().toString()
                                } else {
                                    it.toString()
                                }
                                navController
                                    .navigate(
                                        Screen.Tutor.route.replace(
                                            "{imageUri}",
                                            imageUri
                                        )
                                    )
                            }
                        } else if (intent.type?.startsWith("application/pdf") == true) {
                            intent.parcelable<Uri>(Intent.EXTRA_STREAM)?.let {
                                navController
                                    .navigate(
                                        Screen.Summarize.route.replace(
                                            "{pdfUri}",
                                            it.toString()
                                        )
                                    )
                            }
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize().semantics {
                        testTagsAsResourceId = true
                    },
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Main.route
                    ) {
                        composable(
                            Screen.Main.route,
                            enterTransition = {
                                slideIntoContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            exitTransition = {
                                slideOutOfContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            popEnterTransition = {
                                slideIntoContainer(SlideDirection.Right, animationSpec = tween(350))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    SlideDirection.Right,
                                    animationSpec = tween(350)
                                )
                            }
                        ) {
                            MainScreen(navController)
                        }
                        composable(
                            Screen.Tutor.route,
                            arguments = listOf(
                                navArgument("imageUri") { nullable = true },
                                navArgument("text") { nullable = true }
                            ),
                            enterTransition = {
                                slideIntoContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            exitTransition = {
                                slideOutOfContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            popEnterTransition = {
                                slideIntoContainer(SlideDirection.Right, animationSpec = tween(350))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    SlideDirection.Right,
                                    animationSpec = tween(350)
                                )
                            }

                        ) {
                            TutorScreen(
                                it.arguments?.getString("imageUri")?.toUri(),
                                it.arguments?.getString("text"),
                                mainViewModel
                            )
                        }
                        composable(
                            Screen.Summarize.route,
                            arguments = listOf(
                                navArgument("pdfUri") { nullable = true }
                            ),
                            enterTransition = {
                                slideIntoContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            exitTransition = {
                                slideOutOfContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            popEnterTransition = {
                                slideIntoContainer(SlideDirection.Right, animationSpec = tween(350))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    SlideDirection.Right,
                                    animationSpec = tween(350)
                                )
                            }
                        ) {
                            SummarizeScreen(
                                it.arguments?.getString("pdfUri")?.toUri(),
                                mainViewModel
                            )
                        }
                        composable(
                            Screen.Writer.route,
                            enterTransition = {
                                slideIntoContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            exitTransition = {
                                slideOutOfContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            popEnterTransition = {
                                slideIntoContainer(SlideDirection.Right, animationSpec = tween(350))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    SlideDirection.Right,
                                    animationSpec = tween(350)
                                )
                            }
                        ) {
                            WriterScreen(mainViewModel)
                        }
                        composable(Screen.Questions.route,
                            enterTransition = {
                                slideIntoContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            exitTransition = {
                                slideOutOfContainer(SlideDirection.Left, animationSpec = tween(350))
                            },
                            popEnterTransition = {
                                slideIntoContainer(SlideDirection.Right, animationSpec = tween(350))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    SlideDirection.Right,
                                    animationSpec = tween(350)
                                )
                            }
                        ) {
                            QuestionsScreen(mainViewModel)
                        }
                        composable(Screen.Settings.route,
                            enterTransition = {
                                slideIntoContainer(SlideDirection.Up, animationSpec = tween(350))
                            },
                            exitTransition = {
                                slideOutOfContainer(SlideDirection.Down, animationSpec = tween(350))
                            },
                            popEnterTransition = {
                                slideIntoContainer(SlideDirection.Up, animationSpec = tween(350))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    SlideDirection.Down,
                                    animationSpec = tween(350)
                                )
                            }
                        ) {
                            SettingsScreen(mainViewModel)
                        }
                    }
                }
            }
        }
    }
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}