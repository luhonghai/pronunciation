# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}


-keep public class * extends com.cmg.android.bbcaccent.BaseActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.support.v7.app.Fragment
-keep public class * extends android.support.v7.app.DialogFragment
-keep public class * extends com.actionbarsherlock.app.SherlockListFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends com.luhonghai.litedb.LiteDatabaseHelper

-keep class com.cmg.android.bbcaccent.http.**{
    public protected private *;
}

-keep class com.cmg.android.bbcaccent.data.**{
    public protected private *;
}

-keep class com.cmg.android.bbcaccent.data.dto.**{
    public protected private *;
}

-keep class com.cmg.android.bbcaccent.auth.**{
    public protected private *;
}

-keep class com.cmg.android.bbcaccent.data.sqllite.**{
    public protected private *;
}

-keep public class com.cmg.android.bbcaccent.data.sqllite.FreestyleDatabaseHelper{
      public protected private *;
}

-keep public class com.cmg.android.bbcaccent.subscription.IAPFactory{
      public protected private *;
}

-keep class com.google.code.gson.**{
     public protected private *;
}

-keep class cn.pedant.sweetalert.**{
    public protected private *;
}

-keep class com.luhonghai.litedb.**{
     public protected private *;
}

# for facebook
-keep class com.facebook.** {
   *;
}
-keepattributes Signature
-keepattributes *Annotation*


#for google plus

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }

#sweetAlert
 -keep class cn.pedant.SweetAlert.Rotate3dAnimation {
    *;
 }
 -keep class cn.pedant.SweetAlert.** {
    *;
 }
# for butterknife library
-keep class butterknife.** { *; }

-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}



# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn android.support.**
-dontwarn com.google.ads.**
-dontwarn com.actionbarsherlock.internal.**
-dontwarn cn.pedant.SweetAlert.**
-dontwarn it.sephiroth.android.**
-dontwarn com.google.api.client.extensions.android.**
-dontwarn com.google.api.client.googleapis.extensions.android.**
-dontwarn com.google.android.gms.**
-dontnote java.nio.file.Files, java.nio.file.Path
-dontnote **.ILicensingService
-dontnote sun.misc.Unsafe
-dontwarn sun.misc.Unsafe
-dontwarn butterknife.internal.**