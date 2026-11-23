package com.localpk.app.common_components

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun searchGooglePlaces(query: String, context: Any?): List<GooglePlace> = withContext(Dispatchers.Default) {
    searchPlacesWebAPI(query)
}
