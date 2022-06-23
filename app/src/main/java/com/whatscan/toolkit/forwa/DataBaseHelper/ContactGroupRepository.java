package com.whatscan.toolkit.forwa.DataBaseHelper;

import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.NewContactGroup;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.NewContactGroupDao;

import java.util.List;

import io.reactivex.Single;

public final class ContactGroupRepository {
    public final GroupDao groupDao;
    public final NewContactGroupDao newContactGroupDao;

    public ContactGroupRepository(NewContactGroupDao newContactGroupDao2, GroupDao groupDao2) {
        this.newContactGroupDao = newContactGroupDao2;
        this.groupDao = groupDao2;
    }

    public final Single<List<GroupModel>> getAllGroup() {
        return groupDao.getAll();
    }

    public final List<NewContactGroup> getAllGroupContact() {
        return newContactGroupDao.getAll();
    }

    public final GroupDao getGroupDao() {
        return groupDao;
    }

    public final int getGroupId(String str) {
        return groupDao.getGroupId(str);
    }

    public final NewContactGroupDao getNewContactGroupDao() {
        return newContactGroupDao;
    }

    public final void saveContactGroups(List<NewContactGroup> list) {
        newContactGroupDao.insertAll(list);
    }

    public final void saveGroup(GroupModel groupModel) {
        groupDao.insertGroup(groupModel);
    }
}