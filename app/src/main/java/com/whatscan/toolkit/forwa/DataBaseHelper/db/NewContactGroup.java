package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(childColumns = {"groupId"}, entity = GroupModel.class, onDelete = CASCADE, parentColumns = {"id"}), @ForeignKey(childColumns = {"groupPhoneContactId"}, entity = ContactModel.class, onDelete = 5, parentColumns = {"id"})}, tableName = DatabaseTableName.NEW_CONTACT_IN_GROUP_TABLE)
public final class NewContactGroup {
    @PrimaryKey(autoGenerate = true)
    private Integer contactGroupId;
    private String contactId;
    private Integer groupId;
    private Integer groupPhoneContactId;

    public NewContactGroup() {
    }

    public NewContactGroup(@Nullable Integer num, @Nullable String str, @Nullable Integer num2, @Nullable Integer num3) {
        this.contactGroupId = num;
        this.contactId = str;
        this.groupPhoneContactId = num2;
        this.groupId = num3;
    }

    public /* synthetic */ NewContactGroup(Integer num, String str, Integer num2, Integer num3, int i) {
        this((i & 1) != 0 ? null : num, (i & 2) != 0 ? null : str, (i & 4) != 0 ? null : num2, (i & 8) != 0 ? null : num3);
    }

    @NotNull
    public final NewContactGroup copy(@Nullable Integer num, @Nullable String str, @Nullable Integer num2, @Nullable Integer num3) {
        return new NewContactGroup(num, str, num2, num3);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NewContactGroup)) {
            return false;
        }
        NewContactGroup newContactGroup = (NewContactGroup) obj;
        return Intrinsics.areEqual(this.contactGroupId, newContactGroup.contactGroupId) && Intrinsics.areEqual(this.contactId, newContactGroup.contactId) && Intrinsics.areEqual(this.groupPhoneContactId, newContactGroup.groupPhoneContactId) && Intrinsics.areEqual(this.groupId, newContactGroup.groupId);
    }

    public Integer getContactGroupId() {
        return contactGroupId;
    }

    public void setContactGroupId(Integer contactGroupId) {
        this.contactGroupId = contactGroupId;
    }

    public final String getContactId() {
        return this.contactId;
    }

    public final void setContactId(@Nullable String str) {
        this.contactId = str;
    }

    public final Integer getGroupId() {
        return this.groupId;
    }

    public final void setGroupId(@Nullable Integer num) {
        this.groupId = num;
    }

    public final Integer getGroupPhoneContactId() {
        return this.groupPhoneContactId;
    }

    public final void setGroupPhoneContactId(@Nullable Integer num) {
        this.groupPhoneContactId = num;
    }

    public int hashCode() {
        Integer num = this.contactGroupId;
        int i = 0;
        int hashCode = (num != null ? num.hashCode() : 0) * 31;
        String str = this.contactId;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        Integer num2 = this.groupPhoneContactId;
        int hashCode3 = (hashCode2 + (num2 != null ? num2.hashCode() : 0)) * 31;
        Integer num3 = this.groupId;
        if (num3 != null) {
            i = num3.hashCode();
        }
        return hashCode3 + i;
    }

    @NotNull
    public String toString() {
        return "NewContactGroup(contactGroupId=" + this.contactGroupId + ", contactId=" + this.contactId + ", groupPhoneContactId=" + this.groupPhoneContactId + ", groupId=" + this.groupId + ")";
    }
}