package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

@Entity(foreignKeys = {@ForeignKey(childColumns = {"importedFileContactId"}, entity = ImportedFile.class, onDelete = 5, parentColumns = {"importedFileId"}), @ForeignKey(childColumns = {"phoneContactImportId"}, entity = ContactModel.class, onDelete = 5, parentColumns = {"id"})}, tableName = DatabaseTableName.IMPORTED_CONTACTS_TABLE)
public class ImportedContact {
    @PrimaryKey(autoGenerate = true)
    private long importedContactId;
    private Long importedFileContactId;
    private Long phoneContactImportId;

    public ImportedContact() {
        this(0, null, null, 7);
    }

    public ImportedContact(long j, @Nullable Long l, @Nullable Long l2) {
        this.importedContactId = j;
        this.importedFileContactId = l;
        this.phoneContactImportId = l2;
    }

    public /* synthetic */ ImportedContact(long j, Long l, Long l2, int i) {
        this((i & 1) != 0 ? 0 : j, (i & 2) != 0 ? null : l, (i & 4) != 0 ? null : l2);
    }

    @NotNull
    public final ImportedContact copy(long j, @Nullable Long l, @Nullable Long l2) {
        return new ImportedContact(j, l, l2);
    }

    public boolean equals(@Nullable Object obj) {
        if (this != obj) {
            if (obj instanceof ImportedContact) {
                ImportedContact importedContact = (ImportedContact) obj;
                if (!(this.importedContactId == importedContact.importedContactId) || !Intrinsics.areEqual(this.importedFileContactId, importedContact.importedFileContactId) || !Intrinsics.areEqual(this.phoneContactImportId, importedContact.phoneContactImportId)) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    public final long getImportedContactId() {
        return this.importedContactId;
    }

    public final void setImportedContactId(long j) {
        this.importedContactId = j;
    }

    public final Long getImportedFileContactId() {
        return this.importedFileContactId;
    }

    public final void setImportedFileContactId(@Nullable Long l) {
        this.importedFileContactId = l;
    }

    public final Long getPhoneContactImportId() {
        return this.phoneContactImportId;
    }

    public final void setPhoneContactImportId(@Nullable Long l) {
        this.phoneContactImportId = l;
    }

    public int hashCode() {
        long j = this.importedContactId;
        int i = ((int) (j ^ (j >>> 32))) * 31;
        Long l = this.importedFileContactId;
        int i2 = 0;
        int hashCode = (i + (l != null ? l.hashCode() : 0)) * 31;
        Long l2 = this.phoneContactImportId;
        if (l2 != null) {
            i2 = l2.hashCode();
        }
        return hashCode + i2;
    }

    @NotNull
    public String toString() {
        return "ImportedContact(importedContactId=" + this.importedContactId + ", importedFileContactId=" + this.importedFileContactId + ", phoneContactImportId=" + this.phoneContactImportId + ")";
    }
}
