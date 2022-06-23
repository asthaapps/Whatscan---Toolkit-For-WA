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
public interface ImportedFileDao {
    @Delete
    void delete(@NotNull ImportedFile importedFile);

    @NotNull
    @Query("SELECT * FROM IMPORTED_FILES_TABLE WHERE importedFileId LIKE :first ")
    ImportedFile findByName(@NotNull String first);

    @NotNull
    @Query("SELECT * FROM IMPORTED_FILES_TABLE")
    Single<List<ImportedFile>> getAll();

    @NotNull
    @Query("SELECT * FROM IMPORTED_FILES_TABLE WHERE importedFileId LIKE :importedFileId ")
    Single<ImportedFile> getImportedFile(long importedFileId);

    @Query("SELECT * FROM IMPORTED_FILES_TABLE WHERE name LIKE :importedFileName ")
    long getImportedFileId(@NotNull String importedFileName);

    @Insert
    void insertAll(@NotNull ImportedFile... importedFileArr);

    @Insert(onConflict = 1)
    void insertImportedFile(@NotNull ImportedFile importedFile);

    @NotNull
    @Query("SELECT * FROM IMPORTED_FILES_TABLE WHERE importedFileId IN (:userIds)")
    List<ImportedFile> loadAllByIds(@NotNull int[] userIds);

    @Update
    void updateImportedFile(@NotNull ImportedFile importedFile);
}
