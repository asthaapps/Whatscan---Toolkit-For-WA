package com.whatscan.toolkit.forwa.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    //Application
    public static final String keylockPasscode = "passCode";
    private static final String Main_Url = "Main_Url";

    //Api User
    private static final String Login = "Login";
    private static final String Register = "Register";
    private static final String ForgotResend = "forgot_resend";
    private static final String ChangePass = "pass_match";
    private static final String ForgotVerify = "forgot_verify";
    private static final String ForgotEmail = "forgot_pass";
    private static final String RegisterResend = "email_resend";
    private static final String RegisterVerify = "otp_verify";

    //Api Text
    private static final String Font_Changer = "Font_Changer";
    private static final String Font_Decor = "Font_Decor";
    private static final String Cat_Sticker = "Cat_Sticker";
    private static final String Cat_SubSticker = "Cat_SubSticker";
    private static final String Caption_Cat = "Caption_Cat";
    private static final String Caption_SubCat = "Caption_SubCat";
    private static final String Cat_Emoticon = "Cat_Emoticon";
    private static final String Cat_SubEmoticon = "Cat_SubEmoticon";
    private static final String Report_Request = "Report_Request";


    //plan key Monthly
    private static final String classic_monthly_key = "classic_monthly_key";
    private static final String classic_monthly_price = "classic_monthly_price";
    private static final String premium_monthly_key = "premium_monthly_key";
    private static final String premium_monthly_price = "premium_monthly_price";
    private static final String master_monthly_key = "master_monthly_key";
    private static final String master_monthly_price = "master_monthly_price";
    private static final String offer_time_key = "offer_time_key";
    private static final String offer_time_price = "offer_time_price";
