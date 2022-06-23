package com.whatscan.toolkit.forwa.Api;

import com.whatscan.toolkit.forwa.GetSet.AdFreeData;
import com.whatscan.toolkit.forwa.GetSet.CaptionData;
import com.whatscan.toolkit.forwa.GetSet.CaptionSubData;
import com.whatscan.toolkit.forwa.GetSet.ChangePassword;
import com.whatscan.toolkit.forwa.GetSet.CheckCoinData;
import com.whatscan.toolkit.forwa.GetSet.CheckRedeemData;
import com.whatscan.toolkit.forwa.GetSet.CheckRedemption;
import com.whatscan.toolkit.forwa.GetSet.EmoticonData;
import com.whatscan.toolkit.forwa.GetSet.EmoticonSubData;
import com.whatscan.toolkit.forwa.GetSet.FeatursData;
import com.whatscan.toolkit.forwa.GetSet.LoginCoin;
import com.whatscan.toolkit.forwa.GetSet.MainStickerCategery;
import com.whatscan.toolkit.forwa.GetSet.NotificationModel;
import com.whatscan.toolkit.forwa.GetSet.PaymentBuyBack;
import com.whatscan.toolkit.forwa.GetSet.RegisterToken;
import com.whatscan.toolkit.forwa.GetSet.ReportRequest;
import com.whatscan.toolkit.forwa.GetSet.ResendOtp;
import com.whatscan.toolkit.forwa.GetSet.ResponseDecorativeMagic;
import com.whatscan.toolkit.forwa.GetSet.ResponseLogin;
import com.whatscan.toolkit.forwa.GetSet.ResponseRegister;
import com.whatscan.toolkit.forwa.GetSet.StickerData;
import com.whatscan.toolkit.forwa.GetSet.ToolUseCoin;
import com.whatscan.toolkit.forwa.GetSet.TransactionData;
import com.whatscan.toolkit.forwa.GetSet.VeryfyPlan;
import com.whatscan.toolkit.forwa.GetSet.verifyEmail;
import com.whatscan.toolkit.forwa.GetSet.verifyOtp;
import com.whatscan.toolkit.forwa.Util.AppConstant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @FormUrlEncoded()
    @POST()
     Call<ResponseRegister> getRegister(@Url String url, @Field(AppConstant.username) String username, @Field(AppConstant.email) String email,
                                       @Field(AppConstant.country_code) String country_code, @Field(AppConstant.mobile) String mobile,
                                       @Field(AppConstant.password) String password, @Field(AppConstant.c_password) String c_password,
                                       @Field(AppConstant.android_id) String android_id, @Field(AppConstant.fcm_id) String fcm_id,
                                       @Field(AppConstant.type) String type, @Field(AppConstant.google_key) String google_key,
                                       @Field(AppConstant.facebook_key) String facebook_key,
                                       @Field(AppConstant.refered_number) String refered_number);

    @FormUrlEncoded()
    @POST()
    Call<ResponseLogin> getLogin(@Url String url, @Field(AppConstant.username) String username, @Field(AppConstant.email) String email, @Field(AppConstant.country_code) String country_code,
                                 @Field(AppConstant.password) String password, @Field(AppConstant.android_id) String android_id, @Field(AppConstant.mobile) String mobile,
                                 @Field(AppConstant.fcm_id) String fcm_id, @Field(AppConstant.type) String type,
                                 @Field(AppConstant.google_key) String google_key, @Field(AppConstant.facebook_key) String facebook_key);

    @FormUrlEncoded()
    @POST()
    Call<ResponseDecorativeMagic> getDecorativeMagic(@Url String url, @Field(AppConstant.name) String name);

    @FormUrlEncoded()
    @POST()
    Call<CaptionData> getCaptionCategory(@Url String url, @Field(AppConstant.package_name) String package_name,
                                         @Field(AppConstant.version_code) int version_code);

    @FormUrlEncoded()
    @POST()
    Call<CaptionSubData> getCaptionSubCategory(@Url String url, @Field(AppConstant.caption_id) String caption_id);

    @FormUrlEncoded()
    @POST()
    Call<MainStickerCategery> getMainStickerCategory(@Url String url, @Field(AppConstant.package_name) String package_name,
                                                     @Field(AppConstant.version_code) int version_code);

    @FormUrlEncoded()
    @POST()
    Call<StickerData> getMainStickerSubCategory(@Url String url, @Field(AppConstant.package_name) String package_name,
                                                @Field(AppConstant.Title_id) String Title_id);

    @FormUrlEncoded()
    @POST()
    Call<EmoticonData> getEmoticonCategory(@Url String url, @Field(AppConstant.package_name) String package_name);

    @FormUrlEncoded()
    @POST()
    Call<EmoticonSubData> getEmoticonSubCategory(@Url String url, @Field(AppConstant.package_name) String package_name,
                                                 @Field(AppConstant.cat_id) String cat_id);

    @FormUrlEncoded()
    @POST()
    Call<VeryfyPlan> getVerifyPaymentPlanBilling(@Url String url, @Field(AppConstant.u_id) String u_id,
                                                 @Field(AppConstant.key_name) String key_name,
                                                 @Field(AppConstant.purchase_token) String purchase_token, @Field(AppConstant.start_date) String start_date);


    @FormUrlEncoded()
    @POST()
    Call<ReportRequest> getReportRequest(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.device_name) String device_name,
                                         @Field(AppConstant.android_version) String android_version,
                                         @Field(AppConstant.version_code) int version_code, @Field(AppConstant.message) String message,
                                         @Field(AppConstant.type) String type);

    @FormUrlEncoded()
    @POST()
    Call<NotificationModel> getNotification(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.status) String status);

    @FormUrlEncoded()
    @POST()
    Call<RegisterToken> getRegisterToken(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.token) String token);

    @FormUrlEncoded()
    @POST()
    Call<verifyEmail> getForgotEmail(@Url String url, @Field(AppConstant.email) String email);

    @FormUrlEncoded()
    @POST()
    Call<verifyOtp> getOtpVerifyForgot(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.otp) String otp);

    @FormUrlEncoded()
    @POST()
    Call<ChangePassword> getOtpVerifyEmail(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.otp) String otp);

    @FormUrlEncoded()
    @POST()
    Call<ChangePassword> getChangePass(@Url String url, @Field(AppConstant.u_id) String u_id,
                                       @Field(AppConstant.password) String password, @Field(AppConstant.c_password) String c_password);

    @FormUrlEncoded()
    @POST()
    Call<ResendOtp> getResendOtp(@Url String url, @Field(AppConstant.email) String email);

    @FormUrlEncoded()
    @POST()
    Call<CheckCoinData> geCheckCoinData(@Url String url, @Field(AppConstant.u_id) String u_id);

    @FormUrlEncoded()
    @POST()
    Call<LoginCoin> getWatchAdCoin(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.flag) String flag);

    @FormUrlEncoded()
    @POST()
    Call<ToolUseCoin> getToolsUseCoin(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.time) String time);

    @FormUrlEncoded()
    @POST()
    Call<LoginCoin> getInvitationCoin(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.refered_number) String refered_number);

    @FormUrlEncoded()
    @POST()
    Call<LoginCoin> getLoginCoin(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.status) String status);

    @FormUrlEncoded()
    @POST()
    Call<TransactionData> getTransactionData(@Url String url, @Field(AppConstant.u_id) String u_id);

    @FormUrlEncoded()
    @POST()
    Call<AdFreeData> getAdFree(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.days) String days, @Field(AppConstant.coin) String coin);

    @FormUrlEncoded()
    @POST()
    Call<FeatursData> getFeaturs(@Url String url, @Field(AppConstant.u_id) String u_id, @Field(AppConstant.days) String days, @Field(AppConstant.coin) String coin, @Field(AppConstant.f_name) String f_name);

    @FormUrlEncoded()
    @POST()
    Call<CheckRedeemData> getCheckRedeem(@Url String url, @Field(AppConstant.u_id) String u_id);

    @FormUrlEncoded()
    @POST()
    Call<CheckRedemption> getCheckRedemption(@Url String url, @Field(AppConstant.u_id) String u_id);
}