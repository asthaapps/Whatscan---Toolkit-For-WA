package com.whatscan.toolkit.forwa.WSticker.editimage.utils;

import java.util.List;

public class ListUtil {
    public static boolean isEmpty(List list) {
        if (list == null)
            return true;

        return list.size() == 0;
    }

}
