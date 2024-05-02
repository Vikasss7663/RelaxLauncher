package org.shekhawat.launcher.ui.theme.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import org.shekhawat.launcher.AppInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RootScreen(appList: List<AppInfo>) {
    val scrollState = rememberPagerState(3) { 5 }
    HorizontalPager(state = scrollState) {
        when (it) {
            0 -> TimerScreen()
            1 -> TimeScreen()
            2 -> PomodoroScreen()
            3 -> MainScreen(appList)
            4 -> WidgetsScreen()
        }
    }
}