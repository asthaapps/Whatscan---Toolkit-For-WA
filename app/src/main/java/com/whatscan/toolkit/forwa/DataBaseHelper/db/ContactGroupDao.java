package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ContactGroupDao {
    @Delete
    void delete(@NotNull ContactGroup contactGroup);

    @Delete
    void deleteAll(@NotNull List<ContactGroup> list);

    @Query("SELECT * FROM CONTACT_IN_GROUP_TABLE")
    @NotNull
    List<ContactGroup> getAll();

    @Query("SELECT * FROM CONTACT_IN_GROUP_TABLE WHERE groupId IN (:groupId)")
    @NotNull
    Single<List<ContactGroup>> getAllContactGroupByGroupId(int groupId);

    @Query("SELECT * FROM CONTACT_IN_GROUP_TABLE WHERE contactId LIKE :conId")
    @NotNull
    ContactGroup getGroupModelByContactId(@NotNull String conId);

    @Insert(onConflict = 1)
    void insertAll(@NotNull List<ContactGroup> list);

    @Query("SELECT * FROM CONTACT_IN_GROUP_TABLE WHERE id IN (:userIds)")
    @NotNull
    List<ContactGroup> loadAllByIds(@NotNull int[] userIds);
}