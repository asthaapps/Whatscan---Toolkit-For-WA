package com.whatscan.toolkit.forwa.DataBaseHelper;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ContactDao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

public final class ContactRepository {
    private final ContactDao contactDao;

    public ContactRepository(@NotNull ContactDao contactDao2) {
        contactDao = contactDao2;
    }

    public final void deleteAll(@NotNull List<? extends ContactModel> list) {
        contactDao.deleteAll(list);
    }

    @NotNull
    public final Single<List<ContactModel>> getAllContacts() {
        return contactDao.getAll();
    }

    public final void saveAllContacts(@NotNull List<? extends ContactModel> list) {
        contactDao.insertAll(list);
    }
}
