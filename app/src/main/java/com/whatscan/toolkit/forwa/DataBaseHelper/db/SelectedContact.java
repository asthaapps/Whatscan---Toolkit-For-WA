package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity(tableName = DatabaseTableName.SELECTED_CONTACTS)
public final class SelectedContact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int selectedContactId;

    public SelectedContact() {
    }

    public SelectedContact(int i, int i2) {
        this.id = i;
        this.selectedContactId = i2;
    }

    public boolean equals(@Nullable Object obj) {
        if (this != obj) {
            if (obj instanceof SelectedContact) {
                SelectedContact selectedContact = (SelectedContact) obj;
                if (this.id == selectedContact.id) {
                    if (this.selectedContactId == selectedContact.selectedContactId) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public final int getId() {
        return this.id;
    }

    public final void setId(int i) {
        this.id = i;
    }

    public final int getSelectedContactId() {
        return this.selectedContactId;
    }

    public final void setSelectedContactId(int i) {
        this.selectedContactId = i;
    }

    public int hashCode() {
        return (this.id * 31) + this.selectedContactId;
    }

    @NotNull
    public String toString() {
        return "SelectedContact(id=" + this.id + ", selectedContactId=" + this.selectedContactId + ")";
    }
}