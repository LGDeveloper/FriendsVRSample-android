# Using the Google Cardboard SDK on mutiple architectures.

By default, the Cardboard SDK targets the armeabi-v7a architecture and the .aar
files in the **libraries** modules only have armeabi-v7a components. This is
sufficient for targeting the majority of Android devices since these libraries
can be used on both armeabi-v7a and arm64-v8a phones. Some x86 Android devices
also support armeabi-v7a native code.

In certain cases, native components for other architectures are required.
Alternative versions of the Cardboard SDK native libraries are included in this
directory and can be included in your app in necessary. The following
instructions explain how to include alternative architectures in the
**samples/simplepanowidget** demo.

1. Build the standard existing **samples/simplepanowidget** demo via Android
Studio or `./gradlew :samples-simplepanowidget:build`. Examine the contents of
the resulting application by extracting the APK or using `unzip -l
samples/simplepanowidget/build/outputs/apk/samples-simplepanowidget-debug.apk |
grep lib.*so`. The basic sample will only show a single native library at
**lib/armeabi-v7a/libpanorenderer.so**. For your own app, note all the
architectures and libraries that your application includes.
1. Add other architures to the application. For **simplepanowidget**, this is
done by editing the **samples/simplepanowidget/build.gradle** file and
uncommenting the **sourceSets** section which configures the build to include
the contents of **simplepanowidget/jniLibs/** in the final APK. Create this
**jniLibs** directory and copy the contents of the appropriate folder in
the **libraries/native** directories to this **jniLibs** directory. For example,
adding support for x86 devices to **simplepanowidget**, would require copying
**x86/libpanorenderer.so** to **simplepanowidget/jniLibs/x86/libpanorenderer.so**.
1. Once the required native libraries are copied, rebuild the app in Android
Studio or via `./gradlew clean` and `./gradlew :samples-simplepanowidget:build`.
Examining the new APK will now show an additional native library at
**lib/x86/libpanorenderer.so**.
1. If you do not need the default armeabi-v7a library, it can be removed using a
**packagingOptions** step in the appropriate **build.gradle** file. Uncommenting
this section in **samples/simplepanowidget/build.gradle** and rebuilding the app
will remove **lib/armeabi-v7a/libpanorenderer.so** from the APK.
