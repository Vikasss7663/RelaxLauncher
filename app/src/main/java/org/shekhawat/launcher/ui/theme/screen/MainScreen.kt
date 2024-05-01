package org.shekhawat.launcher.ui.theme.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import org.shekhawat.launcher.AppInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(appList: List<AppInfo>) {

    val scrollState = rememberPagerState(0) { 2 }
    VerticalPager(state = scrollState) {
        when (it) {
            0 -> HomeScreen()
            1 -> AppListScreen(appList = appList)
        }
    }
}