#include <jni.h>
#include <string>

// Secrets were moved to secrets.properties / BuildConfig.
// Keep non-sensitive prod URL helpers for any remaining JNI callers.

extern "C" JNIEXPORT jstring
JNICALL
Java_com_localpk_app_features_address_data_Keys_accessKey(JNIEnv *env, jobject thiz) {
    std::string accessKey = "";
    return env->NewStringUTF(accessKey.c_str());
}

extern "C" JNIEXPORT jstring
JNICALL
Java_com_localpk_app_base_BaseUrls_getBaseUrl(JNIEnv *env, jobject object) {
    std::string baseUrl = "https://api.dealcart.io";
    return env->NewStringUTF(baseUrl.c_str());
}


extern "C" JNIEXPORT jstring
JNICALL
Java_com_localpk_app_base_BaseUrls_getAgentBaseUrl(JNIEnv *env, jobject object) {
    std::string baseUrl = "https://agent-api.dealcart.io";
    return env->NewStringUTF(baseUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_appId(JNIEnv *env, jobject thiz) {
    std::string packageName = "com.localpk.android";
    return env->NewStringUTF(packageName.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_getDolatBaseUrl(JNIEnv *env, jobject thiz) {
    std::string dolatBaseUrl = "https://dolat-api.dealcart.io";
    return env->NewStringUTF(dolatBaseUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_getSportsBaseUrl(JNIEnv *env, jobject thiz) {
    std::string sportsBaseUrl = "https://sports.dealcart.io";
    return env->NewStringUTF(sportsBaseUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_getLinksBaseUrl(JNIEnv *env, jobject thiz) {
    std::string linksBaseUrl = "https://links.dealcart.io";
    return env->NewStringUTF(linksBaseUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_cardPublicKey(JNIEnv *env, jobject thiz) {
    std::string cardPublicKey = "";
    return env->NewStringUTF(cardPublicKey.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_cardHmacSecretKey(JNIEnv *env, jobject thiz) {
    std::string cardHmacSecretKey = "";
    return env->NewStringUTF(cardHmacSecretKey.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_cardAccountId(JNIEnv *env, jobject thiz) {
    std::string cardAccountId = "";
    return env->NewStringUTF(cardAccountId.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_getFoodBaseUrl(JNIEnv *env, jobject thiz) {
    std::string foodBaseUrl = "https://food-api.dealcart.io";
    return env->NewStringUTF(foodBaseUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_localpk_app_base_BaseUrls_mergnApiKey(JNIEnv *env, jobject thiz) {
    std::string mergnApiKey = "";
    return env->NewStringUTF(mergnApiKey.c_str());
}
