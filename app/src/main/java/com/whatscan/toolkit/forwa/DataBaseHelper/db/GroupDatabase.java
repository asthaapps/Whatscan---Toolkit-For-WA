package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;

import org.jetbrains.annotations.NotNull;

@Database(entities = {ContactModel.class, GroupModel.class, NewContactGroup.class, AutomaticSendRecord.class, ImportedFile.class, ImportedContact.class, SelectedContact.class, ContactGroup.class}, version = 3, exportSchema = false)
public abstract class GroupDatabase extends RoomDatabase {
    static final Migration d = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `PHONE_CONTACT_TABLE` (`name` TEXT, `phoneNumber` TEXT, `countryCode` TEXT, `photoUri` TEXT, `contactId` TEXT, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `key` INTEGER NOT NULL, `isSelected` INTEGER NOT NULL)");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `GROUP_TABLE` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `count` INTEGER, `name` TEXT, `description` TEXT)");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `NEW_CONTACT_IN_GROUP_TABLE` (`contactGroupId` INTEGER PRIMARY KEY AUTOINCREMENT, `contactId` TEXT, `groupPhoneContactId` INTEGER, `groupId` INTEGER, FOREIGN KEY(`groupId`) REFERENCES `GROUP_TABLE`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`groupPhoneContactId`) REFERENCES `PHONE_CONTACT_TABLE`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `AUTOMATIC_SENT_HISTORY` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `phoneNumber` TEXT, `message` TEXT, `sendThrough` TEXT, `sendMode` TEXT, `userPlan` TEXT, `timestamp` INTEGER NOT NULL)");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Imported_files_table` (`importedFileId` INTEGER PRIMARY KEY AUTOINCREMENT, `count` INTEGER, `name` TEXT, `timestamp` INTEGER)");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Imported_contacts_table` (`importedContactId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `importedFileContactId` INTEGER, `phoneContactImportId` INTEGER, FOREIGN KEY(`importedFileContactId`) REFERENCES `Imported_files_table`(`importedFileId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`phoneContactImportId`) REFERENCES `PHONE_CONTACT_TABLE`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `SELECTED_CONTACTS` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `selectedContactId` INTEGER NOT NULL)");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `CONTACT_IN_GROUP_TABLE` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `contactId` TEXT, `groupId` INTEGER)");
            supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
        }
    };

    private static GroupDatabase groupDatabase;
    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            super.onCreate(supportSQLiteDatabase);
            new PopulateDbAsyncTask(groupDatabase).execute(new Void[0]);
        }
    };

    static {
        new Migration(2, 3) {
            @Override
            public void migrate(@NotNull SupportSQLiteDatabase supportSQLiteDatabase) {
            }
        };
        new Migration(1, 2) {
            @Override
            public void migrate(@NotNull SupportSQLiteDatabase supportSQLiteDatabase) {
                try {
                    supportSQLiteDatabase.execSQL("CREATE UNIQUE INDEX contactId ON CONTACT_IN_GROUP_TABLE (contactId)");
                    supportSQLiteDatabase.execSQL("CREATE UNIQUE INDEX groupId ON CONTACT_IN_GROUP_TABLE (groupId)");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static GroupDatabase getInstance(Context context) {
        if (groupDatabase == null) {
            groupDatabase = Room.databaseBuilder(context.getApplicationContext(), GroupDatabase.class, "wt_chat_database.db").addMigrations(d).build();
        }
        return groupDatabase;
    }

    public abstract AutomaticSenderDao getAutomaticSenderDao();

    public abstract ContactDao getContactDao();

    public abstract ContactGroupDao getContactGroupDao();

    public abstract GroupDao getGroupDao();

    public abstract NewContactGroupDao getNewContactGroupDao();

    public abstract ImportedContactsDao importedContactsDao();

    public abstract ImportedFileDao importedFileDao();

    public abstract SelectedContactsDao selectedContactsDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private GroupDao noteDao;

        private PopulateDbAsyncTask(GroupDatabase groupDatabase) {
            this.noteDao = groupDatabase.getGroupDao();
        }

        public Void doInBackground(Void... voidArr) {
            for (int i = 0; i < GroupListKt.getGroupList().size(); i++) {
                GroupModel groupModel = new GroupModel();
                groupModel.setName(GroupListKt.getGroupList().get(i));
                groupModel.setId(Integer.valueOf(i));
                groupModel.setCount(0);
                this.noteDao.insertGroup(groupModel);
            }
            return null;
        }
    }
}
