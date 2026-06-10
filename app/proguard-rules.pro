# Add project specific ProGuard rules here.

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile# Please add these rules to your existing keep rules in order to suppress warnings.
                                      ## This is generated automatically by the Android Gradle plugin.
                                      #-dontwarn com.google.android.apps.nbu.paisa.inapp.client.api.PaymentsClient
                                      #-dontwarn com.google.android.apps.nbu.paisa.inapp.client.api.Wallet
                                      #-dontwarn com.google.android.apps.nbu.paisa.inapp.client.api.WalletUtils

# Keep Proguard annotations
-keep class proguard.annotation.** { *; }
-dontwarn proguard.annotation.**

# Fix common missing warnings for Android libraries
-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.util.concurrent.ListenableFuture
-dontwarn androidx.appcompat.view.ContextThemeWrapper
# Replace 'com.example.naguorg' with the actual package where your data models live
-keep class com.example.naguorg.** { *; }

# If you use GSON for serialization/deserialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keepclassmembers class com.example.naguorg.** {
    <fields>;
}
-keep class com.razorpay.** {*;}
