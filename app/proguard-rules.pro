# Enable optimizations
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-repackageclasses ''
-renamesourcefileattribute SourceFile
-allowaccessmodification
-optimizations !code/simplification/arithmetic

-dontnote android.net.http.*
-dontnote org.apache.http.**

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes SourceFile
-keepattributes LineNumberTable

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# Preserve static fields of inner classes of R classes that might be accessed through introspection.
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class * extends android.support.v4.view.ActionProvider
-keepclassmembers class * extends android.support.v4.view.ActionProvider {
    <init>(android.content.Context);
}

# Okio
-dontwarn okio.**

# OkHttp
-dontwarn okhttp3.**

# Retrofit
-dontwarn rx.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# RxJava
-dontwarn sun.misc.**
-dontnote rx.internal.util.PlatformDependent

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# IcePick
-dontwarn icepick.**
-keep class **$$Icepick { *; }
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}