package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import kotlin.jvm.internal.Intrinsics;

@Entity(tableName = DatabaseTableName.IMPORTED_FILES_TABLE)
public final class ImportedFile implements Serializable {

    private Long count;
    @PrimaryKey(autoGenerate = true)
    private Long importedFileId;
    private String name;
    private Long timestamp;

    public ImportedFile() {
        this(null, null, null, null, 15);
    }

    public ImportedFile(@Nullable Long l, @Nullable Long l2, @Nullable String str, @Nullable Long l3) {
        this.importedFileId = l;
        this.count = l2;
        this.name = str;
        this.timestamp = l3;
    }

    public /* synthetic */ ImportedFile(Long l, Long l2, String str, Long l3, int i) {
        this((i & 1) != 0 ? null : l, (i & 2) != 0 ? null : l2, (i & 4) != 0 ? null : str, (i & 8) != 0 ? null : l3);
    }

    public final ImportedFile copy(@Nullable Long l, @Nullable Long l2, @Nullable String str, @Nullable Long l3) {
        return new ImportedFile(l, l2, str, l3);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImportedFile)) {
            return false;
        }
        ImportedFile importedFile = (ImportedFile) obj;
        return Intrinsics.areEqual(this.importedFileId, importedFile.importedFileId) && Intrinsics.areEqual(this.count, importedFile.count) && Intrinsics.areEqual(this.name, importedFile.name) && Intrinsics.areEqual(this.timestamp, importedFile.timestamp);
    }

    public final Long getCount() {
        return this.count;
    }

    public final void setCount(@Nullable Long l) {
        this.count = l;
    }

    public final Long getImportedFileId() {
        return this.importedFileId;
    }

    public final void setImportedFileId(@Nullable Long l) {
        this.importedFileId = l;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(@Nullable String str) {
        this.name = str;
    }

    public final Long getTimestamp() {
        return this.timestamp;
    }

    public final void setTimestamp(@Nullable Long l) {
        this.timestamp = l;
    }

    public int hashCode() {
        Long l = this.importedFileId;
        int i = 0;
        int hashCode = (l != null ? l.hashCode() : 0) * 31;
        Long l2 = this.count;
        int hashCode2 = (hashCode + (l2 != null ? l2.hashCode() : 0)) * 31;
        String str = this.name;
        int hashCode3 = (hashCode2 + (str != null ? str.hashCode() : 0)) * 31;
        Long l3 = this.timestamp;
        if (l3 != null) {
            i = l3.hashCode();
        }
        return hashCode3 + i;
    }

    @NotNull
    public String toString() {
        return "ImportedFile(importedFileId=" + this.importedFileId + ", count=" + this.count + ", name=" + this.name + ", timestamp=" + this.timestamp + ")";
    }
}