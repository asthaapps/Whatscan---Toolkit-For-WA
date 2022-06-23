package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

@Entity(tableName = DatabaseTableName.CONTACT_IN_GROUP_TABLE)
public final class ContactGroup {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String contactId;
    private Integer groupId;

    public ContactGroup() {
        this(null, null, null, 7);
    }

    public ContactGroup(@Nullable Integer num, @Nullable String str, @Nullable Integer num2) {
        this.id = num;
        this.contactId = str;
        this.groupId = num2;
    }

    public /* synthetic */ ContactGroup(Integer num, String str, Integer num2, int i) {
        this((i & 1) != 0 ? null : num, (i & 2) != 0 ? null : str, (i & 4) != 0 ? null : num2);
    }


    @NotNull
    public final ContactGroup copy(@Nullable Integer num, @Nullable String str, @Nullable Integer num2) {
        return new ContactGroup(num, str, num2);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ContactGroup)) {
            return false;
        }
        ContactGroup contactGroup = (ContactGroup) obj;
        return Intrinsics.areEqual(this.id, contactGroup.id) && Intrinsics.areEqual(this.contactId, contactGroup.contactId) && Intrinsics.areEqual(this.groupId, contactGroup.groupId);
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

    public final Integer getId() {
        return this.id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    public int hashCode() {
        Integer num = this.id;
        int i = 0;
        int hashCode = (num != null ? num.hashCode() : 0) * 31;
        String str = this.contactId;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        Integer num2 = this.groupId;
        if (num2 != null) {
            i = num2.hashCode();
        }
        return hashCode2 + i;
    }

    @NotNull
    public String toString() {
        return "ContactGroup(id=" + this.id + ", contactId=" + this.contactId + ", groupId=" + this.groupId + ")";
    }
}