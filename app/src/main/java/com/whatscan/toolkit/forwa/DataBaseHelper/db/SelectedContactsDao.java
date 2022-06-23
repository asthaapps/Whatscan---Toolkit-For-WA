package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface SelectedContactsDao {
    @Delete
    void delete(@NotNull SelectedContact selectedContact);

    @Query("DELETE FROM SELECTED_CONTACTS")
    void deleteAll();

    @Query("SELECT * FROM SELECTED_CONTACTS")
    @NotNull
    Single<List<SelectedContact>> getAll();

    @Query("SELECT selectedContactId FROM SELECTED_CONTACTS")
    @NotNull
    Single<List<Integer>> getAllContactsId();

    @Query("SELECT * FROM PHONE_CONTACT_TABLE INNER JOIN SELECTED_CONTACTS ON PHONE_CONTACT_TABLE.id=SELECTED_CONTACTS.selectedContactId ")
    @NotNull
    Single<List<ContactModel>> getContactsSelectedId();

    @Insert
    void insertAll(@NotNull List<SelectedContact> list);

    @Insert(onConflict = 1)
    void insertSelectedContact(@NotNull SelectedContact selectedContact);

    @Update
    void updateSelectedContact(@NotNull SelectedContact selectedContact);
}
