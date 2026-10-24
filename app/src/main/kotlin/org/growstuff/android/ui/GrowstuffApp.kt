// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.growstuff.android.AppContainer
import org.growstuff.android.R
import org.growstuff.android.ui.components.VerticalRule
import org.growstuff.android.ui.friends.FriendsScreen
import org.growstuff.android.ui.gardens.GardensScreen
import org.growstuff.android.ui.gardens.GardensViewModel
import org.growstuff.android.ui.home.Area
import org.growstuff.android.ui.home.HomeScreen
import org.growstuff.android.ui.jobs.JobsScreen
import org.growstuff.android.ui.jobs.JobsViewModel
import org.growstuff.android.ui.signin.SignInScreen
import org.growstuff.android.ui.theme.GrowstuffColors

@Serializable private object SignInRoute

@Serializable private object HomeRoute

@Serializable private object GardensRoute

@Serializable private object FriendsRoute

@Serializable private object JobsRoute

@Composable
fun GrowstuffApp(container: AppContainer) {
    val member by container.session.member.collectAsStateWithLifecycle()
    val ideaCount by container.jobs.ideaCount.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val nav = rememberNavController()
    // Two panes from medium width up: a tablet, or a big phone on its side.
    val twoPanes = currentWindowAdaptiveInfoV2().windowSizeClass
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    val area: @Composable (Area, (() -> Unit)?) -> Unit = { which, onBack ->
        when (which) {
            Area.Gardens -> GardensScreen(gardensViewModel(container), container.clock, onBack)
            Area.Friends -> FriendsScreen(onBack)
            Area.Jobs -> JobsScreen(jobsViewModel(container), onBack)
        }
    }

    NavHost(nav, startDestination = if (member == null) SignInRoute else HomeRoute) {
        composable<SignInRoute> {
            SignInScreen(onSignIn = {
                scope.launch {
                    container.session.signIn()
                    nav.navigate(HomeRoute) { popUpTo(SignInRoute) { inclusive = true } }
                }
            })
        }
        composable<HomeRoute> {
            var selected by rememberSaveable { mutableStateOf<Area?>(null) }
            val home: @Composable (Modifier) -> Unit = { modifier ->
                HomeScreen(
                    loginName = member?.loginName.orEmpty(),
                    hasIdeas = ideaCount > 0,
                    selected = selected.takeIf { twoPanes },
                    onOpen = { which ->
                        if (twoPanes) {
                            selected = which
                        } else {
                            nav.navigate(
                                when (which) {
                                    Area.Gardens -> GardensRoute
                                    Area.Friends -> FriendsRoute
                                    Area.Jobs -> JobsRoute
                                },
                            )
                        }
                    },
                    onSignOut = {
                        scope.launch {
                            container.session.signOut()
                            nav.navigate(SignInRoute) { popUpTo(HomeRoute) { inclusive = true } }
                        }
                    },
                    modifier = modifier,
                )
            }
            if (twoPanes) {
                Row(Modifier.fillMaxSize()) {
                    home(Modifier.widthIn(max = 420.dp).weight(0.4f))
                    VerticalRule()
                    Box(Modifier.weight(0.6f)) {
                        selected?.let { area(it, null) } ?: PickAnArea()
                    }
                }
            } else {
                home(Modifier)
            }
        }
        composable<GardensRoute> { area(Area.Gardens) { nav.popBackStack() } }
        composable<FriendsRoute> { area(Area.Friends) { nav.popBackStack() } }
        composable<JobsRoute> { area(Area.Jobs) { nav.popBackStack() } }
    }
}

@Composable
private fun PickAnArea() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            stringResource(R.string.pick_an_area),
            style = MaterialTheme.typography.bodyLarge,
            color = GrowstuffColors.Black,
        )
    }
}

@Composable
private fun jobsViewModel(container: AppContainer): JobsViewModel = viewModel(
    factory = viewModelFactory { initializer { JobsViewModel(container.jobs, container.clock) } },
)

@Composable
private fun gardensViewModel(container: AppContainer): GardensViewModel = viewModel(
    factory = viewModelFactory {
        initializer { GardensViewModel(container.gardens, container.clock) }
    },
)
