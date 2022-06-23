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
public interface ImportedContactsDao {
    @Delete
    void delete(@NotNull ImportedContact importedContact);

    @Query("DELETE FROM IMPORTED_CONTACTS_TABLE WHERE importedFileContactId = :impFileId and phoneContactImportId = :contactId")
    void deleteImportedContact(long impFileId, long contactId);

    @Query("SELECT * FROM IMPORTED_CONTACTS_TABLE")
    @NotNull
    Single<List<ImportedContact>> getAll();

    @Query("SELECT * FROM PHONE_CONTACT_TABLE INNER JOIN IMPORTED_CONTACTS_TABLE ON PHONE_CONTACT_TABLE.id=IMPORTED_CONTACTS_TABLE.phoneContactImportId WHERE IMPORTED_CONTACTS_TABLE.importedFileContactId=:importedId")
    @NotNull
    Single<List<ContactModel>> getContactsFromImportId(long importedId);

    @Query("SELECT phoneContactImportId FROM IMPORTED_CONTACTS_TABLE WHERE importedFileContactId=:importedId")
    @NotNull
    Single<List<Integer>> getContactsIdsFromImportId(long importedId);

    @Query("SELECT * FROM IMPORTED_CONTACTS_TABLE")
    @NotNull
    Single<List<ImportedContact>> getImportedFile();

    @Insert
    @NotNull
    List<Long> insertAll(@NotNull List<ImportedContact> list);

    @Insert(onConflict = 1)
    void insertImportedContact(@NotNull ImportedContact importedContact);

    @Update
    void updateImportedContact(@NotNull ImportedContact importedContact);
}
