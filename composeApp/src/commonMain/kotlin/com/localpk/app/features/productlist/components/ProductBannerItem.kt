package com.localpk.app.features.productlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.banner.data.Banner
import com.localpk.app.utils.BUNDLE
import com.localpk.app.utils.DEAL
import com.localpk.app.utils.EXTERNAL
import com.localpk.app.utils.PRODUCT
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.placeholder_home_banner

@Composable
fun ProductBannerItem(
    banner: Banner,
    onBannerClick: (Long) -> Unit,
    onBannerToProductClick: (Long) -> Unit,
    onBannerToBottomSheetClick: () -> Unit,
    onBannerToVideoClick: (String) -> Unit
) {
    loadImagePainterWithValidation(
        imageUrl = banner.image?.path,
        fallbackRes = Res.drawable.placeholder_home_banner,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(7f / 2f)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onBannerClick(banner.id)
                when (banner.redirectType) {
                    PRODUCT, DEAL -> onBannerToProductClick(banner.redirectTypeId.getValueOrDefault())
                    BUNDLE -> {
                        onBannerToBottomSheetClick()
                    }

                    EXTERNAL -> onBannerToVideoClick(banner.redirectURL)
                }
            },
        contentScale = ContentScale.Crop
    )
}