package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.localpk.app.theming.ButtonSurfaceBrand
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.house_loc_user

//@Composable
//fun Map(startLatLng: LatLng, endLatLng: LatLng, path: List<LatLng>?, logo: String) {
//    key(startLatLng) {
//        SafeGoogleMap(
//            modifier = Modifier.fillMaxSize(),
//            cameraPosition = CameraPosition.fromLatLngZoom(endLatLng, 14.0f)
//        ) {
//            MapMarker(
//                latlng = startLatLng,
//                title = "",
//                iconResourceId = Res.drawable.house_loc_user
//            )
//            MapMarkerFromUrl(
//                latlng = endLatLng,
//                title = "",
//                iconUrl = logo
//            )
//
//            if (!path.isNullOrEmpty()) {
//                Polyline(
//                    points = path,
//                    color = ButtonSurfaceBrand,
//                    width = 8f
//                )
//            }
//        }
//    }
//}