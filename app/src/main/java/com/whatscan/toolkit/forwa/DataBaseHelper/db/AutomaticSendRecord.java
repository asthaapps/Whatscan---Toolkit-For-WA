package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

@Entity(tableName = DatabaseTableName.AUTOMATIC_SENT_HISTORY)
public final class AutomaticSendRecord {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String message;
    private String phoneNumber;
    private String sendMode;
    private String sendThrough;
    private long timestamp;
    private String userPlan;

    public AutomaticSendRecord() {
        this(null, null, null, null, null, null, 0, 127);
    }

    public AutomaticSendRecord(@Nullable Integer num, @Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5, long j) {
        this.id = num;
        this.phoneNumber = str;
        this.message = str2;
        this.sendThrough = str3;
        this.sendMode = str4;
        this.userPlan = str5;
        this.timestamp = j;
    }

    public /* synthetic */ AutomaticSendRecord(Integer num, String str, String str2, String str3, String str4, String str5, long j, int i) {
        this((i & 1) != 0 ? null : num, (i & 2) != 0 ? null : str, (i & 4) != 0 ? null : str2, (i & 8) != 0 ? null : str3, (i & 16) != 0 ? null : str4, (i & 32) == 0 ? str5 : null, (i & 64) != 0 ? 0 : j);
    }

    @NotNull
    public final AutomaticSendRecord copy(@Nullable Integer num, @Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5, long j) {
        return new AutomaticSendRecord(num, str, str2, str3, str4, str5, j);
    }

    public boolean equals(@Nullable Object obj) {
        if (this != obj) {
            if (obj instanceof AutomaticSendRecord) {
                AutomaticSendRecord automaticSendRecord = (AutomaticSendRecord) obj;
                if (Intrinsics.areEqual(this.id, automaticSendRecord.id) && Intrinsics.areEqual(this.phoneNumber, automaticSendRecord.phoneNumber) && Intrinsics.areEqual(this.message, automaticSendRecord.message) && Intrinsics.areEqual(this.sendThrough, automaticSendRecord.sendThrough) && Intrinsics.areEqual(this.sendMode, automaticSendRecord.sendMode) && Intrinsics.areEqual(this.userPlan, automaticSendRecord.userPlan)) {
                    if (this.timestamp == automaticSendRecord.timestamp) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public final Integer getId() {
        return this.id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    public final String getMessage() {
        return this.message;
    }

    public final void setMessage(@Nullable String str) {
        this.message = str;
    }

    public final String getPhoneNumber() {
        return this.phoneNumber;
    }

    public final void setPhoneNumber(@Nullable String str) {
        this.phoneNumber = str;
    }

    public final String getSendMode() {
        return this.sendMode;
    }

    public final void setSendMode(@Nullable String str) {
        this.sendMode = str;
    }

    public final String getSendThrough() {
        return this.sendThrough;
    }

    public final void setSendThrough(@Nullable String str) {
        this.sendThrough = str;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    public final void setTimestamp(long j) {
        this.timestamp = j;
    }

    public final String getUserPlan() {
        return this.userPlan;
    }

    public final void setUserPlan(@Nullable String str) {
        this.userPlan = str;
    }

    public int hashCode() {
        Integer num = this.id;
        int i = 0;
        int hashCode = (num != null ? num.hashCode() : 0) * 31;
        String str = this.phoneNumber;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.message;
        int hashCode3 = (hashCode2 + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.sendThrough;
        int hashCode4 = (hashCode3 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.sendMode;
        int hashCode5 = (hashCode4 + (str4 != null ? str4.hashCode() : 0)) * 31;
        String str5 = this.userPlan;
        if (str5 != null) {
            i = str5.hashCode();
        }
        long j = this.timestamp;
        return ((hashCode5 + i) * 31) + ((int) (j ^ (j >>> 32)));
    }

    @NotNull
    public String toString() {
        return "AutomaticSendRecord(id=" + this.id + ", phoneNumber=" + this.phoneNumber + ", message=" + this.message + ", sendThrough=" + this.sendThrough + ", sendMode=" + this.sendMode + ", userPlan=" + this.userPlan + ", timestamp=" + this.timestamp + ")";
    }
}
