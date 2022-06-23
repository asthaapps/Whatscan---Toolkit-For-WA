-ignorewarnings

-keep public class com.google.android.gms.ads.**{
   public *;
}


-dontwarn com.google.ads.**

-keep public class com.google.ads.* {*;}


-keep public class com.google.gson.** {
    public protected *;
}

-keep public class com.google.ads.internal.* {*;}
-keep public class com.google.ads.internal.AdWebView.* {*;}
-keep public class com.google.ads.internal.state.AdState.* {*;}
-keep public class com.google.ads.searchads.* {*;}
-keep public class com.google.ads.util.* {*;}

-keep public class com.whatscan.toolkit.forwa.Api.** {*;}
-keep public class com.whatscan.toolkit.forwa.GetSet.** {*;}
-keep public class com.whatscan.toolkit.forwa.DataBaseHelper.** {*;}