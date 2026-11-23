package com.localpk.app.features.banner.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.base.data.UserSettingsFlowData
import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.banner.data.BannerUseCaseParams
import com.localpk.app.features.banner.domain.BannerUseCase
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.utils.CATEGORY
import com.localpk.app.utils.COINS_CARD
import com.localpk.app.utils.COMMERCIAL
import com.localpk.app.utils.CRICKET
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.HOME
import com.localpk.app.utils.SAVINGS_CARD
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNullOrEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BannerViewModel(
    private val bannerUseCase: BannerUseCase,
    private val userSettingsFlowData: UserSettingsFlowData,
) : BaseViewModel<List<Banner>>() {

    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners: StateFlow<List<Banner>> = _banners.asStateFlow()

    private val _categoryBanners = MutableStateFlow<List<Banner>>(emptyList())
    val categoryBanners: StateFlow<List<Banner>> = _categoryBanners.asStateFlow()

    private val _commercialBanners = MutableStateFlow<List<Banner>>(emptyList())
    val commercialBanners: StateFlow<List<Banner>> = _commercialBanners.asStateFlow()

    private val _coinsCardBanners = MutableStateFlow<List<Banner>>(emptyList())
    val coinsCardBanners: StateFlow<List<Banner>> = _coinsCardBanners.asStateFlow()

    private val _savingsCardBanners = MutableStateFlow<List<Banner>>(emptyList())
    val savingsCardBanners: StateFlow<List<Banner>> = _savingsCardBanners.asStateFlow()

    private val _cricketBanners = MutableStateFlow<List<Banner>>(emptyList())
    val cricketBanners: StateFlow<List<Banner>> = _cricketBanners.asStateFlow()


    fun getBanners(
        belongsTo: String = EMPTY_STRING,
        bannerType: String = SwimlaneType.HOME.type,
        categoryId: Long = ZERO_LONG,
        bannerCacheVersion: String = userSettingsFlowData.getUserSettings().bannerVersion.default(),
        successCallback: () -> Unit = {}
    ) {
        unZipViewStateFlow(
            viewStateFlow = bannerUseCase(
                BannerUseCaseParams(
                    belongsTo = belongsTo,
                    bannerCacheVersion = bannerCacheVersion
                )
            ),
            onSuccess = { banners ->
                // Defensive null check - ensure banners is not null before processing
                val safeBanners = banners ?: emptyList()
                if (safeBanners.isNotNullOrEmpty()) {
                    when (bannerType) {

                        HOME -> {
                            if (belongsTo == CRICKET) {
                                _cricketBanners.value =
                                    safeBanners.filter { it.bannerType == bannerType }
                                        .sortedBy { it.order }
                            } else {
                                _banners.value =
                                    safeBanners.filter { it.bannerType == bannerType }
                                        .sortedBy { it.order }
                                _commercialBanners.value =
                                    safeBanners.filter { it.bannerType == COMMERCIAL }
                                        .sortedBy { it.order }
                            }
                        }

                        CATEGORY -> _categoryBanners.value =
                            safeBanners.filter { it.bannerType == bannerType && it.bannerTypeId == categoryId }
                                .sortedBy { it.order }

                        SAVINGS_CARD -> {
                            _savingsCardBanners.value =
                                safeBanners.filter { it.bannerType == bannerType }
                                    .sortedBy { it.order }
                        }

                        COINS_CARD -> {
                            _coinsCardBanners.value =
                                safeBanners.filter { it.bannerType == bannerType }
                                    .sortedBy { it.order }
                        }
                    }
                }
                successCallback()
            },
            onError = { viewError ->
            }
        )
    }
} 