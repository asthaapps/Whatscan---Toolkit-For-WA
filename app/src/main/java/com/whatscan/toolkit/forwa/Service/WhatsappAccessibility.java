package com.whatscan.toolkit.forwa.Service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.whatscan.toolkit.forwa.DataBaseHelper.DatabaseHandler;
import com.whatscan.toolkit.forwa.GetSet.ChatLockModel;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.whatscan.toolkit.forwa.WChatLocker.ActivityChatLock;
import com.whatscan.toolkit.forwa.WChatLocker.ActivityChatLockMain;

import java.util.ArrayList;
import java.util.List;

public class WhatsappAccessibility extends AccessibilityService {
    public static final String packageName = "com.whatsapp";
    public static final String packageNameB = "com.whatsapp.w4b";
    public static int DELAY_TIME = 500;
    public static boolean startTheService = false;
    public ArrayList<ChatLockModel> ITEMS = new ArrayList<>();
    public String currentAccessibilityPackage;
    public boolean isWhatsappUnLockedLocked = false;
    public Context context;
    public DatabaseHandler db;

    public static boolean isStartTheService() {
        return startTheService;
    }

    public static void setStartTheService(boolean z) {
        startTheService = z;
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        db = new DatabaseHandler(getApplicationContext());
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            System.out.println("notification: " + accessibilityEvent.getText());
        }

