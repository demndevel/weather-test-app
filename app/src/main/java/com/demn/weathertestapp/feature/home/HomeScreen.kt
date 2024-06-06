package com.demn.weathertestapp.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.demn.core.models.City
import com.demn.data.repos.MockWeatherRepository
import com.demn.weathertestapp.R
import com.demn.weathertestapp.ui.theme.WeatherTestAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private val GutterWidth = 40.dp

@Composable
fun HomeScreen(
    onCityClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    vm: HomeViewModel = koinViewModel(),
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadData()
    }

    Box(modifier) {
        when (state) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator(
                    Modifier
                        .align(Alignment.Center)
                        .width(48.dp)
                )
            }

            is HomeUiState.NoData -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.error_occured))

                    Spacer(modifier = Modifier.height(42.dp))

                    Button(onClick = { vm.loadData() }) {
                        Text(stringResource(R.string.refresh))
                    }
                }
            }

            is HomeUiState.Loaded -> {
                LoadedState(state as HomeUiState.Loaded, onCityClick, Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun LoadedState(
    state: HomeUiState.Loaded,
    onCityClick: (Long) -> Unit,
    modifier: Modifier
) {
    StickyLetterList(
        items = state.cities,
        gutterWidth = GutterWidth,
        itemFactory = { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        ),
                    )
                    .height(40.dp)
                    .clickable { onCityClick(item.id) },
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun StickyLetterList(
    items: List<City>,
    modifier: Modifier = Modifier,
    gutterWidth: Dp,
    itemFactory: @Composable (City) -> Unit
) {
    val state = rememberLazyListState()
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val gutterPx = with(density) {
        gutterWidth.toPx() + 32.dp.toPx()
    }
    val initialColor = MaterialTheme.colorScheme.secondary
    val initialTextStyle: TextStyle = MaterialTheme.typography.labelMedium
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    val statusBarHeight = statusBarPadding.calculateTopPadding()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(items) {
        coroutineScope.launch {
            state.animateScrollToItem(0)
        }
    }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .drawWithCache {
                    drawResult(
                        state,
                        items,
                        textMeasurer,
                        initialTextStyle,
                        gutterPx,
                        initialColor,
                        statusBarHeight
                    )
                }
                .padding(start = 16.dp)
        ) {
            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = gutterWidth)
            ) {
                item {
                    Spacer(Modifier.statusBarsPadding())
                }

                items(items) { item ->
                    itemFactory(item)
                }
            }
        }
    }
}

private fun CacheDrawScope.drawResult(
    state: LazyListState,
    items: List<City>,
    textMeasurer: TextMeasurer,
    initialTextStyle: TextStyle,
    gutterPx: Float,
    initialColor: Color,
    statusBarHeight: Dp
): DrawResult {
    var itemHeight = 0
    return onDrawBehind {
        var initial: Char? = null
        if (itemHeight == 0) {
            itemHeight = state.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
        }
        state.layoutInfo.visibleItemsInfo.forEachIndexed { index, itemInfo ->
            val itemInitial = items.getOrNull(itemInfo.index)?.name?.first()
            if (itemInitial != null && itemInitial != initial) {
                initial = itemInitial
                val nextInitial = items.getOrNull(itemInfo.index + 1)?.name?.first()
                val textLayout = textMeasurer.measure(
                    text = AnnotatedString(itemInitial.toString()),
                    style = initialTextStyle,
                )
                val horizontalOffset = (gutterPx - textLayout.size.width) / 2
                val verticalOffset =
                    (itemHeight - textLayout.size.height + statusBarHeight.toPx() * 2) / 2
                drawText(
                    textLayoutResult = textLayout,
                    color = initialColor,
                    topLeft = Offset(
                        x = horizontalOffset,
                        y = if (index != 0 || itemInitial != nextInitial) {
                            itemInfo.offset.toFloat()
                        } else {
                            0f
                        } + verticalOffset,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    WeatherTestAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                {},
                Modifier
                    .fillMaxSize(),
                vm = HomeViewModel(MockWeatherRepository())
            )
        }
    }
}