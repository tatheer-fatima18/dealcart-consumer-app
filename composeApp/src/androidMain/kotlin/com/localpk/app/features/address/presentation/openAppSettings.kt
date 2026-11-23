import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.localpk.app.base.PlatformContext

actual fun openAppSettings() {
    val context = PlatformContext.appContext // Replace with your actual context getter
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:" + context.packageName)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
} 