//    private static final String offer_time_close_key = "offer_time_close_key";
  //  private static final String offer_time_close_price = "offer_time_close_price";

    //plan key Yearly
    private static final String classic_yearly_key = "classic_yearly_key";
    private static final String classic_yearly_price = "classic_yearly_price";
    private static final String premium_yearly_key = "premium_yearly_key";
    private static final String premium_yearly_price = "premium_yearly_price";
    private static final String master_yearly_key = "master_yearly_key";
    private static final String master_yearly_price = "master_yearly_price";
    private static final String vip_key = "vip_key";
    private static final String vip_price = "vip_price";
    private static final String vip_close_key = "vip_close_key";
    private static final String vip_close_price = "vip_close_price";


    private static final String active_MkeyM = "active_MkeyM";
    private static final String active_MkeyY = "active_MkeyY";
    private static final String active_CkeyM = "active_CkeyM";
    private static final String active_CkeyY = "active_CkeyY";
    private static final String active_PkeyM = "active_PkeyM";
    private static final String active_PkeyY = "active_PkeyY";
    private static final String active_offer = "active_offer";
    private static final String active_vip = "active_vip";

    private static final String Ads_name = "Ads_name";


    //Api Payment
    private static final String Check_Plan = "Check_Plan";
    private static final String Buy_Back = "Buy_Back";
    private static final String Verify_Plan = "Verify_Plan";

    //Extra
    private static final String Font_url = "Font_url";
    private static final String WebUrl = "WebUrl";
    private static final String AgentString = "AgentString";
    private static final String u_id = "u_id";
    private static final String tool_id = "tool_id";
    private static final String android_id = "android_id";
    private static final String Fcm_Id = "Fcm_Id";
    private static final String google_key = "google_key";
    private static final String facebook_key = "facebook_key";
    private static final String Type = "Type";
    private static final String Profile = "Profile";
    private static final String Login_status = "Login_status";
    private static final String Notification_status = "Notification_status";
    private static final String Register_token = "Register_token";
    private static final String token = "token";

    //Other
    private static final String sticker_path = "sticker_path";
    private static final String sticker_url = "sticker_url";
    private static final String Permission_storage = "Permission_storage";
    private static final String Permission_media = "Permission_media";
    private static final String Permission_overlay = "Permission_overlay";
    private static final String Permission_accessbility = "Permission_accessbility";
    private static final String Permission_notification = "Permission_notification";
    private static final String caption_path = "caption_path";
    private static final String Check_Notification = "Check_Notification";
    private static final String CheckFingure = "CheckFingure";
    private static final String PrivacyPolicy = "pp";
    private static final String TermCondition = "tc";
    private static final String BulkDemo = "bulk_demo";
    private static final String ReportDemo = "report_demo";
    private static final String ChatNo = "chat_no";
    private static final String dev_insta = "dev_insta";
    private static final String dev_fb = "dev_fb";
    private static final String dev_twitter = "dev_twitter";
    private static final String dev_youtube = "dev_youtube";
    private static final String dev_mail = "dev_mail";

    private static final String CheckActiveLanguage = "CheckActiveLanguage";
    public static final String SWITCH2 = "globalSwitch";
    public static final String WA_TREE_URI = "wa_tree_uri";

    //Register
    private static final String UserName = "UserName";
    private static final String Mobile = "Mobile";
    private static final String Email = "Email";
    private static final String Password = "Password";
    private static final String CPassword = "CPassword";

    //Advertisement
    private static final String GBanner = "GBanner";
    private static final String GFull = "GFull";
    private static final String GNative = "GNative";
    private static final String App_Open = "App_Open";
    private static final String rewardAds = "rewardAds";

    private static final String base_key = "base_key";
    private static final String PREF_APP = "pref_app";

    //Active Plan Pref


    //Coins
    private static final String check_coin = "check_coin";
    private static final String watch_ad = "watch_ad";
    private static final String tool_use = "tool_use";
    private static final String invitation_code = "invitation_code";
    private static final String first_logIn = "first_logIn";
    private static final String referedCode = "referedCode";
    private static final String coin_history = "coin_history";
    private static final String adFree_coin = "adFree_coin";
    private static final String adFeaturs_coin = "adFeaturs_coin";
    private static final String checkRedeem = "checkRedeem";
    private static final String checkRedemption = "checkRedemption";
    private static final String total_coin = "total_coin";
    private static final String coin1Min = "coin1Min";
    private static final String coin5Min = "coin5Min";
    private static final String coin10Min = "coin10Min";
    private static final String coin20Min = "coin20Min";
    private static final String coin30Min = "coin30Min";
    private static final String login_coin = "login_coin";
    private static final String invitation_coin = "invitation_coin";
    private static final String ads_one_day = "ads_one_day";
    private static final String ads_three_day = "ads_three_day";
    private static final String ads_seven_day = "ads_seven_day";
    private static final String ads_thirty_day = "ads_thirty_day";
    private static final String bulk_one_day = "bulk_one_day";
    private static final String bulk_seven_day = "bulk_seven_day";
    private static final String auto_one_day = "auto_one_day";
    private static final String auto_seven_day = "auto_seven_day";
    private static final String import_one_day = "import_one_day";
    private static final String import_seven_day = "import_seven_day";


    private static SharedPreferences get() {
        return AppController.getApp().getSharedPreferences("AppController", Context.MODE_PRIVATE);
    }

    public static String getGBanner() {
        return get().getString(GBanner, "");
    }

    public static void setGBanner(String value) {
        get().edit().putString(GBanner, value).apply();

    }

    //classic
    public static String getClassic_monthly_key() {
        return get().getString(classic_monthly_key, "");
    }

    public static void setClassic_monthly_key(String value) {
        get().edit().putString(classic_monthly_key, value).apply();
    }

    public static String getClassic_monthly_price() {
        return get().getString(classic_monthly_price, "");
    }

    public static void setClassic_monthly_price(String value) {
        get().edit().putString(classic_monthly_price, value).apply();
    }

    public static String getClassic_yearly_key() {
        return get().getString(classic_yearly_key, "");
    }

    public static void setClassic_yearly_key(String value) {
        get().edit().putString(classic_yearly_key, value).apply();
    }

    public static String getClassic_yearly_price() {
        return get().getString(classic_yearly_price, "");
    }

    public static void setClassic_yearly_price(String value) {
        get().edit().putString(classic_yearly_price, value).apply();
    }

    //premium
    public static String getPremium_monthly_key() {
        return get().getString(premium_monthly_key, "");
    }

    public static void setPremium_monthly_key(String value) {
        get().edit().putString(premium_monthly_key, value).apply();
    }

    public static String getPremium_monthly_price() {
        return get().getString(premium_monthly_price, "");
    }

    public static void setPremium_monthly_price(String value) {
        get().edit().putString(premium_monthly_price, value).apply();
    }

    public static String getPremium_yearly_key() {
        return get().getString(premium_yearly_key, "");
    }

    public static void setPremium_yearly_key(String value) {
        get().edit().putString(premium_yearly_key, value).apply();
    }

    public static String getPremium_yearly_price() {
        return get().getString(premium_yearly_price, "");
    }

    public static void setPremium_yearly_price(String value) {
        get().edit().putString(premium_yearly_price, value).apply();
    }

    //master
    public static String getMaster_yearly_key() {
        return get().getString(master_yearly_key, "");
    }

    public static void setMaster_yearly_key(String value) {
        get().edit().putString(master_yearly_key, value).apply();
    }

    public static String getMaster_yearly_price() {
        return get().getString(master_yearly_price, "");
    }

    public static void setMaster_yearly_price(String value) {
        get().edit().putString(master_yearly_price, value).apply();
    }

    public static String getMaster_monthly_key() {
        return get().getString(master_monthly_key, "");
    }

    public static void setMaster_monthly_key(String value) {
        get().edit().putString(master_monthly_key, value).apply();
    }


    public static String getMaster_monthly_price() {
        return get().getString(master_monthly_price, "");
    }

    public static void setMaster_monthly_price(String value) {
        get().edit().putString(master_monthly_price, value).apply();
    }


    //Offer Time Limited
    public static String getOffer_time_key() {
        return get().getString(offer_time_key, "");
    }

    public static void setOffer_time_key(String value) {
        get().edit().putString(offer_time_key, value).apply();
    }

