package com.whatscan.toolkit.forwa.ContactExport.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class JSON {
    private JSONArray jsArr;
    private JSONObject jsObj;
    private Object value;

    public JSON(Object obj) {
        obj = ArrayList.class.isInstance(obj) ? new JSONArray((ArrayList) obj) : obj;
        if (obj == null) {
            this.value = null;
        } else if (obj.toString().trim().startsWith("{")) {
            try {
                this.jsObj = new JSONObject(obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (obj.toString().trim().startsWith("[")) {
            try {
                this.jsArr = new JSONArray(obj.toString());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        } else {
            this.value = obj;
        }
    }

    public static JSON create(Object obj) {
        return new JSON(obj.toString());
    }

    public static JSONObject dic(Object... objArr) {
        JSONObject jSONObject = new JSONObject();
        for (int i = 0; i < objArr.length; i += 2) {
            try {
                Object obj = objArr[i + 1];
                if (JSON.class.isInstance(obj)) {
                    if (((JSON) obj).getJsonArray() != null) {
                        obj = ((JSON) obj).getJsonArray();
                    } else if (((JSON) obj).getJsonObject() != null) {
                        obj = ((JSON) obj).getJsonObject();
                    }
                }
                String str = (String) objArr[i];
                if (obj == null) {
                    obj = JSONObject.NULL;
                }
                jSONObject.put(str, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    public static JSONArray array(Object... objArr) {
        JSONArray jSONArray = new JSONArray();
        for (Object put : objArr) {
            jSONArray.put(put);
        }
        return jSONArray;
    }

    public static boolean jsonObjectComparesEqual(JSONObject jSONObject, JSONObject jSONObject2, Collection<String> collection, Collection<String> collection2) {
        Object obj;
        Set<String> keySet = keySet(jSONObject);
        keySet.addAll(keySet(jSONObject2));
        if (collection != null) {
            keySet.retainAll(collection);
        }
        if (collection2 != null) {
            keySet.removeAll(collection2);
        }
        for (String next : keySet) {
            Object obj2 = null;
            try {
                obj = jSONObject.get(next);
            } catch (JSONException unused) {
                obj = null;
            }
            try {
                obj2 = jSONObject.get(next);
            } catch (JSONException unused2) {
            }
            if (obj != null) {
                if (!obj.equals(obj2)) {
                    return false;
                }
            } else if (obj2 != null && !obj2.equals(obj)) {
                return false;
            }
        }
        return true;
    }

    private static Set<String> keySet(JSONObject jSONObject) {
        TreeSet treeSet = new TreeSet();
        Iterator it = new AsIterable(jSONObject.keys()).iterator();
        while (it.hasNext()) {
            treeSet.add((String) it.next());
        }
        return treeSet;
    }

    public String toString() {
        if (isNull()) {
            return "";
        }
        return value().toString();
    }

    public int count() {
        JSONObject jSONObject = this.jsObj;
        if (jSONObject != null) {
            return jSONObject.length();
        }
        JSONArray jSONArray = this.jsArr;
        if (jSONArray != null) {
            return jSONArray.length();
        }
        return 0;
    }

    public JSON key(String str) {
        JSONObject jSONObject = this.jsObj;
        if (jSONObject == null) {
            return new JSON((Object) null);
        }
        try {
            Object obj = jSONObject.get(str);
            if (obj != null) {
                return new JSON(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSON((Object) null);
    }

    public JSON index(int i) {
        JSONArray jSONArray = this.jsArr;
        if (jSONArray == null) {
            return new JSON((Object) null);
        }
        try {
            return new JSON(jSONArray.get(i));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSON((Object) null);
        }
    }

    public String stringValue() {
        if (value() != null && !isNull()) {
            return String.valueOf(value());
        }
        return "";
    }

    public int intValue() {
        if (value() == null) {
            return 0;
        }
        try {
            return Integer.valueOf(value().toString()).intValue();
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public long longValue() {
        if (value() == null) {
            return 0;
        }
        try {
            return Long.valueOf(value().toString()).longValue();
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public Double doubleValue() {
        Object value2 = value();
        Double valueOf = Double.valueOf("0.0d");
        if (value2 == null) {
            return valueOf;
        }
        try {
            return Double.valueOf(value().toString());
        } catch (NumberFormatException unused) {
            return valueOf;
        }
    }

    public boolean booleanValue() {
        if (value() == null) {
            return false;
        }
        return stringValue().equals("true");
    }

    public Object value() {
        if (nullableValue() != null) {
            return nullableValue();
        }
        return new JSON((Object) null);
    }

    public boolean isNull() {
        if ((this.value != null || this.jsArr != null || this.jsObj != null) && !String.valueOf(value()).equals("null")) {
            return false;
        }
        return true;
    }

    private Object nullableValue() {
        Object obj = this.value;
        if (obj != null) {
            return obj;
        }
        JSONObject jSONObject = this.jsObj;
        if (jSONObject != null) {
            return jSONObject.toString();
        }
        JSONArray jSONArray = this.jsArr;
        if (jSONArray != null) {
            return jSONArray.toString();
        }
        return null;
    }

    public boolean exist() {
        return nullableValue() != null;
    }

    public JSONObject getJsonObject() {
        return this.jsObj;
    }

    public JSONArray getJsonArray() {
        return this.jsArr;
    }

    public void removeWithKey(String str) {
        JSONObject jsonObject;
        if (str != null && (jsonObject = getJsonObject()) != null) {
            jsonObject.remove(str);
            this.jsObj = jsonObject;
        }
    }

    public void addEditWithKey(String str, Object obj) {
        JSONObject jsonObject;
        if (obj != null && str != null && (jsonObject = getJsonObject()) != null) {
            if (JSON.class.isInstance(obj)) {
                JSON json = (JSON) obj;
                if (json.getJsonArray() != null) {
                    obj = json.getJsonArray();
                } else if (json.getJsonObject() != null) {
                    obj = json.getJsonObject();
                } else {
                    obj = json.value();
                }
            }
            try {
                jsonObject.putOpt(str, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.jsObj = jsonObject;
        }
    }

    public void addWithIndex(Object obj, int i) {
        JSONArray jsonArray;
        if (obj != null && (jsonArray = getJsonArray()) != null) {
            try {
                if (JSON.class.isInstance(obj)) {
                    if (((JSON) obj).getJsonArray() != null) {
                        if (i == -1) {
                            jsonArray.put(((JSON) obj).getJsonArray());
                        } else {
                            jsonArray.put(i, ((JSON) obj).getJsonArray());
                        }
                    } else if (((JSON) obj).getJsonObject() != null) {
                        if (i == -1) {
                            jsonArray.put(((JSON) obj).getJsonObject());
                        } else {
                            jsonArray.put(i, ((JSON) obj).getJsonObject());
                        }
                    } else if (i == -1) {
                        jsonArray.put(((JSON) obj).value());
                    } else {
                        jsonArray.put(i, ((JSON) obj).value());
                    }
                } else if (i == -1) {
                    jsonArray.put(obj);
                } else if (i > -1) {
                    jsonArray.put(i, obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.jsArr = jsonArray;
        }
    }

    public void add(Object obj) {
        addWithIndex(obj, -1);
    }

    public void removeWithIndex(int i) {
        JSONArray jsonArray = getJsonArray();
        if (jsonArray != null) {
            JSONArray jSONArray = new JSONArray();
            for (int i2 = 0; i2 < jsonArray.length(); i2++) {
                if (i2 != i) {
                    try {
                        jSONArray.put(jsonArray.get(i2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.jsArr = jSONArray;
        }
    }

    public void remove(Object obj) {
        JSONArray jsonArray;
        if (obj != null && (jsonArray = getJsonArray()) != null) {
            JSONArray jSONArray = new JSONArray();
            int i = 0;
            while (i < jsonArray.length()) {
                try {
                    if (!JSONObject.class.isInstance(obj) || !JSONObject.class.isInstance(jsonArray.get(i))) {
                        if (!jsonArray.get(i).equals(obj)) {
                            jSONArray.put(jsonArray.get(i));
                        }
                        i++;
                    } else {
                        if (!jsonObjectComparesEqual((JSONObject) jsonArray.get(i), (JSONObject) obj, (Collection<String>) null, (Collection<String>) null)) {
                            jSONArray.put(jsonArray.get(i));
                        }
                        i++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            this.jsArr = jSONArray;
        }
    }

    private static class AsIterable<T> implements Iterable<T> {
        private Iterator<T> iterator;

        public AsIterable(Iterator<T> it) {
            this.iterator = it;
        }

        public Iterator<T> iterator() {
            return this.iterator;
        }
    }
}