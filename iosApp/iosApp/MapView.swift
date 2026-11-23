import ComposeApp
import GoogleMaps
import SwiftUI

struct MapView: UIViewRepresentable {
    var onLocationSelected: ((KotlinDouble, KotlinDouble) -> KotlinUnit)?
    var initialLat: KotlinDouble?
    var initialLng: KotlinDouble?
    var initialZoom: KotlinDouble?

    func makeUIView(context: Context) -> GMSMapView {
        let options = GMSMapViewOptions()
        let gmsMapView = GMSMapView(options: options)
        gmsMapView.delegate = context.coordinator
        gmsMapView.isMyLocationEnabled = false
        context.coordinator.onLocationSelected = onLocationSelected
        context.coordinator.mapView = gmsMapView

        // Set initial camera position if provided
        if let lat = initialLat?.doubleValue,
           let lng = initialLng?.doubleValue,
           let zoom = initialZoom?.doubleValue {
            let camera = GMSCameraPosition.camera(
                withLatitude: lat,
                longitude: lng,
                zoom: Float(zoom)
            )
            gmsMapView.camera = camera
        }
        return gmsMapView
    }

    func updateUIView(_ uiView: GMSMapView, context: Context) {
        guard let lat = initialLat?.doubleValue,
              let lng = initialLng?.doubleValue,
              let zoom = initialZoom?.doubleValue else { return }

        let currentTarget = uiView.camera.target
        let currentZoom = uiView.camera.zoom

        let latChanged = abs(currentTarget.latitude - lat) > 0.000001
        let lngChanged = abs(currentTarget.longitude - lng) > 0.000001
        let zoomChanged = abs(Double(currentZoom) - zoom) > 0.000001

        if latChanged || lngChanged || zoomChanged {
            let newCamera = GMSCameraPosition.camera(
                withLatitude: lat,
                longitude: lng,
                zoom: Float(zoom)
            )

            // Instantly move camera on first load, animate afterwards
            if uiView.camera.target.latitude == 0.0 && uiView.camera.target.longitude == 0.0 {
                uiView.camera = newCamera
            } else {
                uiView.animate(to: newCamera)
            }
        }
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    class Coordinator: NSObject, GMSMapViewDelegate {
        var onLocationSelected: ((KotlinDouble, KotlinDouble) -> KotlinUnit)?
        weak var mapView: GMSMapView?

        func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
            onLocationSelected?(
                KotlinDouble(value: position.target.latitude),
                KotlinDouble(value: position.target.longitude)
            )
        }

        func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
            mapView.animate(
                to: GMSCameraPosition.camera(
                    withLatitude: coordinate.latitude,
                    longitude: coordinate.longitude,
                    zoom: 18.0
                )
            )
            // Let idleAt handle the callback
        }

        func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
            return true
        }
    }
}
