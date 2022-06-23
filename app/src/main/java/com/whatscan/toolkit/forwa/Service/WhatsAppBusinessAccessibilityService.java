//package com.whatscan.toolkit.forwa.Service;
//
//import android.accessibilityservice.AccessibilityService;
//import android.view.accessibility.AccessibilityEvent;
//import android.widget.Toast;
//
//import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
//
//import java.util.List;
//
//public class WhatsAppBusinessAccessibilityService extends AccessibilityService {
//    private static final String packageName = "com.whatsapp.w4b";
//    private static int DELAY_TIME = 500;
//    private static boolean startTheService = false;
//
//    public static boolean isStartTheService() {
//        return startTheService;
//    }
//
//    public static void setStartTheService(boolean z) {
//        startTheService = z;
//    }
//
//    private void checkDialogConditionAndProcess(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
//        if (accessibilityNodeInfoCompat != null && accessibilityNodeInfoCompat.getWindowId() > 0) {
//            if (accessibilityNodeInfoCompat.getChildCount() == 2) {
//                if ((accessibilityNodeInfoCompat.getChild(0).getText() + "").contains("isn't on WhatsApp")) {
//                    try {
//                        performGlobalBackInWhatsTool();
//                        return;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                }
//            }
//            if (accessibilityNodeInfoCompat.getChildCount() == 2) {
//                if ((accessibilityNodeInfoCompat.getChild(0).getText() + "").contains("Couldn't look up")) {
//                    try {
//                        performGlobalBackInWhatsTool();
//                        return;
//                    } catch (Exception e2) {
//                        e2.printStackTrace();
//                        return;
//                    }
//                }
//            }
//            if (accessibilityNodeInfoCompat.getChildCount() == 2) {
//                if ((accessibilityNodeInfoCompat.getChild(0).getText() + "").contains("Share with")) {
//                    try {
//                        accessibilityNodeInfoCompat2.performAction(16);
//                        performGlobalBackInWhatsTool();
//                    } catch (Exception e3) {
//                        e3.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    private void performGlobalBackInWhatsTool() {
//        try {
//            Thread.sleep(DELAY_TIME);
//            if (!performGlobalAction(1)) {
//                retryGlobalBackPress();
//            }
//        } catch (InterruptedException unused) {
//            Toast.makeText(this, "Auto back press failed. Press Back button to continue", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void retryGlobalBackPress() {
//        if (DELAY_TIME < 2000) {
//            try {
//                int i = DELAY_TIME + 200;
//                DELAY_TIME = i;
//                Thread.sleep(i);
//                if (!performGlobalAction(1)) {
//                    retryGlobalBackPress();
//                }
//            } catch (InterruptedException unused) {
//                Toast.makeText(this, "Auto back press failed. Press Back button to continue", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void retrySendClick(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
//        try {
//            Thread.sleep(400);
//            if (!accessibilityNodeInfoCompat.performAction(16)) {
//                Toast.makeText(this, "Send Failed. Press Back button to continue", Toast.LENGTH_SHORT).show();
//            }
//        } catch (InterruptedException unused) {
//            Toast.makeText(this, "Send Failed. Press Back button to continue", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void sendAndGoBack(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
//        boolean performAction = accessibilityNodeInfoCompat.performAction(16);
//        if (!performAction) {
//            retrySendClick(accessibilityNodeInfoCompat);
//        }
//        performGlobalBackInWhatsTool();
//        performGlobalBackInWhatsTool();
//    }
//
//    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
//        AccessibilityNodeInfoCompat wrap;
//        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId;
//        if (startTheService && accessibilityEvent.getPackageName().equals(packageName)) {
//            try {
//                if (getRootInActiveWindow() != null && (wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow())) != null) {
//                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId2 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp.w4b:id/entry");
//                    if (findAccessibilityNodeInfosByViewId2 == null || findAccessibilityNodeInfosByViewId2.isEmpty()) {
//                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId3 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp.w4b:id/send");
//                        if (findAccessibilityNodeInfosByViewId3 == null || findAccessibilityNodeInfosByViewId3.isEmpty()) {
//                            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText = wrap.findAccessibilityNodeInfosByText("OK");
//                            if (findAccessibilityNodeInfosByText == null || findAccessibilityNodeInfosByText.isEmpty()) {
//                                List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText2 = wrap.findAccessibilityNodeInfosByText("My status");
//                                if (findAccessibilityNodeInfosByText2 != null && !findAccessibilityNodeInfosByText2.isEmpty()) {
//                                    performGlobalBackInWhatsTool();
//                                    return;
//                                }
//                                return;
//                            }
//                            checkDialogConditionAndProcess(wrap, findAccessibilityNodeInfosByText.get(0));
//                            return;
//                        }
//                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = findAccessibilityNodeInfosByViewId3.get(0);
//                        if (!accessibilityNodeInfoCompat.isVisibleToUser()) {
//                            Toast.makeText(this, "Please close the overlay or dialog to  continue", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        sendAndGoBack(accessibilityNodeInfoCompat);
//                        return;
//                    }
//                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = findAccessibilityNodeInfosByViewId2.get(0);
//                    if (accessibilityNodeInfoCompat2.getText() != null && accessibilityNodeInfoCompat2.getText().length() != 0 && (findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp.w4b:id/send")) != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
//                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = findAccessibilityNodeInfosByViewId.get(0);
//                        if (!accessibilityNodeInfoCompat3.isVisibleToUser()) {
//                            Toast.makeText(this, "Please close the overlay or dialog to  continue", Toast.LENGTH_SHORT).show();
//                        } else {
//                            sendAndGoBack(accessibilityNodeInfoCompat3);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void onInterrupt() {
//        Toast.makeText(this, "Please allow accessibility permission to WhatsApp Sender", Toast.LENGTH_SHORT).show();
//    }
//}