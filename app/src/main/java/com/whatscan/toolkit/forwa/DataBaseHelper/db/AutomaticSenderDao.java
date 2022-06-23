package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface AutomaticSenderDao {
    @Delete
    void delete(@NotNull AutomaticSendRecord automaticSendRecord);

    @Delete
    void deleteAll(@NotNull List<AutomaticSendRecord> list);

    @Query("SELECT * FROM AUTOMATIC_SENT_HISTORY")
    @NotNull
    List<AutomaticSendRecord> getAll();

    @Query("SELECT * FROM AUTOMATIC_SENT_HISTORY WHERE userPlan='free' AND sendMode='FREE'")
    @NotNull
    Single<List<AutomaticSendRecord>> getAllFreeTrialList();

    @Insert(onConflict = 1)
    void insert(@NotNull AutomaticSendRecord automaticSendRecord);

    @Insert(onConflict = 1)
    void insertAll(@NotNull List<AutomaticSendRecord> list);
}
