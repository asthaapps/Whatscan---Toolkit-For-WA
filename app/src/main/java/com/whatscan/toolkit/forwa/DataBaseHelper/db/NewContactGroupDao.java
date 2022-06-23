package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface NewContactGroupDao {
    @Delete
    void delete(NewContactGroup newContactGroup);

    @Delete
    void deleteAll(List<NewContactGroup> list);

    @Query("SELECT * FROM NEW_CONTACT_IN_GROUP_TABLE")
    List<NewContactGroup> getAll();

    @Query("SELECT * FROM NEW_CONTACT_IN_GROUP_TABLE WHERE groupId IN (:groupId)")
    Single<List<NewContactGroup>> getAllContactGroupByGroupId(int groupId);

    @Query("SELECT * FROM PHONE_CONTACT_TABLE INNER JOIN NEW_CONTACT_IN_GROUP_TABLE ON PHONE_CONTACT_TABLE.contactId=NEW_CONTACT_IN_GROUP_TABLE.groupPhoneContactId WHERE NEW_CONTACT_IN_GROUP_TABLE.groupId=:groupId")
    Single<List<ContactModel>> getContactsFromGroupId(int groupId);

    @Query("SELECT groupPhoneContactId FROM NEW_CONTACT_IN_GROUP_TABLE WHERE groupId=:groupId")
    Single<List<Integer>> getContactsIdsFromGroupId(int groupId);

    @Query("SELECT * FROM NEW_CONTACT_IN_GROUP_TABLE WHERE contactId LIKE :conId")
    NewContactGroup getGroupModelByContactId(String conId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NewContactGroup> list);

    @Query("SELECT * FROM NEW_CONTACT_IN_GROUP_TABLE WHERE contactGroupId IN (:userIds)")
    List<NewContactGroup> loadAllByIds(int[] userIds);
}
