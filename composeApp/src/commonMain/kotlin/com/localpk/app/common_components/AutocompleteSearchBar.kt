package com.localpk.app.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.features.wallet.components.getPlatformContext
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompleteSearchBar(
    onLocationSelected: (Geolocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val platformContext = getPlatformContext()
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<GooglePlace>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val textFieldValue = remember { mutableStateOf(androidx.compose.ui.text.input.TextFieldValue(EMPTY_STRING)) }

    // 3 second debounce: search only after user stops typing for 3 seconds (and query has 5+ chars)
    LaunchedEffect(searchQuery) {
        if (searchQuery.length < RemoteConfigs.getSearchPlacesCharacterLimit()) {
            searchResults = emptyList()
            isSearching = false
            return@LaunchedEffect
        }
        delay(RemoteConfigs.getSearchPlacesDebounceTime().toLong())
        val query = searchQuery
        if (query.length < RemoteConfigs.getSearchPlacesCharacterLimit()) return@LaunchedEffect
        isSearching = true
        try {
            searchResults = searchGooglePlaces(query, platformContext)
        } catch (e: Exception) {
            searchResults = emptyList()
        } finally {
            isSearching = false
        }
    }

    Box(modifier = modifier.zIndex(9999f)) {
        Column {
            OutlinedTextField(
                value = textFieldValue.value,
                onValueChange = { newValue ->
                    textFieldValue.value = newValue
                    searchQuery = newValue.text
                    showDropdown = newValue.text.isNotEmpty()
                    if (newValue.text.length < RemoteConfigs.getSearchPlacesCharacterLimit()) {
                        searchResults = emptyList()
                        isSearching = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp)),
                placeholder = {
                    Text(
                        text = "Search location in Pakistan...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSubdued
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSubdued
                    )
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily()
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                shape = RoundedCornerShape(12.dp)
            )
            
            // Dropdown with search results
            if (showDropdown && (searchResults.isNotEmpty() || isSearching)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    if (isSearching) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = TextSubdued
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            items(searchResults) { place ->
                                SearchResultItem(
                                    place = place,
                                    onClick = {
                                        onLocationSelected(
                                            Geolocation(place.latitude, place.longitude)
                                        )
                                        searchQuery = place.name
                                        // Move cursor to end of text
                                        textFieldValue.value = textFieldValue.value.copy(
                                            text = place.name,
                                            selection = androidx.compose.ui.text.TextRange(place.name.length)
                                        )
                                        showDropdown = false
                                        searchResults = emptyList()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    place: GooglePlace,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .zIndex(100f)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = place.address,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSubdued
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Data class for Google Places results
data class GooglePlace(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

// Function to search Google Places API
// This will be implemented differently for Android and iOS
expect suspend fun searchGooglePlaces(query: String, context: Any? = null): List<GooglePlace>

