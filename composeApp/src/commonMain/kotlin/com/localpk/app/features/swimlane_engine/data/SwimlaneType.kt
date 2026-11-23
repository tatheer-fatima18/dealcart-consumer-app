package com.localpk.app.features.swimlane_engine.data

enum class SwimlaneType(val type: String) {
    PROMOTIONAL("promotional"),
    CART("cart"),
    HOME("home"),
    NEWLY_ADDED("newly-added"),
    GROUP_DEALS("group-deals"),
    MAX_ABS_DISCOUNT("max-absolute-discount"),
    MIN_AMOUNT_OFF("min-amount-off"),
    MIN_PERCENTAGE_OFF("min-percentage-off"),
    UNDER_AMOUNT("under-amount"),
    BEST_SELLING("best-selling")
}

enum class LandingPageType(val type: String) {
    Main("main"),
    CATEGORY("category")
}