//    public static String getOffer_time_close_key() {
//        return get().getString(offer_time_close_key, "");
//    }
//
//    public static void setOffer_time_close_key(String value) {
//        get().edit().putString(offer_time_close_key, value).apply();
//    }


    public static String getOffer_time_price() {
        return get().getString(offer_time_price, "");
    }

    public static void setOffer_time_price(String value) {
        get().edit().putString(offer_time_price, value).apply();
    }
//    public static String getOffer_time_close_price() {
//        return get().getString(offer_time_close_price, "");
//    }
//
//    public static void setOffer_time_close_price(String value) {
//        get().edit().putString(offer_time_close_price, value).apply();
//    }


    //Vip
    public static String getVip_key() {
        return get().getString(vip_key, "");
    }

    public static void setVip_key(String value) {
        get().edit().putString(vip_key, value).apply();
    }

    public static String getVip_close_key() {
        return get().getString(vip_close_key, "");
    }

    public static void setVip_close_key(String value) {
        get().edit().putString(vip_close_key, value).apply();
    }

    public static String getVip_price() {
        return get().getString(vip_price, "");
    }

    public static void setVip_price(String value) {
        get().edit().putString(vip_price, value).apply();
    }

    public static String getVip_close_price() {
        return get().getString(vip_close_price, "");
    }

    public static void setVip_close_price(String value) {
        get().edit().putString(vip_close_price, value).apply();
    }


    public static String getGFull() {
        return get().getString(GFull, "");
    }

    public static void setGFull(String value) {
        get().edit().putString(GFull, value).apply();
    }

    public static String getGNative() {
        return get().getString(GNative, "");
    }

    public static void setGNative(String value) {
        get().edit().putString(GNative, value).apply();
    }

    public static String getApp_Open() {
        return get().getString(App_Open, "");
    }

    public static void setApp_Open(String value) {
        get().edit().putString(App_Open, value).apply();
    }


    public static String getRewardAds() {
        return get().getString(rewardAds, "");
    }

    public static void setRewardAds(String value) {
        get().edit().putString(rewardAds, value).apply();
    }


    public static String getActive_MkeyM() {
        return get().getString(active_MkeyM, "");
    }

    public static void setActive_MkeyM(String value) {
        get().edit().putString(active_MkeyM, value).apply();
    }

    public static String getActive_MkeyY() {
        return get().getString(active_MkeyY, "");
    }

    public static void setActive_MkeyY(String value) {
        get().edit().putString(active_MkeyY, value).apply();
    }


    public static String getActive_CkeyM() {
        return get().getString(active_CkeyM, "");
    }

    public static void setActive_CkeyM(String value) {
        get().edit().putString(active_CkeyM, value).apply();
    }

    public static String getActive_CkeyY() {
        return get().getString(active_CkeyY, "");
    }

    public static void setActive_CkeyY(String value) {
        get().edit().putString(active_CkeyY, value).apply();
    }

    public static String getActive_offer() {
        return get().getString(active_offer, "");
    }

    public static void setActive_offer(String value) {
        get().edit().putString(active_offer, value).apply();
    }

    public static String getActive_vip() {
        return get().getString(active_vip, "");
    }

    public static void setActive_vip(String value) {
        get().edit().putString(active_vip, value).apply();
    }

    public static String getActive_PkeyM() {
        return get().getString(active_PkeyM, "");
    }

    public static void setActive_PkeyM(String value) {
        get().edit().putString(active_PkeyM, value).apply();
    }

    public static String getActive_PkeyY() {
        return get().getString(active_PkeyY, "");
    }

    public static void setActive_PkeyY(String value) {
        get().edit().putString(active_PkeyY, value).apply();
    }


    public static String getAds_name() {
        return get().getString(Ads_name, "");
    }

    public static void setAds_name(String value) {
        get().edit().putString(Ads_name, value).apply();
    }


    public static String getBase_key() {
        return get().getString(base_key, "");
    }

    public static void setBase_key(String value) {
        get().edit().putString(base_key, value).apply();
    }

    public static String getSticker_path() {
        return get().getString(sticker_path, "");
    }

    public static void setSticker_path(String value) {
        get().edit().putString(sticker_path, value).apply();
    }

    public static String getSticker_url() {
        return get().getString(sticker_url, "");
    }

    public static void setSticker_url(String value) {
        get().edit().putString(sticker_url, value).apply();
    }

    public static String getPermission_storage() {
        return get().getString(Permission_storage, "");
    }

    public static void setPermission_storage(String value) {
        get().edit().putString(Permission_storage, value).apply();
    }

    public static String getPermission_media() {
        return get().getString(Permission_media, "");
    }

    public static void setPermission_media(String value) {
        get().edit().putString(Permission_media, value).apply();
    }

    public static String getPermission_overlay() {
        return get().getString(Permission_overlay, "");
    }

    public static void setPermission_overlay(String value) {
        get().edit().putString(Permission_overlay, value).apply();
    }

    public static String getPermission_accessbility() {
        return get().getString(Permission_accessbility, "");
    }

    public static void setPermission_accessbility(String value) {
        get().edit().putString(Permission_accessbility, value).apply();
    }

    public static String getPermission_notification() {
        return get().getString(Permission_notification, "");
    }

    public static void setPermission_notification(String value) {
        get().edit().putString(Permission_notification, value).apply();
    }


    public static String getCaption_path() {
        return get().getString(caption_path, "");
    }

    public static void setCaption_path(String value) {
        get().edit().putString(caption_path, value).apply();
    }

    public static String getForgotResend() {
        return get().getString(ForgotResend, "");
    }

    public static void setForgotResend(String value) {
        get().edit().putString(ForgotResend, value).apply();
    }

    public static String getChangePass() {
        return get().getString(ChangePass, "");
    }

    public static void setChangePass(String value) {
        get().edit().putString(ChangePass, value).apply();
    }

    public static String getForgotVerify() {
        return get().getString(ForgotVerify, "");
    }

    public static void setForgotVerify(String value) {
        get().edit().putString(ForgotVerify, value).apply();
    }

    public static String getForgotEmail() {
        return get().getString(ForgotEmail, "");
    }

    public static void setForgotEmail(String value) {
        get().edit().putString(ForgotEmail, value).apply();
    }

    public static String getRegisterResend() {
        return get().getString(RegisterResend, "");
    }

    public static void setRegisterResend(String value) {
        get().edit().putString(RegisterResend, value).apply();
    }

    public static String getRegisterVerify() {
        return get().getString(RegisterVerify, "");
    }

    public static void setRegisterVerify(String value) {
        get().edit().putString(RegisterVerify, value).apply();
    }

    public static String getMain_Url() {
        return get().getString(Main_Url, "");
    }

    public static void setMain_Url(String value) {
        get().edit().putString(Main_Url, value).apply();
    }

    public static String getU_id() {
        return get().getString(u_id, "");
    }

    public static void setU_id(String value) {
        get().edit().putString(u_id, value).apply();
    }

    public static String getTool_id() {
        return get().getString(tool_id, "");
    }

    public static void setTool_id(String value) {
        get().edit().putString(tool_id, value).apply();
    }

    public static String getWebUrl() {
        return get().getString(WebUrl, "");
    }

    public static void setWebUrl(String value) {
        get().edit().putString(WebUrl, value).apply();
    }

    public static String getAgentString() {
        return get().getString(AgentString, "");
    }

    public static void setAgentString(String value) {
        get().edit().putString(AgentString, value).apply();
    }

    public static String getTermCondition() {
        return get().getString(TermCondition, "");
    }

    public static void setTermCondition(String value) {
        get().edit().putString(TermCondition, value).apply();
    }

    public static String getBulkDemo() {
        return get().getString(BulkDemo, "");
    }

    public static void setBulkDemo(String value) {
        get().edit().putString(BulkDemo, value).apply();
    }

    public static String getReportDemo() {
        return get().getString(ReportDemo, "");
    }

    public static void setReportDemo(String value) {
        get().edit().putString(ReportDemo, value).apply();
    }

    public static String getChatNo() {
        return get().getString(ChatNo, "");
    }

    public static void setChatNo(String value) {
        get().edit().putString(ChatNo, value).apply();
    }

    public static String getDev_insta() {
        return get().getString(dev_insta, "");
    }

    public static void setDev_insta(String value) {
        get().edit().putString(dev_insta, value).apply();
    }

    public static String getDev_fb() {
        return get().getString(dev_fb, "");
    }

    public static void setDev_fb(String value) {
        get().edit().putString(dev_fb, value).apply();
    }

    public static String getDev_twitter() {
        return get().getString(dev_twitter, "");
    }

    public static void setDev_twitter(String value) {
        get().edit().putString(dev_twitter, value).apply();
    }

    public static String getDev_youtube() {
        return get().getString(dev_youtube, "");
    }

    public static void setDev_youtube(String value) {
        get().edit().putString(dev_youtube, value).apply();
    }

    public static String getDev_mail() {
        return get().getString(dev_mail, "");
    }

    public static void setDev_mail(String value) {
        get().edit().putString(dev_mail, value).apply();
    }


    public static String getCheckActiveLanguage() {
        return get().getString(CheckActiveLanguage, "");
    }

    public static void setCheckActiveLanguage(String value) {
        get().edit().putString(CheckActiveLanguage, value).apply();
    }

    public static String getPrivacyPolicy() {
        return get().getString(PrivacyPolicy, "");
    }

    public static void setPrivacyPolicy(String value) {
        get().edit().putString(PrivacyPolicy, value).apply();
    }

    public static String getCheckFingure() {
        return get().getString(CheckFingure, "");
    }

    public static void setCheckFingure(String value) {
        get().edit().putString(CheckFingure, value).apply();
    }


    public static String getUserName() {
        return get().getString(UserName, "");
    }

    public static void setUserName(String value) {
        get().edit().putString(UserName, value).apply();
    }

    public static String getMobile() {
        return get().getString(Mobile, "");
    }

    public static void setMobile(String value) {
        get().edit().putString(Mobile, value).apply();
    }

    public static String getEmail() {
        return get().getString(Email, "");
    }

    public static void setEmail(String value) {
        get().edit().putString(Email, value).apply();
    }

    public static String getPassword() {
        return get().getString(Password, "");
    }

    public static void setPassword(String value) {
        get().edit().putString(Password, value).apply();
    }

    public static String getCPassword() {
        return get().getString(CPassword, "");
    }

    public static void setCPassword(String value) {
        get().edit().putString(CPassword, value).apply();
    }

    public static String getLogin() {
        return get().getString(Login, "");
    }

    public static void setLogin(String value) {
        get().edit().putString(Login, value).apply();
    }

    public static String getRegister() {
        return get().getString(Register, "");
    }

    public static void setRegister(String value) {
        get().edit().putString(Register, value).apply();
    }

    public static String getFont_url() {
        return get().getString(Font_url, "");
    }

    public static void setFont_url(String value) {
        get().edit().putString(Font_url, value).apply();
    }

    public static String getFont_Changer() {
        return get().getString(Font_Changer, "");
    }

    public static void setFont_Changer(String value) {
        get().edit().putString(Font_Changer, value).apply();
    }

    public static String getFont_Decor() {
        return get().getString(Font_Decor, "");
    }

    public static void setFont_Decor(String value) {
        get().edit().putString(Font_Decor, value).apply();
    }

    public static String getCat_Sticker() {
        return get().getString(Cat_Sticker, "");
    }

    public static void setCat_Sticker(String value) {
        get().edit().putString(Cat_Sticker, value).apply();
    }

    public static String getCat_SubSticker() {
        return get().getString(Cat_SubSticker, "");
    }

    public static void setCat_SubSticker(String value) {
        get().edit().putString(Cat_SubSticker, value).apply();
    }

    public static String getCaption_Cat() {
        return get().getString(Caption_Cat, "");
    }

    public static void setCaption_Cat(String value) {
        get().edit().putString(Caption_Cat, value).apply();
    }

    public static String getCaption_SubCat() {
        return get().getString(Caption_SubCat, "");
    }

    public static void setCaption_SubCat(String value) {
        get().edit().putString(Caption_SubCat, value).apply();
    }

    public static String getCat_Emoticon() {
        return get().getString(Cat_Emoticon, "");
    }

    public static void setCat_Emoticon(String value) {
        get().edit().putString(Cat_Emoticon, value).apply();
    }

    public static String getCat_SubEmoticon() {
        return get().getString(Cat_SubEmoticon, "");
    }

    public static void setCat_SubEmoticon(String value) {
        get().edit().putString(Cat_SubEmoticon, value).apply();
    }

    public static String getReport_Request() {
        return get().getString(Report_Request, "");
    }

    public static void setReport_Request(String value) {
        get().edit().putString(Report_Request, value).apply();
    }

    public static String getCheck_Plan() {
        return get().getString(Check_Plan, "");
    }

    public static void setCheck_Plan(String value) {
        get().edit().putString(Check_Plan, value).apply();
    }

    public static String getVerify_Plan() {
        return get().getString(Verify_Plan, "");
    }

    public static void setVerify_Plan(String value) {
        get().edit().putString(Verify_Plan, value).apply();
    }

    public static String getBuy_Back() {
        return get().getString(Buy_Back, "");
    }

    public static void setBuy_Back(String value) {
        get().edit().putString(Buy_Back, value).apply();
    }

    public static String getFcm_Id() {
        return get().getString(Fcm_Id, "");
    }

    public static void setFcm_Id(String value) {
        get().edit().putString(Fcm_Id, value).apply();
    }

    public static String getAndroid_id() {
        return get().getString(android_id, "");
    }

    public static void setAndroid_id(String value) {
        get().edit().putString(android_id, value).apply();
    }

    public static String getGoogle_key() {
        return get().getString(google_key, "");
    }

    public static void setGoogle_key(String value) {
        get().edit().putString(google_key, value).apply();
    }

    public static String getFacebook_key() {
        return get().getString(facebook_key, "");
    }

    public static void setFacebook_key(String value) {
        get().edit().putString(facebook_key, value).apply();
    }

    public static String getType() {
        return get().getString(Type, "");
    }

    public static void setType(String value) {
        get().edit().putString(Type, value).apply();
    }

    public static String getLogin_status() {
        return get().getString(Login_status, "");
    }

    public static void setLogin_status(String value) {
        get().edit().putString(Login_status, value).apply();
    }

    public static String getNotification_status() {
        return get().getString(Notification_status, "");
    }

    public static void setNotification_status(String value) {
        get().edit().putString(Notification_status, value).apply();
    }

    public static String getCheck_Notification() {
        return get().getString(Check_Notification, "");
    }

    public static void setCheck_Notification(String value) {
        get().edit().putString(Check_Notification, value).apply();
    }

    public static String getRegister_token() {
        return get().getString(Register_token, "");
    }

    public static void setRegister_token(String value) {
        get().edit().putString(Register_token, value).apply();
    }

    public static String getToken() {
        return get().getString(token, "");
    }

    public static void setToken(String value) {
        get().edit().putString(token, value).apply();
    }

    public static String getProfile() {
        return get().getString(Profile, "");
    }

    public static void setProfile(String value) {
        get().edit().putString(Profile, value).apply();
    }

    public static String getCheck_coin() {
        return get().getString(check_coin, "");
    }

    public static void setCheck_coin(String value) {
        get().edit().putString(check_coin, value).apply();
    }

    public static String getWatch_ad() {
        return get().getString(watch_ad, "");
    }

    public static void setWatch_ad(String value) {
        get().edit().putString(watch_ad, value).apply();
    }

    public static String getTool_use() {
        return get().getString(tool_use, "");
    }

    public static void setTool_use(String value) {
        get().edit().putString(tool_use, value).apply();
    }

    public static String getInvitation_code() {
        return get().getString(invitation_code, "");
    }

    public static void setInvitation_code(String value) {
        get().edit().putString(invitation_code, value).apply();
    }

    public static String getFirst_logIn() {
        return get().getString(first_logIn, "");
    }

    public static void setFirst_logIn(String value) {
        get().edit().putString(first_logIn, value).apply();
    }

    public static String getReferedCode() {
        return get().getString(referedCode, "");
    }

    public static void setReferedCode(String value) {
        get().edit().putString(referedCode, value).apply();
    }

    public static String getCoin_history() {
        return get().getString(coin_history, "");
    }

    public static void setCoin_history(String value) {
        get().edit().putString(coin_history, value).apply();
    }

    public static String getAdFree_coin() {
        return get().getString(adFree_coin, "");
    }

    public static void setAdFree_coin(String value) {
        get().edit().putString(adFree_coin, value).apply();
    }

    public static String getAdFeaturs_coin() {
        return get().getString(adFeaturs_coin, "");
    }

    public static void setAdFeaturs_coin(String value) {
        get().edit().putString(adFeaturs_coin, value).apply();
    }

    public static String getCheckRedeem() {
        return get().getString(checkRedeem, "");
    }

    public static void setCheckRedeem(String value) {
        get().edit().putString(checkRedeem, value).apply();
    }

    public static String getCheckRedemption() {
        return get().getString(checkRedemption, "");
    }

    public static void setCheckRedemption(String value) {
        get().edit().putString(checkRedemption, value).apply();
    }

    public static String getTotal_coin() {
        return get().getString(total_coin, "");
    }

    public static void setTotal_coin(String value) {
        get().edit().putString(total_coin, value).apply();
    }

    public static String getCoin1Min() {
        return get().getString(coin1Min, "");
    }

    public static void setCoin1Min(String value) {
        get().edit().putString(coin1Min, value).apply();
    }

    public static String getCoin10Min() {
        return get().getString(coin10Min, "");
    }

    public static void setCoin10Min(String value) {
        get().edit().putString(coin10Min, value).apply();
    }

    public static String getCoin5Min() {
        return get().getString(coin5Min, "");
    }

    public static void setCoin5Min(String value) {
        get().edit().putString(coin5Min, value).apply();
    }

    public static String getCoin20Min() {
        return get().getString(coin20Min, "");
    }

    public static void setCoin20Min(String value) {
        get().edit().putString(coin20Min, value).apply();
    }

    public static String getCoin30Min() {
        return get().getString(coin30Min, "");
    }

    public static void setCoin30Min(String value) {
        get().edit().putString(coin30Min, value).apply();
    }

    public static String getLogin_coin() {
        return get().getString(login_coin, "");
    }

    public static void setLogin_coin(String value) {
        get().edit().putString(login_coin, value).apply();
    }

    public static String getInvitation_coin() {
        return get().getString(invitation_coin, "");
    }

    public static void setInvitation_coin(String value) {
        get().edit().putString(invitation_coin, value).apply();
    }

    public static String getAds_one_day() {
        return get().getString(ads_one_day, "");
    }

    public static void setAds_one_day(String value) {
        get().edit().putString(ads_one_day, value).apply();
    }

    public static String getAds_three_day() {
        return get().getString(ads_three_day, "");
    }

    public static void setAds_three_day(String value) {
        get().edit().putString(ads_three_day, value).apply();
    }

    public static String getAds_seven_day() {
        return get().getString(ads_seven_day, "");
    }

    public static void setAds_seven_day(String value) {
        get().edit().putString(ads_seven_day, value).apply();
    }

    public static String getAds_thirty_day() {
        return get().getString(ads_thirty_day, "");
    }

    public static void setAds_thirty_day(String value) {
        get().edit().putString(ads_thirty_day, value).apply();
    }

    public static String getBulk_one_day() {
        return get().getString(bulk_one_day, "");
    }

    public static void setBulk_one_day(String value) {
        get().edit().putString(bulk_one_day, value).apply();
    }

    public static String getBulk_seven_day() {
        return get().getString(bulk_seven_day, "");
    }

    public static void setBulk_seven_day(String value) {
        get().edit().putString(bulk_seven_day, value).apply();
    }

    public static String getAuto_one_day() {
        return get().getString(auto_one_day, "");
    }

    public static void setAuto_one_day(String value) {
        get().edit().putString(auto_one_day, value).apply();
    }

    public static String getAuto_seven_day() {
        return get().getString(auto_seven_day, "");
    }

    public static void setAuto_seven_day(String value) {
        get().edit().putString(auto_seven_day, value).apply();
    }

    public static String getImport_one_day() {
        return get().getString(import_one_day, "");
    }

    public static void setImport_one_day(String value) {
        get().edit().putString(import_one_day, value).apply();
    }

    public static String getImport_seven_day() {
        return get().getString(import_seven_day, "");
    }

    public static void setImport_seven_day(String value) {
        get().edit().putString(import_seven_day, value).apply();
    }

    public static void setBooleanTheme(Boolean bool) {
        get().edit().putBoolean(SWITCH2, bool).apply();
    }

    public static Boolean getBooleanTheme(Boolean bool) {
        return get().getBoolean(SWITCH2, bool);
    }

    public static void setWATree(Context context, String str) {
        get().edit().putString(WA_TREE_URI, str).apply();
    }

    public static String getWATree(Context context) {
        return get().getString(WA_TREE_URI, "");
    }

    public static void setBoolean(Context context, String str, String str2, Boolean bool) {
        get().edit().putBoolean(str2, bool).apply();
    }

    public static Boolean getBoolean(Context context, String str, String str2, Boolean bool) {
        return get().getBoolean(str2, bool);
    }

    public static void setstring(Context context, String str, String str2, String str3) {
        get().edit().putString(str2, str3).apply();
    }

    public static String getstring(Context context, String str, String str2, String str3) {
        return get().getString(str2, str3);
    }

    public static String getStringPreference(String key_value) {
        return get().getString(key_value, "");
    }

    public static void setStringPreference(String key_value, String default_value) {
        get().edit().putString(key_value, default_value).apply();
    }

    public static String getStringData(Context context, String str) {
        return context.getSharedPreferences(PREF_APP, 0).getString(str, null);
    }

    public static void saveData(Context context, String str, String str2) {
        context.getSharedPreferences(PREF_APP, 0).edit().putString(str, str2).apply();
    }

    public static String getSavedString(Context context, String str, String str2) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, str2);
    }

    public static boolean getSavedBoolean(Context context, String str, boolean z) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(str, z);
    }

    public static int getIntString(Context context, String str, int i) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(str, i);
    }

    public static long getLongString(Context context, String str, long j) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(str, j);
    }

    public static void saveBooleanData(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static void saveIntData(Context context, String str, int i) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static void saveLongData(Context context, String str, long j) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putLong(str, j);
        edit.apply();
    }

    public static void saveStringData(Context context, String str, String str2) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(str, str2);
        edit.apply();
    }
}