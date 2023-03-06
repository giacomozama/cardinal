package me.zama.cardinal.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import me.zama.cardinal.R
import me.zama.cardinal.domain.entities.backgroundColor
import me.zama.cardinal.ui.navigation.Routes
import me.zama.cardinal.ui.navigation.defaultNavAnimationSpec
import me.zama.cardinal.ui.screens.album.AlbumScreen
import me.zama.cardinal.ui.screens.album.AlbumScreenMode
import me.zama.cardinal.ui.screens.album.AlbumViewModel
import me.zama.cardinal.ui.screens.library.LibraryScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberAnimatedNavController()

    val currentBackStackEntry = navController.currentBackStackEntryFlow
        .collectAsStateWithLifecycle(initialValue = null)

    val navTransition = currentBackStackEntry.value?.let {
        updateTransition(it, label = "currentBackStackEntry")
    }

    val albumViewModel = hiltViewModel<AlbumViewModel>(LocalContext.current as ViewModelStoreOwner)
    val playScreenMode = albumViewModel.mode.collectAsStateWithLifecycle()

    val topAppBarColor = navTransition?.animateColor(
        label = "topAppBarColor"
    ) { backStackEntry ->
        if (backStackEntry.destination.route == Routes.Play) {
            (albumViewModel.mode.value as? AlbumScreenMode.Album)
                ?.origin?.coverArt?.backgroundColor ?: MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.surface
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    AnimatedVisibility(
                        visible = navController.backQueue.size > 2,
                        enter = expandHorizontally() + slideInHorizontally(),
                        exit = shrinkHorizontally() + slideOutHorizontally()
                    ) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topAppBarColor?.value ?: MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        AnimatedNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = Routes.Library,
            transition = navTransition
        ) {
            composable(
                route = Routes.Library,
                popEnterTransition = {
                    fadeIn(defaultNavAnimationSpec())
                },
                exitTransition = {
                    fadeOut(defaultNavAnimationSpec())
                }
            ) { backStackEntry ->
                LibraryScreen(
                    viewModel = hiltViewModel(backStackEntry),
                    albumScreenModeState = playScreenMode,
                    transition = navTransition!!,
                    onOpenPlayScreen = { mode ->
                        albumViewModel.setMode(mode)
                        navController.navigate(Routes.Play)
                    }
                )
            }

            composable(
                route = Routes.Play,
                enterTransition = {
                    EnterTransition.None
                },
                popExitTransition = {
                    ExitTransition.None
                }
            ) {
                AlbumScreen(
                    modifier = Modifier.animateEnterExit(
                        enter = slideInVertically(
                            animationSpec = defaultNavAnimationSpec(),
                            initialOffsetY = { it }
                        ),
                        exit = slideOutVertically(
                            animationSpec = defaultNavAnimationSpec(),
                            targetOffsetY = { it }
                        )
                    ),
                    viewModel = albumViewModel,
                    transition = transition
                )
            }
        }
    }
}
