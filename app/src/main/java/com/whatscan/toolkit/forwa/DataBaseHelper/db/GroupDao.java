package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface GroupDao {
    @Delete
    void delete(@NotNull GroupModel groupModel);

    @Query("SELECT * FROM GROUP_TABLE WHERE id LIKE :first ")
    @NotNull
    GroupModel findByName(@NotNull String first);

    @Query("SELECT * FROM GROUP_TABLE")
    @NotNull
    Single<List<GroupModel>> getAll();

    @Query("SELECT * FROM GROUP_TABLE WHERE id LIKE :groupId ")
    @NotNull
    Single<GroupModel> getGroup(int groupId);

    @Query("SELECT * FROM GROUP_TABLE WHERE name LIKE :groupName ")
    int getGroupId(@NotNull String groupName);

    @Insert
    void insertAll(@NotNull GroupModel... groupModelArr);

    @Insert(onConflict = 1)
    void insertGroup(@NotNull GroupModel groupModel);

    @Query("SELECT * FROM GROUP_TABLE WHERE id IN (:userIds)")
    @NotNull
    List<GroupModel> loadAllByIds(@NotNull int[] userIds);

    @Update
    void updateGroup(@NotNull GroupModel groupModel);
}
