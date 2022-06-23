package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ContactDao {
    @Delete
    void delete(@NotNull ContactModel contactModel);

    @Delete
    void deleteAll(@NotNull List<? extends ContactModel> list);

    @Query("SELECT * FROM PHONE_CONTACT_TABLE WHERE name LIKE :first ")
    @NotNull
    ContactModel findByName(@NotNull String first);

    @Query("SELECT * FROM PHONE_CONTACT_TABLE  ORDER BY name ASC")
    @NotNull
    Single<List<ContactModel>> getAll();

    @Query("SELECT * FROM PHONE_CONTACT_TABLE WHERE id IN (:phoneId) ")
    @NotNull
    ContactModel getContactFromIdSync(int phoneId);

    @Query("SELECT * FROM PHONE_CONTACT_TABLE WHERE id IN (:phoneId) ")
    @NotNull
    Single<List<ContactModel>> getContactFromIds(int phoneId);

    @Query("SELECT * FROM PHONE_CONTACT_TABLE WHERE contactId IN (:userId) ")
    @NotNull
    Single<ContactModel> getContactIds(@NotNull String userId);

    @Query("SELECT COUNT(id) FROM PHONE_CONTACT_TABLE  ")
    int getCount();

    @Insert(onConflict = 1)
    void insertAll(@NotNull List<? extends ContactModel> list);

    @Insert(onConflict = 1)
    @NotNull
    List<Long> insertAllContacts(@NotNull List<? extends ContactModel> list);

    @Query("SELECT * FROM PHONE_CONTACT_TABLE WHERE contactId IN (:userIds) LIMIT 100")
    @NotNull
    Single<List<ContactModel>> loadAllByContactIds(@NotNull List<String> userIds);

    @Query("SELECT * FROM PHONE_CONTACT_TABLE WHERE id IN (:userIds)")
    @NotNull
    List<ContactModel> loadAllByIds(@NotNull int[] userIds);
}
