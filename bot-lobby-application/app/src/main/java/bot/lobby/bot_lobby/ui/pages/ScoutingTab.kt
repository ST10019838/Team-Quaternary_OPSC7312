package bot.lobby.bot_lobby.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions

object ScoutingTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Scouting"
            val icon = rememberVectorPainter(Icons.Default.Search)

            return remember {
                TabOptions(
                    index = 4u,
                    title = title,
                    icon = icon
                )
            }
        }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
//        val selectablePeriod by SessionState.selectedPeriod.collectAsState()

        TabNavigator(ScoutingTeamsTab) { tabNavigator ->
            Scaffold(
                bottomBar =
                {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            thickness = 1.dp, modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .align(Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(100)),
                            color = MaterialTheme.colorScheme.primary
                        )

                        PrimaryTabRow(
                            selectedTabIndex = tabNavigator.current.options.index.toInt(),
                            divider = {},
                            modifier = Modifier.fillMaxWidth(),
                        ) {

                            Tab(
                                selected = tabNavigator.current == ScoutingTeamsTab,
                                onClick = { tabNavigator.current = ScoutingTeamsTab },
                                text = {
                                    Text(
                                        text = ScoutingTeamsTab.options.title,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Tab(
                                selected = tabNavigator.current == ScoutingPlayersTab,
                                onClick = { tabNavigator.current = ScoutingPlayersTab },
                                text = {
                                    Text(
                                        text = ScoutingPlayersTab.options.title,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                },
                content =
                {
                    Box(
                        modifier = Modifier.padding(it)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
//                            SelectablePeriodSearch(
//                                value = selectablePeriod,
//                                onSelectionChange = {
//                                    SessionState.onSelectedPeriodChange(
//                                        it.fromDate,
//                                        it.toDate
//                                    )
//                                },
//                                isOpen = false
//                            )

                            CurrentTab()
                        }
                    }
                }
            )
        }
    }
}