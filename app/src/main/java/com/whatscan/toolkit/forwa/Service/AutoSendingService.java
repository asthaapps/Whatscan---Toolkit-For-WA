package com.whatscan.toolkit.forwa.Service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.whatscan.toolkit.forwa.Util.Utils;

public class AutoSendingService extends AccessibilityService {
    private static final String TAG = "AutoSendingService";
    private static int DELAY_TIME = 500;
    private static boolean startTheService = false;
    private Boolean isOnCamera;
    private Boolean isThisTrue1;
    private Boolean isThisTrue2;

    public AutoSendingService() {
        Boolean bool = Boolean.FALSE;
        isOnCamera = bool;
        isThisTrue1 = bool;
        isThisTrue2 = bool;
    }

    public static void startTheService(boolean z) {
        startTheService = z;
    }

    private void clickSendButton(AccessibilityNodeInfo accessibilityNodeInfo) {
        boolean bool = Boolean.TRUE;
        boolean bool2 = Boolean.FALSE;
        String sb = "clickSendButton: " + 0;
        Log.d(TAG, sb);
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            try {
                if (accessibilityNodeInfo.getChild(i) != null) {
                    String str = accessibilityNodeInfo.getChild(i).getContentDescription() + "";
                    if (str.equals("Send")) {
                        Log.d(TAG, "clickSendButton: Found SEND BUTTON" + i);
                        Thread.sleep(50);
                        accessibilityNodeInfo.getChild(i).performAction(16);
                        Thread.sleep(200);
                        if (isThisTrue1) {
                            isThisTrue1 = bool2;
                            Thread.sleep(1200);
                        }
                        Thread.sleep(1200);
                        try {
                            performGlobalBackInWhatsTool();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isThisTrue2 = bool;
                        return;
                    } else if (str.equals("New chat")) {
                        Log.d(TAG, "clickSendButton: Found New chat" + i);
                        Thread.sleep(50);
                        try {
                            performGlobalBackInWhatsTool();
                            return;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return;
                        }
                    } else if (isThisTrue2 && str.contains("Voice message,")) {
                        Log.d(TAG, "clickSendButton: Voice message" + i);
                        isThisTrue2 = bool2;
                        Thread.sleep(50);
                        try {
                            performGlobalBackInWhatsTool();
                            return;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            return;
                        }
                    } else if (!isOnCamera || !str.equals("Camera")) {
                        Log.d(TAG, "clickSendButton: Camera" + i);
                        isThisTrue1 = bool;
                    } else {
                        Thread.sleep(50);
                        isOnCamera = bool2;
                        try {
                            performGlobalBackInWhatsTool();
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    }
                }
            } catch (Exception unused) {
                return;
            }
        }
    }

    private void performGlobalBackInWhatsTool() {
        try {
            Thread.sleep(DELAY_TIME);
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
                Log.d(TAG, "onAccessibilityEvent: Global back Retry pressed " + DELAY_TIME);
                int i = DELAY_TIME + 500;
                DELAY_TIME = i;
                Thread.sleep(i);
                if (!performGlobalAction(1)) {
                    retryGlobalBackPress();
                }
            } catch (InterruptedException unused) {
                Utils.showToast(getApplicationContext(), "Auto back press failed. Press Back button to continue");
            }
        }
    }

    public void GetChild(AccessibilityNodeInfo accessibilityNodeInfo) {
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            if (accessibilityNodeInfo.getChild(i) != null) {
                String trim = (accessibilityNodeInfo.getChild(i).getText() + "").toLowerCase().trim();
                if (trim.equals("whatsapp")) {
                    accessibilityNodeInfo.performAction(16);
                    return;
                } else if (!trim.contains("whatsapp") || trim.length() <= 8) {
                    GetChild(accessibilityNodeInfo.getChild(i));
                } else {
                    accessibilityNodeInfo.performAction(16);
                    return;
                }
            }
        }
    }

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo rootInActiveWindow;
        String charSequence = accessibilityEvent.getPackageName().toString();
        Log.d(TAG, "Package Name: " + charSequence);
        if (!startTheService) {
            return;
        }
        if ((charSequence.equals("com.whatsapp") || charSequence.equals("com.whatsapp.w4b")) && (rootInActiveWindow = getRootInActiveWindow()) != null) {
            Log.d(TAG, "Window ID: " + rootInActiveWindow.getWindowId());
            if (rootInActiveWindow.getWindowId() > 0) {
                AccessibilityNodeInfo rootInActiveWindow2 = getRootInActiveWindow();
                for (int i = 0; i < rootInActiveWindow2.getChildCount(); i++) {
                    try {
                        if (rootInActiveWindow2.getChild(i) != null) {
                            Log.d(TAG, i + " : " + rootInActiveWindow2.getChild(i).getClassName());
                            Log.d(TAG, i + " : " + rootInActiveWindow2.getChild(i).getContentDescription());
                            Log.d(TAG, i + " : " + rootInActiveWindow2.getChild(i).getText());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rootInActiveWindow.getChildCount() == 2) {
                    if ((rootInActiveWindow.getChild(0).getText() + "").contains("isn't on WhatsApp")) {
                        try {
                            performGlobalBackInWhatsTool();
                            return;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return;
                        }
                    }
                }
                if (rootInActiveWindow.getChildCount() == 2) {
                    if ((rootInActiveWindow.getChild(0).getText() + "").contains("Couldn't look up")) {
                        try {
                            performGlobalBackInWhatsTool();
                            return;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            return;
                        }
                    }
                }
                if (rootInActiveWindow.getChildCount() == 4) {
                    if ((rootInActiveWindow.getChild(1).getText() + "").contains("Send to")) {
                        try {
                            performGlobalBackInWhatsTool();
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    }
                }
                clickSendButton(rootInActiveWindow);
            }
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onInterrupt() {
    }
}