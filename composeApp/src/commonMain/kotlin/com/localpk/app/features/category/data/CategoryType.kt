package com.localpk.app.features.category.data

import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING

enum class CategoryType(val value: String) {
    SUPER_PARENT("super_parent"),
    PARENT("parent"),
    CHILD("child")
}


enum class SuperParent(val properName: String, val id: Long) {
    FOOD(properName = "Food", id = RemoteConfigs.getFoodSuperParentId().toLong()),
    GROCERY(properName = EMPTY_STRING, id = 497),
    LIFESTYLE(properName = EMPTY_STRING, id = 1517),
    BEAUTYHUB(properName = EMPTY_STRING, id = 1516),
    FASHION(properName = EMPTY_STRING, id = 1514),
    ELECTRONICS(properName = EMPTY_STRING, id = 1620)
}