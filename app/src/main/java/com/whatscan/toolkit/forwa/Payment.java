package com.whatscan.toolkit.forwa;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.whatscan.toolkit.forwa.Ads.Security;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Payment {

    public static void checkActiveSubs(Context context) {
        BillingClient billingClient = BillingClient.newBuilder(context)
                .setListener((billingResult, list) -> {

                })
                .enablePendingPurchases()
                .build();


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, (billingResult1, list) -> {
                        Purchase.PurchasesResult purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                        if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK && !Objects.requireNonNull(purchaseResult.getPurchasesList()).isEmpty()) {

                            ArrayList<String> classic_monthly = new ArrayList<>();
                            classic_monthly.add(Preference.getClassic_monthly_key());
                            ArrayList<String> classic_yearly = new ArrayList<>();
                            classic_yearly.add(Preference.getClassic_yearly_key());

                            ArrayList<String> premium_monthly = new ArrayList<>();
                            premium_monthly.add(Preference.getPremium_monthly_key());
                            ArrayList<String> premium_yearly = new ArrayList<>();
                            premium_yearly.add(Preference.getPremium_yearly_key());


                            ArrayList<String> master_monthly = new ArrayList<>();
                            master_monthly.add(Preference.getMaster_yearly_key());
                            ArrayList<String> master_yearly = new ArrayList<>();
                            master_yearly.add(Preference.getMaster_yearly_key());


                            if (purchaseResult.getPurchasesList().size() != 0) {
                                for (int i = 0; i < purchaseResult.getPurchasesList().size(); i++) {
                                    if (purchaseResult.getPurchasesList().get(i).getSkus().equals(master_monthly)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            Preference.setActive_MkeyM("");
                                        } else {
                                            Preference.setActive_MkeyM("true");
                                        }
                                    } else if (purchaseResult.getPurchasesList().get(i).getSkus().equals(master_yearly)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            Preference.setActive_MkeyY("");
                                        } else {
                                            Preference.setActive_MkeyY("true");
                                        }
                                    }  else if (purchaseResult.getPurchasesList().get(i).getSkus().equals(classic_monthly)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            Preference.setActive_CkeyM("");
                                        } else {
                                            Preference.setActive_CkeyM("true");
                                        }
                                    } else if (purchaseResult.getPurchasesList().get(i).getSkus().equals(classic_yearly)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            Preference.setActive_CkeyY("");
                                        } else {
                                            Preference.setActive_CkeyY("true");
                                        }
                                    }  else if (purchaseResult.getPurchasesList().get(i).getSkus().equals(premium_monthly)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            Preference.setActive_PkeyM("");
                                        } else {
                                            Preference.setActive_PkeyM("true");
                                        }
                                    } else if (purchaseResult.getPurchasesList().get(i).getSkus().equals(premium_yearly)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            Preference.setActive_PkeyY("");
                                        } else {
                                            Preference.setActive_PkeyY("true");
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    public static boolean verifyValidSignature(String signedData, String signature) {
        try {
            return Security.verifyPurchase(Preference.getBase_key(), signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

}
