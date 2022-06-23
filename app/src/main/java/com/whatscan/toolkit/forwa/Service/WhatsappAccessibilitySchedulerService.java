package com.whatscan.toolkit.forwa.Service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.whatscan.toolkit.forwa.AutoScheduler.ActivityAllEventScheduler;
import com.whatscan.toolkit.forwa.AutoScheduler.ActivityEventSchedule;

import java.util.List;

public class WhatsappAccessibilitySchedulerService extends AccessibilityService {
    public Bundle contactbundle = new Bundle();

    public void onInterrupt() {
    }

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (getRootInActiveWindow() != null) {
            AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
            if (accessibilityEvent.getSource() != null) {
                if (ActivityEventSchedule.packagename != null) {
                    if (ActivityEventSchedule.packagename.equals("com.whatsapp")) {
                        forselectwhatsappcontact();
                    }
                    if (ActivityEventSchedule.packagename.equals("com.whatsapp.w4b")) {
                        forselectbusinesswhatsappcontact();
                    }
                }
                if (AlarmReceiver.pkg != null) {
                    if (AlarmReceiver.pkg.equals("com.whatsapp")) {
                        serviceforbusinesswhatsapptEST();
                    }
                    if (AlarmReceiver.pkg.equals("com.whatsapp.w4b")) {
                        serviceforbusinesswhatsapp();
                    }
                }
            }
        }
    }

    private void forselectbusinesswhatsappcontact() {
        AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
        try {
            if (ActivityAllEventScheduler.contactname != null) {
                List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/conversation_contact_name");
                if (findAccessibilityNodeInfosByViewId.size() > 0) {
                    ActivityAllEventScheduler.contactname = findAccessibilityNodeInfosByViewId.get(0).getText().toString();
                    if (!ActivityAllEventScheduler.contactname.isEmpty()) {
                        System.out.println("Without Emoji   " + ActivityAllEventScheduler.contactname.replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]", ""));
                        ActivityAllEventScheduler.sendcontactname = ActivityAllEventScheduler.contactname;
                        System.out.println("Contact Name    " + ActivityAllEventScheduler.sendcontactname);
                        ActivityAllEventScheduler.contactname = null;
                        Thread.sleep(100);
                        performGlobalAction(1);
                        Thread.sleep(300);
                        performGlobalAction(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forselectwhatsappcontact() {
        try {
            AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
            if (ActivityAllEventScheduler.contactname != null) {
                List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/conversation_contact_name");
                if (findAccessibilityNodeInfosByViewId.size() > 0) {
                    ActivityAllEventScheduler.contactname = findAccessibilityNodeInfosByViewId.get(0).getText().toString();
                    if (!ActivityAllEventScheduler.contactname.isEmpty()) {
                        ActivityAllEventScheduler.sendcontactname = ActivityAllEventScheduler.contactname.trim();
                        ActivityAllEventScheduler.contactname = null;
                        System.out.println("Contact Name    " + ActivityAllEventScheduler.sendcontactname);
                        Thread.sleep(100);
                        performGlobalAction(1);
                        Thread.sleep(300);
                        performGlobalAction(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serviceforbusinesswhatsapp() {
        AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
        if (!(AlarmReceiver.contacts == null || ActivityAllEventScheduler.sendtoken == null)) {
            ActivityAllEventScheduler.sendtoken = null;
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/menuitem_search");
            if (!findAccessibilityNodeInfosByViewId.isEmpty()) {
                findAccessibilityNodeInfosByViewId.get(0).performAction(16);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId2 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/search_src_text");
                if (!findAccessibilityNodeInfosByViewId2.isEmpty()) {
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = findAccessibilityNodeInfosByViewId2.get(0);
                    int i = 2097152;
                    accessibilityNodeInfoCompat.performAction(2097152, this.contactbundle);
                    int i2 = 0;
                    while (true) {
                        if (i2 >= AlarmReceiver.contacts.size()) {
                            break;
                        }
                        this.contactbundle.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, AlarmReceiver.contacts.get(i2).replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]", ""));
                        accessibilityNodeInfoCompat.performAction(i, this.contactbundle);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId3 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/contactpicker_row_name");
                        if (findAccessibilityNodeInfosByViewId3.size() > 0) {
                            try {
                                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = findAccessibilityNodeInfosByViewId3.get(0);
                                AccessibilityNodeInfoCompat parent = accessibilityNodeInfoCompat2.getParent().getParent().getParent().getParent().getParent();
                                if (accessibilityNodeInfoCompat2.getText().toString().equals(AlarmReceiver.contacts.get(i2))) {
                                    parent.performAction(16);
                                    break;
                                } else if (accessibilityNodeInfoCompat2.getText().toString().contains(AlarmReceiver.contacts.get(i2))) {
                                    parent.performAction(16);
                                    break;
                                }
                            } catch (Exception e3) {
                                new Handler().post(e3::printStackTrace);
                            }
                        }
                        i2++;
                        i = 2097152;
                    }
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e5) {
                        e5.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId4 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/search_close_btn");
                    if (!findAccessibilityNodeInfosByViewId4.isEmpty()) {
                        findAccessibilityNodeInfosByViewId4.get(0).performAction(16);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e6) {
                            e6.printStackTrace();
                        }
                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId5 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/send");
                        if (findAccessibilityNodeInfosByViewId5 != null && !findAccessibilityNodeInfosByViewId5.isEmpty()) {
                            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = findAccessibilityNodeInfosByViewId5.get(0);
                            if (accessibilityNodeInfoCompat3.isVisibleToUser()) {
                                accessibilityNodeInfoCompat3.performAction(16);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e7) {
                                    e7.printStackTrace();
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        try {
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId6 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/entry");
            if (findAccessibilityNodeInfosByViewId6.size() > 0) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat4 = findAccessibilityNodeInfosByViewId6.get(0);
                if (accessibilityNodeInfoCompat4 != null && accessibilityNodeInfoCompat4.getText().toString().length() != 0 && accessibilityNodeInfoCompat4.getText().toString().endsWith("   ")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e8) {
                        e8.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId7 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/send");
                    if (findAccessibilityNodeInfosByViewId7 != null && !findAccessibilityNodeInfosByViewId7.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat5 = findAccessibilityNodeInfosByViewId7.get(0);
                        if (accessibilityNodeInfoCompat5.isVisibleToUser()) {
                            accessibilityNodeInfoCompat5.performAction(16);
                            try {
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                            } catch (InterruptedException e9) {
                                e9.printStackTrace();
                            }
                            performGlobalAction(1);
                            AlarmReceiver.msg = null;
                            AlarmReceiver.contacts.clear();
                            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId8 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/send");
                            if (findAccessibilityNodeInfosByViewId8 != null && !findAccessibilityNodeInfosByViewId8.isEmpty()) {
                                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat6 = findAccessibilityNodeInfosByViewId8.get(0);
                                if (accessibilityNodeInfoCompat6.isVisibleToUser()) {
                                    accessibilityNodeInfoCompat6.performAction(16);
                                    try {
                                        Thread.sleep(1000);
                                        performGlobalAction(1);
                                        Thread.sleep(1000);
                                        performGlobalAction(1);
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e10) {
                                        e10.printStackTrace();
                                    }
                                    performGlobalAction(1);
                                    AlarmReceiver.msg = null;
                                    AlarmReceiver.contacts.clear();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId9 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/mentionable_entry");
            if (findAccessibilityNodeInfosByViewId9.size() > 0) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat7 = findAccessibilityNodeInfosByViewId9.get(0);
                if (accessibilityNodeInfoCompat7 != null && accessibilityNodeInfoCompat7.getText().toString().length() != 0 && accessibilityNodeInfoCompat7.getText().toString().endsWith("   ")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e11) {
                        e11.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId10 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/send");
                    if (findAccessibilityNodeInfosByViewId10 != null && !findAccessibilityNodeInfosByViewId10.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat8 = findAccessibilityNodeInfosByViewId10.get(0);
                        if (accessibilityNodeInfoCompat8.isVisibleToUser()) {
                            accessibilityNodeInfoCompat8.performAction(16);
                            try {
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                            } catch (InterruptedException e12) {
                                e12.printStackTrace();
                            }
                            performGlobalAction(1);
                            AlarmReceiver.msg = null;
                            AlarmReceiver.contacts.clear();
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId11 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp.w4b") + ":id/send");
            if (findAccessibilityNodeInfosByViewId11 != null && !findAccessibilityNodeInfosByViewId11.isEmpty()) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat9 = findAccessibilityNodeInfosByViewId11.get(0);
                if (accessibilityNodeInfoCompat9.isVisibleToUser()) {
                    accessibilityNodeInfoCompat9.performAction(16);
                    try {
                        Thread.sleep(1000);
                        performGlobalAction(1);
                        Thread.sleep(1000);
                        performGlobalAction(1);
                        Thread.sleep(1000);
                    } catch (InterruptedException e13) {
                        e13.printStackTrace();
                    }
                    performGlobalAction(1);
                    AlarmReceiver.msg = null;
                    AlarmReceiver.contacts.clear();
                }
            }
        } catch (Exception e14) {
            e14.printStackTrace();
        }
    }

    private void serviceforbusinesswhatsapptEST() {
        AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
        if (!(AlarmReceiver.contacts == null || ActivityAllEventScheduler.sendtoken == null)) {
            ActivityAllEventScheduler.sendtoken = null;
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/menuitem_search");
            if (!findAccessibilityNodeInfosByViewId.isEmpty() && findAccessibilityNodeInfosByViewId != null) {
                findAccessibilityNodeInfosByViewId.get(0).performAction(16);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId2 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/search_src_text");
                if (!findAccessibilityNodeInfosByViewId2.isEmpty() && findAccessibilityNodeInfosByViewId2 != null) {
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = findAccessibilityNodeInfosByViewId2.get(0);
                    int i = 2097152;
                    accessibilityNodeInfoCompat.performAction(2097152, this.contactbundle);
                    int i2 = 0;
                    while (true) {
                        if (i2 >= AlarmReceiver.contacts.size()) {
                            break;
                        }
                        this.contactbundle.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, AlarmReceiver.contacts.get(i2).replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]", ""));
                        accessibilityNodeInfoCompat.performAction(i, this.contactbundle);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId3 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/contactpicker_row_name");
                        if (findAccessibilityNodeInfosByViewId3.size() > 0) {
                            try {
                                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = findAccessibilityNodeInfosByViewId3.get(0);
                                AccessibilityNodeInfoCompat parent = accessibilityNodeInfoCompat2.getParent().getParent().getParent().getParent().getParent();
                                if (accessibilityNodeInfoCompat2.getText().toString().equals(AlarmReceiver.contacts.get(i2))) {
                                    parent.performAction(16);
                                    break;
                                } else if (accessibilityNodeInfoCompat2.getText().toString().contains(AlarmReceiver.contacts.get(i2))) {
                                    parent.performAction(16);
                                    break;
                                }
                            } catch (RuntimeException e3) {
                                new Handler().post(new Runnable() {
                                    public void run() {
                                        e3.printStackTrace();
                                    }
                                });
                            } catch (Exception e4) {
                                new Handler().post(new Runnable() {
                                    public void run() {
                                        e4.printStackTrace();
                                    }
                                });
                            }
                        }
                        i2++;
                        i = 2097152;
                    }
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e5) {
                        e5.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId4 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/search_close_btn");
                    if (!findAccessibilityNodeInfosByViewId4.isEmpty() && findAccessibilityNodeInfosByViewId4 != null) {
                        findAccessibilityNodeInfosByViewId4.get(0).performAction(16);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e6) {
                            e6.printStackTrace();
                        }
                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId5 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/send");
                        if (findAccessibilityNodeInfosByViewId5 != null && !findAccessibilityNodeInfosByViewId5.isEmpty()) {
                            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = findAccessibilityNodeInfosByViewId5.get(0);
                            if (accessibilityNodeInfoCompat3.isVisibleToUser()) {
                                accessibilityNodeInfoCompat3.performAction(16);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e7) {
                                    e7.printStackTrace();
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        try {
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId6 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/entry");
            if (findAccessibilityNodeInfosByViewId6.size() > 0) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat4 = findAccessibilityNodeInfosByViewId6.get(0);
                if (accessibilityNodeInfoCompat4 != null && accessibilityNodeInfoCompat4.getText().toString().length() != 0 && accessibilityNodeInfoCompat4.getText().toString().endsWith("   ")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e8) {
                        e8.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId7 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/send");
                    if (findAccessibilityNodeInfosByViewId7 == null) {
                        return;
                    }
                    if (!findAccessibilityNodeInfosByViewId7.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat5 = findAccessibilityNodeInfosByViewId7.get(0);
                        if (accessibilityNodeInfoCompat5.isVisibleToUser()) {
                            accessibilityNodeInfoCompat5.performAction(16);
                            try {
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                            } catch (InterruptedException e9) {
                                e9.printStackTrace();
                            }
                            performGlobalAction(1);
                            AlarmReceiver.msg = null;
                            AlarmReceiver.contacts.clear();
                            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId8 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/send");
                            if (findAccessibilityNodeInfosByViewId8 == null) {
                                return;
                            }
                            if (!findAccessibilityNodeInfosByViewId8.isEmpty()) {
                                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat6 = findAccessibilityNodeInfosByViewId8.get(0);
                                if (accessibilityNodeInfoCompat6.isVisibleToUser()) {
                                    accessibilityNodeInfoCompat6.performAction(16);
                                    try {
                                        Thread.sleep(1000);
                                        performGlobalAction(1);
                                        Thread.sleep(1000);
                                        performGlobalAction(1);
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e10) {
                                        e10.printStackTrace();
                                    }
                                    performGlobalAction(1);
                                    AlarmReceiver.msg = null;
                                    AlarmReceiver.contacts.clear();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId9 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/mentionable_entry");
            if (findAccessibilityNodeInfosByViewId9.size() > 0) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat7 = findAccessibilityNodeInfosByViewId9.get(0);
                if (accessibilityNodeInfoCompat7 != null && accessibilityNodeInfoCompat7.getText().toString().length() != 0 && accessibilityNodeInfoCompat7.getText().toString().endsWith("   ")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e11) {
                        e11.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId10 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/send");
                    if (findAccessibilityNodeInfosByViewId10 == null) {
                        return;
                    }
                    if (!findAccessibilityNodeInfosByViewId10.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat8 = findAccessibilityNodeInfosByViewId10.get(0);
                        if (accessibilityNodeInfoCompat8.isVisibleToUser()) {
                            accessibilityNodeInfoCompat8.performAction(16);
                            try {
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                            } catch (InterruptedException e12) {
                                e12.printStackTrace();
                            }
                            performGlobalAction(1);
                            AlarmReceiver.msg = null;
                            AlarmReceiver.contacts.clear();
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId11 = wrap.findAccessibilityNodeInfosByViewId(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("whatsz", "com.whatsapp") + ":id/send");
            if (findAccessibilityNodeInfosByViewId11 == null) {
                return;
            }
            if (!findAccessibilityNodeInfosByViewId11.isEmpty()) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat9 = findAccessibilityNodeInfosByViewId11.get(0);
                if (accessibilityNodeInfoCompat9.isVisibleToUser()) {
                    accessibilityNodeInfoCompat9.performAction(16);
                    try {
                        Thread.sleep(1000);
                        performGlobalAction(1);
                        Thread.sleep(1000);
                        performGlobalAction(1);
                        Thread.sleep(1000);
                    } catch (InterruptedException e13) {
                        e13.printStackTrace();
                    }
                    performGlobalAction(1);
                    AlarmReceiver.msg = null;
                    AlarmReceiver.contacts.clear();
                }
            }
        } catch (Exception e14) {
            e14.printStackTrace();
        }
    }

    private void serviceforwhatsapp() {
        AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
        if (!(AlarmReceiver.contacts == null || ActivityAllEventScheduler.sendtoken == null)) {
            ActivityAllEventScheduler.sendtoken = null;
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId3 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/menuitem_search");
            if (!findAccessibilityNodeInfosByViewId3.isEmpty()) {
                findAccessibilityNodeInfosByViewId3.get(0).performAction(16);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId4 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/search_src_text");
                if (!findAccessibilityNodeInfosByViewId4.isEmpty()) {
                    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = findAccessibilityNodeInfosByViewId4.get(0);
                    int i = 2097152;
                    accessibilityNodeInfoCompat.performAction(2097152, this.contactbundle);
                    int i2 = 0;
                    while (true) {
                        if (i2 >= AlarmReceiver.contacts.size()) {
                            break;
                        }
                        this.contactbundle.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, AlarmReceiver.contacts.get(i).replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]", ""));
                        accessibilityNodeInfoCompat.performAction(i, this.contactbundle);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }

                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId5 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/contactpicker_row_name");
                        if (findAccessibilityNodeInfosByViewId5.size() > 0) {
                            try {
                                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = findAccessibilityNodeInfosByViewId5.get(0);
                                AccessibilityNodeInfoCompat parent = accessibilityNodeInfoCompat2.getParent().getParent().getParent().getParent().getParent();
                                if (accessibilityNodeInfoCompat2.getText().toString().equals(AlarmReceiver.contacts.get(i2))) {
                                    parent.performAction(16);
                                    break;
                                } else if (accessibilityNodeInfoCompat2.getText().toString().contains(AlarmReceiver.contacts.get(i2))) {
                                    parent.performAction(16);
                                    break;
                                }
                            } catch (Exception e3) {
                                new Handler().post(e3::printStackTrace);
                            }
                        }
                        i2++;
                        i = 2097152;
                    }

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e5) {
                        e5.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId6 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/search_close_btn");
                    if (!findAccessibilityNodeInfosByViewId6.isEmpty()) {
                        findAccessibilityNodeInfosByViewId6.get(0).performAction(16);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e6) {
                            e6.printStackTrace();
                        }
                        List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId5 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                        if (findAccessibilityNodeInfosByViewId5 != null && !findAccessibilityNodeInfosByViewId5.isEmpty()) {
                            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = findAccessibilityNodeInfosByViewId5.get(0);
                            if (accessibilityNodeInfoCompat3.isVisibleToUser()) {
                                accessibilityNodeInfoCompat3.performAction(16);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e7) {
                                    e7.printStackTrace();
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        try {
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = findAccessibilityNodeInfosByViewId.get(0);
                if (accessibilityNodeInfoCompat3 != null && accessibilityNodeInfoCompat3.getText().toString().length() != 0 && accessibilityNodeInfoCompat3.getText().toString().endsWith("   ")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e5) {
                        e5.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId6 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                    if (findAccessibilityNodeInfosByViewId6 != null && !findAccessibilityNodeInfosByViewId6.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat4 = findAccessibilityNodeInfosByViewId6.get(0);
                        if (accessibilityNodeInfoCompat4.isVisibleToUser()) {
                            accessibilityNodeInfoCompat4.performAction(16);
                            try {
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                            } catch (InterruptedException e6) {
                                e6.printStackTrace();
                            }
                            performGlobalAction(1);
                            AlarmReceiver.msg = null;
                            AlarmReceiver.contacts.clear();
                            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId7 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                            if (findAccessibilityNodeInfosByViewId7 != null && !findAccessibilityNodeInfosByViewId7.isEmpty()) {
                                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat5 = findAccessibilityNodeInfosByViewId7.get(0);
                                if (accessibilityNodeInfoCompat5.isVisibleToUser()) {
                                    accessibilityNodeInfoCompat5.performAction(16);
                                    try {
                                        Thread.sleep(1000);
                                        performGlobalAction(1);
                                        Thread.sleep(1000);
                                        performGlobalAction(1);
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e7) {
                                        e7.printStackTrace();
                                    }
                                    performGlobalAction(1);
                                    AlarmReceiver.msg = null;
                                    AlarmReceiver.contacts.clear();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId8 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/mentionable_entry");
            if (findAccessibilityNodeInfosByViewId8.size() > 0) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat6 = findAccessibilityNodeInfosByViewId8.get(0);
                if (accessibilityNodeInfoCompat6 != null && accessibilityNodeInfoCompat6.getText().toString().length() != 0 && accessibilityNodeInfoCompat6.getText().toString().endsWith("   ")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e8) {
                        e8.printStackTrace();
                    }
                    List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId9 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                    if (findAccessibilityNodeInfosByViewId9 != null && !findAccessibilityNodeInfosByViewId9.isEmpty()) {
                        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat7 = findAccessibilityNodeInfosByViewId9.get(0);
                        if (accessibilityNodeInfoCompat7.isVisibleToUser()) {
                            accessibilityNodeInfoCompat7.performAction(16);
                            try {
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                                performGlobalAction(1);
                                Thread.sleep(1000);
                            } catch (InterruptedException e9) {
                                e9.printStackTrace();
                            }
                            performGlobalAction(1);
                            AlarmReceiver.msg = null;
                            AlarmReceiver.contacts.clear();
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId10 = wrap.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
            if (findAccessibilityNodeInfosByViewId10 != null && !findAccessibilityNodeInfosByViewId10.isEmpty()) {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat8 = findAccessibilityNodeInfosByViewId10.get(0);
                if (accessibilityNodeInfoCompat8.isVisibleToUser()) {
                    accessibilityNodeInfoCompat8.performAction(16);
                    try {
                        Thread.sleep(1000);
                        performGlobalAction(1);
                        Thread.sleep(1000);
                        performGlobalAction(1);
                        Thread.sleep(1000);
                    } catch (InterruptedException e10) {
                        e10.printStackTrace();
                    }
                    performGlobalAction(1);
                    AlarmReceiver.msg = null;
                    AlarmReceiver.contacts.clear();
                }
            }
        } catch (Exception e11) {
            e11.printStackTrace();
        }
    }

    public void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = 2080;
        accessibilityServiceInfo.flags = 18;
        accessibilityServiceInfo.feedbackType = 20;
        accessibilityServiceInfo.notificationTimeout = 1000;
        accessibilityServiceInfo.packageNames = new String[]{"com.whatsapp", "com.whatsapp.w4b"};
        accessibilityServiceInfo.getCanRetrieveWindowContent();
        setServiceInfo(accessibilityServiceInfo);
    }
}