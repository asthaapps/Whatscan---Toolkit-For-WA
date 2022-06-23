package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import kotlin.jvm.internal.Intrinsics;

@Entity(tableName = DatabaseTableName.GROUP_TABLE)
public final class GroupModel implements Serializable {
    private Integer count;
    private String description;
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String name;

    public GroupModel() {
    }

    public GroupModel(@Nullable Integer num, @Nullable Integer num2, @Nullable String str, @Nullable String str2) {
        this.id = num;
        this.count = num2;
        this.name = str;
        this.description = str2;
    }

    @NotNull
    public final GroupModel copy(@Nullable Integer num, @Nullable Integer num2, @Nullable String str, @Nullable String str2) {
        return new GroupModel(num, num2, str, str2);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GroupModel)) {
            return false;
        }
        GroupModel groupModel = (GroupModel) obj;
        return Intrinsics.areEqual(this.id, groupModel.id) && Intrinsics.areEqual(this.count, groupModel.count) && Intrinsics.areEqual(this.name, groupModel.name) && Intrinsics.areEqual(this.description, groupModel.description);
    }

    @Nullable
    public final Integer getCount() {
        return this.count;
    }

    public final void setCount(@Nullable Integer num) {
        this.count = num;
    }

    @Nullable
    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(@Nullable String str) {
        this.description = str;
    }

    @Nullable
    public final Integer getId() {
        return this.id;
    }

    public final void setId(@Nullable Integer num) {
        this.id = num;
    }

    @Nullable
    public final String getName() {
        return this.name;
    }

    public final void setName(@Nullable String str) {
        this.name = str;
    }

    public int hashCode() {
        Integer num = this.id;
        int i = 0;
        int hashCode = (num != null ? num.hashCode() : 0) * 31;
        Integer num2 = this.count;
        int hashCode2 = (hashCode + (num2 != null ? num2.hashCode() : 0)) * 31;
        String str = this.name;
        int hashCode3 = (hashCode2 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.description;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode3 + i;
    }

    @NotNull
    public String toString() {
        return "GroupModel(id=" + this.id + ", count=" + this.count + ", name=" + this.name + ", description=" + this.description + ")";
    }
}