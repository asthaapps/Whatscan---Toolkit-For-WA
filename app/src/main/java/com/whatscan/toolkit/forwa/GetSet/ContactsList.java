package com.whatscan.toolkit.forwa.GetSet;

import java.util.ArrayList;

public class ContactsList {
    public ArrayList<Contact> contactArrayList = new ArrayList<>();

    public int getCount() {
        return this.contactArrayList.size();
    }

    public void addContact(Contact contact) {
        this.contactArrayList.add(contact);
    }

    public void removeContact(Contact contact) {
        this.contactArrayList.remove(contact);
    }

    public Contact getContact(int i) {
        for (int i2 = 0; i2 < getCount(); i2++) {
            if (Integer.parseInt(this.contactArrayList.get(i2).id) == i) {
                return this.contactArrayList.get(i2);
            }
        }
        return null;
    }
}