        AccessibilityNodeInfoCompat wrap;
        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId;
        if (startTheService && accessibilityEvent.getPackageName().equals(packageName)) {
            try {
                if (getRootInActiveWindow() != null && (wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow())) != null) {
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId2 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
                    if (findAccessibilityNodeInfosByViewId2 == null || findAccessibilityNodeInfosByViewId2.isEmpty()) {
                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId3 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                        if (findAccessibilityNodeInfosByViewId3 == null || findAccessibilityNodeInfosByViewId3.isEmpty()) {
                            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText = wrap.findAccessibilityNodeInfosByText("OK");
                            if (findAccessibilityNodeInfosByText == null || findAccessibilityNodeInfosByText.isEmpty()) {
                                return;
                            }
                            checkDialogConditionAndProcess(wrap, findAccessibilityNodeInfosByText.get(0));
                            return;
                        }
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = findAccessibilityNodeInfosByViewId3.get(0);
                        if (!accessibilityNodeInfoCompat.isVisibleToUser()) {
                            Utils.showToast(getApplicationContext(), "Please close the overlay or dialog to  continue");
                            return;
                        }
                        sendAndGoBack(accessibilityNodeInfoCompat);
                        return;
                    }
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = findAccessibilityNodeInfosByViewId2.get(0);
                    if (accessibilityNodeInfoCompat2.getText() != null && accessibilityNodeInfoCompat2.getText().length() != 0 && (findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")) != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = findAccessibilityNodeInfosByViewId.get(0);
                        if (!accessibilityNodeInfoCompat3.isVisibleToUser()) {
                            Utils.showToast(getApplicationContext(), "Please close the overlay or dialog to  continue");
                        } else {
                            sendAndGoBack(accessibilityNodeInfoCompat3);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (startTheService && accessibilityEvent.getPackageName().equals(packageNameB)) {
            try {
                if (getRootInActiveWindow() != null && (wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow())) != null) {
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId2 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp.w4b:id/entry");
                    if (findAccessibilityNodeInfosByViewId2 == null || findAccessibilityNodeInfosByViewId2.isEmpty()) {
                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId3 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp.w4b:id/send");
                        if (findAccessibilityNodeInfosByViewId3 == null || findAccessibilityNodeInfosByViewId3.isEmpty()) {
                            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText = wrap.findAccessibilityNodeInfosByText("OK");
                            if (findAccessibilityNodeInfosByText == null || findAccessibilityNodeInfosByText.isEmpty()) {
                                List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText2 = wrap.findAccessibilityNodeInfosByText("My status");
                                if (findAccessibilityNodeInfosByText2 != null && !findAccessibilityNodeInfosByText2.isEmpty()) {
                                    performGlobalBackInWhatsTool();
                                    return;
                                }
                                return;
                            }
                            checkDialogConditionAndProcess(wrap, findAccessibilityNodeInfosByText.get(0));
                            return;
                        }
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = findAccessibilityNodeInfosByViewId3.get(0);
                        if (!accessibilityNodeInfoCompat.isVisibleToUser()) {
                            Toast.makeText(this, "Please close the overlay or dialog to  continue", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sendAndGoBack(accessibilityNodeInfoCompat);
                        return;
                    }
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = findAccessibilityNodeInfosByViewId2.get(0);
                    if (accessibilityNodeInfoCompat2.getText() != null && accessibilityNodeInfoCompat2.getText().length() != 0 && (findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp.w4b:id/send")) != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = findAccessibilityNodeInfosByViewId.get(0);
                        if (!accessibilityNodeInfoCompat3.isVisibleToUser()) {
                            Toast.makeText(this, "Please close the overlay or dialog to  continue", Toast.LENGTH_SHORT).show();
                        } else {
                            sendAndGoBack(accessibilityNodeInfoCompat3);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Constant.WhatsAppChatLock) {
            if (accessibilityEvent.getPackageName() != null) {
                try {
                    currentAccessibilityPackage = accessibilityEvent.getPackageName().toString();
                    if (accessibilityEvent.getPackageName().toString().equalsIgnoreCase("com.whatsapp") && accessibilityEvent.getPackageName().toString().equalsIgnoreCase("com.whatsapp") && accessibilityEvent.getClassName().toString().equalsIgnoreCase("android.widget.RelativeLayout")) {
                        String obj = accessibilityEvent.getText().toString();
                        if (obj.contains("[")) {
                            obj = obj.replace("[", "");
                        }
                        if (obj.contains(",")) {
                            String[] split = obj.split(",");
                            StringBuilder sb = new StringBuilder();
                            sb.append(split[0]);
                            ITEMS.clear();
                            ITEMS.addAll(db.getAllChatLock());
                            if (ActivityChatLockMain.isFromActivity) {
                                if (!contains(ITEMS, sb.toString())) {
                                    ChatLockModel homeModel = new ChatLockModel();
                                    homeModel.setUsername(sb.toString());
                                    homeModel.setIsToCheckLock(0);
                                    homeModel.setIsLock(1);
                                    db.addChatLockInfo(homeModel);
                                    Toast.makeText(context, "Chat/Group added successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Already exist", Toast.LENGTH_SHORT).show();
                                }
                                startActivity(new Intent(context, ActivityChatLockMain.class).addFlags(268435456).addFlags(335577088));
                                ActivityChatLockMain.isFromActivity = false;
                            } else if (contains(ITEMS, sb.toString()) && isLocked(ITEMS, sb.toString()) == 1) {
                                startActivity(new Intent(context, ActivityChatLock.class).putExtra("fromService", "Chat").addFlags(335577088));
                            }
                        }
                    }
                    if (accessibilityEvent.getEventType() != 32 || !accessibilityEvent.getPackageName().toString().equalsIgnoreCase("com.whatsapp")) {
                        if (!accessibilityEvent.getPackageName().toString().equalsIgnoreCase(context.getPackageName()) && !accessibilityEvent.getPackageName().toString().equalsIgnoreCase("com.whatsapp")) {
                            isWhatsappUnLockedLocked = false;
                        }
                    } else if (defaultSharedPreferences.getBoolean("app_lock", false) && !isWhatsappUnLockedLocked) {
                        isWhatsappUnLockedLocked = true;
                        startActivity(new Intent(context, ActivityChatLock.class).putExtra("fromService", "Whatsapp").addFlags(335577088));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkDialogConditionAndProcess(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
        if (accessibilityNodeInfoCompat != null && accessibilityNodeInfoCompat.getWindowId() > 0) {
            if (accessibilityNodeInfoCompat.getChildCount() == 2) {
                if ((((Object) accessibilityNodeInfoCompat.getChild(0).getText()) + "").contains("isn't on WhatsApp")) {
                    try {
                        performGlobalBackInWhatsTool();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
            if (accessibilityNodeInfoCompat.getChildCount() == 2) {
                if ((((Object) accessibilityNodeInfoCompat.getChild(0).getText()) + "").contains("Couldn't look up")) {
                    try {
                        performGlobalBackInWhatsTool();
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
            }
            if (accessibilityNodeInfoCompat.getChildCount() == 2) {
                if ((((Object) accessibilityNodeInfoCompat.getChild(0).getText()) + "").contains("Share with")) {
                    try {
                        accessibilityNodeInfoCompat2.performAction(16);
                        performGlobalBackInWhatsTool();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
    }

    private void performGlobalBackInWhatsTool() {
        try {
            Thread.sleep((long) DELAY_TIME);
            if (!performGlobalAction(1)) {
                retryGlobalBackPress();
            }
        } catch (InterruptedException unused) {
            Utils.showToast(getApplicationContext(), "Auto back press failed. Press Back button to continue");
        }
    }

    private void retryGlobalBackPress() {
        if (DELAY_TIME < 2000) {
            try {
                int i = DELAY_TIME + 200;
                DELAY_TIME = i;
                Thread.sleep((long) i);
                if (!performGlobalAction(1)) {
                    retryGlobalBackPress();
                }
            } catch (InterruptedException unused) {
                Utils.showToast(getApplicationContext(), "Auto back press failed. Press Back button to continue");
            }
        }
    }

    private void retrySendClick(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        try {
            Thread.sleep(400);
            if (!accessibilityNodeInfoCompat.performAction(16)) {
                Utils.showToast(getApplicationContext(), "Send Failed. Press Back button to continue");
            }
        } catch (InterruptedException unused) {
            Utils.showToast(getApplicationContext(), "Send Failed. Press Back button to continue");
        }
    }

    private void sendAndGoBack(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        boolean performAction = accessibilityNodeInfoCompat.performAction(16);
        if (!performAction) {
            retrySendClick(accessibilityNodeInfoCompat);
        }
        performGlobalBackInWhatsTool();
        performGlobalBackInWhatsTool();
    }

    public boolean contains(ArrayList<ChatLockModel> arrayList, String str) {
        for (ChatLockModel chatLockModel : arrayList) {
            if (chatLockModel.getUsername().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public int isLocked(ArrayList<ChatLockModel> arrayList, String str) {
        for (ChatLockModel next : arrayList) {
            if (next.getUsername().equals(str)) {
                return next.getIsLock();
            }
        }
        return 0;
    }

    @Override
    public void onInterrupt() {
        Utils.showToast(this, "Please allow accessibility permission to WhatsApp Sender");
    }
}