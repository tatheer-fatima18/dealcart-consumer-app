package com.localpk.app.features.home.components.swimlanes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.home.components.BottomSection
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.PinkPager
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundWaning
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSuccess
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.hour
import com.localpk.app.utils.minutes
import com.localpk.app.utils.seconds
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.explore_by_flashDeals
import dealcart.composeapp.generated.resources.explore_by_groupDeals
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun GroupDealComponent(
    flashDeals: List<FlashDealUiData>,
    onCardClick: (FlashDealUiData, ProductUiData) -> Unit,
    onAddClick: (FlashDealUiData, ProductUiData) -> Unit,
    screenWidthDp: Int, // Pass manually on iOS via ViewModel or bridge
) {
    if (flashDeals.isNotEmpty()) {
        val itemWidth = 155.dp
        val spacing = 8.dp

        val itemsPerPage = 2 * (screenWidthDp.dp / (itemWidth + spacing)).toInt()
        val totalPages = (flashDeals.size + itemsPerPage - 1) / itemsPerPage

        val lazyListState = rememberLazyGridState()
        val currentPage = remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex / itemsPerPage
            }
        }
        val currentPageState = remember { mutableIntStateOf(0) }

        val hours = remember { mutableStateOf("00") }
        val minutes = remember { mutableStateOf("00") }
        val seconds = remember { mutableStateOf("00") }

        val deal = flashDeals.firstOrNull() ?: return
        val height = if (flashDeals.size < 6) 280.dp else 580.dp

        LaunchedEffect(key1 = deal.timeLeft.getValueOrDefault()) {
            val initialTime = deal.timeLeft.getValueOrDefault()
            var millisUntilFinished = initialTime

            while (millisUntilFinished > 0) {
                delay(1000L)
                millisUntilFinished -= 1000L

                hours.value = millisUntilFinished.hour()
                minutes.value = millisUntilFinished.minutes()
                seconds.value = millisUntilFinished.seconds()
            }

            hours.value = "00"
            minutes.value = "00"
            seconds.value = "00"
        }

        Column(modifier = Modifier.fillMaxSize().padding(vertical = 8.dp)) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .background(
                        SurfaceBackgroundWaning,
                        shape = RoundedCornerShape(10.dp)
                    ) // Replace with parameter if needed
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            if (RemoteConfigs.isEnabledFlashDealTesting()) Res.string.explore_by_groupDeals else Res.string.explore_by_flashDeals
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued,
                        fontFamily = PoppinsFontFamily()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Ends in: ",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        TimerBox(hours.value)
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = ":",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = TextSuccess
                            )
                        )
                        TimerBox(minutes.value)
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = ":",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = TextSuccess
                            )
                        )
                        TimerBox(seconds.value)
                    }
                }

                LazyHorizontalGrid(
                    rows = if (flashDeals.size < 6) GridCells.Fixed(1) else GridCells.Fixed(2),
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .padding(start = 12.dp)
                        .onGloballyPositioned {
                            currentPageState.intValue = currentPage.value
                        },
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    items(flashDeals.size) { data ->
                        val product = flashDeals[data].toProductUiData()
                        Box(modifier = Modifier.padding(vertical = 5.dp)) {
                            FlashDealItem(
                                totalPersonsRequired = flashDeals[data].totalPersonsRequired,
                                totalPersonsJoined = flashDeals[data].totalPersonsJoined,
                                data = product,
                                cardClick = { onCardClick(flashDeals[data], product) },
                                onAddClick = { onAddClick(flashDeals[data], product) }
                            )
                        }
                    }
                }

                BottomSection(
                    size = totalPages - 1,
                    index = currentPageState.intValue,
                    color = PinkPager,
                    modifier = Modifier.fillMaxWidth(),
                    isMatchIndicator = false
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

fun FlashDealUiData.toProductUiData() = ProductUiData(
    id = productId,
    flashDealId = id,
    name = productName,
    description = EMPTY_STRING,
    imageUrl = productImage,
    price = originalPrice,
    finalPrice = ZERO,
    discountedPrice = dealPrice,
    inventoryQty = 0,
    productCategory = null,
    purchasePrice = ZERO,
    isFavourite = false,
    isBackInStockSubscribed = false,
    isSponsored = false,
    slot = 0,
    isFlashDeal = true,
    showMrp = dealPrice >= originalPrice,
    purchaseLimit = 0,
    itemQuantity = itemQuantity
)

