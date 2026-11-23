-dontwarn io.ktor.utils.io.jvm.nio.WritingKt
# Singular SDK ProGuard rules
-keep class com.singular.sdk.** { *; }
-keep public class com.android.installreferrer.** { *; }
# Uncomment this line in case your are calling the 'revenue' function using the Google billing library
#-keep public class com.android.billingclient.** { *; }
# Desugaring support for java.time APIs (kotlinx.datetime uses java.time internally)
# Note: desugar_jdk_libs 2.x handles most rules automatically, but keeping these for safety
-keep class j$.time.** { *; }
-keep class j$.util.DesugarTimeZone { *; }
-dontwarn j$.time.**
#Mergn
-keep public class com.mergn.insights.classes.** {
public protected *;
}
-keep class com.mergn.insights.networkservices.API.** { *; }
-keep class com.mergn.insights.networkservices.requests.** { *; }
-keep class com.mergn.insights.networkservices.responses.** { *; }
-keep class com.mergn.insights.networkservices.RetrofitClient { *; }
-keep class com.mergn.insights.firebaseservices.** { *; }
-keep class com.mergn.insights.views.** { *; }
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
# Keep Serializable classes from being obfuscated
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable { *; }