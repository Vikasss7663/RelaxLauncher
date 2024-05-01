package org.shekhawat.launcher.ui.theme.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import org.shekhawat.launcher.AppInfo
import org.shekhawat.laucher.R

@Composable
fun AppListScreen(appList: List<AppInfo>) {
    var searchAppName by remember {
        mutableStateOf("")
    }
    var recentlyOpenedApps by remember {
        mutableStateOf<Set<AppInfo>>(emptySet())
    }
    val scrollState = rememberLazyListState()

    Column {
        // show search bar
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            value = searchAppName,
            onValueChange = { searchAppName = it },
            placeholder = { Text("Search App") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                if (searchAppName.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                searchAppName = ""
                            }
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                // open the settings
                            }
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )

        if (searchAppName.isNotEmpty()) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )
        }

        val filteredAppList = appList.filter { appInfo ->
            appInfo.name.contains(searchAppName, ignoreCase = true)
        }
        if (filteredAppList.isEmpty()) {
            Text(
                text = "No app found",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            return
        }

        // show the recently opened apps
        if (recentlyOpenedApps.isNotEmpty()) {
            Text(
                text = "Recently Opened",
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
            LazyColumn(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(recentlyOpenedApps.size) { index ->
                    AppItem(recentlyOpenedApps.elementAt(index)) {
                        // no action required here
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(8.dp)
            )
        }


        Text(
            text = "All Apps",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            // show the app list with their icons
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = scrollState,
                verticalArrangement = Arrangement.Top
            ) {
                items(filteredAppList.size) { index ->
                    AppItem(filteredAppList[index]) {
                        if (recentlyOpenedApps.size >= 3) {
                            recentlyOpenedApps = recentlyOpenedApps.drop(1).toSet()
                        }
                        if (recentlyOpenedApps.contains(filteredAppList[index])) {
                            recentlyOpenedApps =
                                recentlyOpenedApps - filteredAppList[index] // re-indexing
                        }
                        recentlyOpenedApps = recentlyOpenedApps + filteredAppList[index]
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // show a-z character and then shift the app list to that character
                items(26) { index ->
                    val char = ('A' + index).toString()
                    Text(
                        text = char,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                // scroll to the app starting with the character
                            }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(appInfo: AppInfo, onClick: () -> Unit) {
    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }
    // show the app icon and name
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    // open the app when clicked
                    appInfo.intent?.let { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                    onClick()
                },
                onLongClick = {
                    // show the app info when long clicked
                    // show the quick action items like app info, uninstall, etc
                    expanded.value = true
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = "App Info") },
                onClick = {
                    expanded.value = false
                    showAppInfo(context, appInfo.packageName)
                }
            )
        }
        // show the app icon ( commenting, app is lagging )
        Image(
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp),
            bitmap = appInfo.icon.toBitmap().asImageBitmap(),
            contentDescription = "App Icon"
        )
        // show the app name
        Text(
            text = appInfo.name,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 18.sp,
        )
    }
}

fun showAppInfo(context: Context, packageName: String) {
    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    context.startActivity(intent)
}