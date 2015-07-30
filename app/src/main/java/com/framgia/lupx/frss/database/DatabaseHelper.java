package com.framgia.lupx.frss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 23/07/2015.
 */
public class DatabaseHelper<T> extends SQLiteOpenHelper {
    private String TABLE_NAME;
    private String KEY_NAME;
    private String SQL_CREATE_TABLE;
    private List<DBFieldInfo> fieldInfos;
    private Class<T> clazz;

    public DatabaseHelper(Context context, String dbName, Class<T> clazz) throws Exception {
        super(context, dbName, null, 1);
        boolean isTable = clazz.isAnnotationPresent(DBTable.class);
        this.clazz = clazz;
        fieldInfos = new ArrayList<>();
        if (isTable) {
            DBTable tbAnn = clazz.getAnnotation(DBTable.class);
            TABLE_NAME = tbAnn.tableName();
            StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            sb.append(TABLE_NAME);
            sb.append("(");
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(DBField.class)) {
                    DBField fAnn = fields[i].getAnnotation(DBField.class);
                    DBFieldInfo dbf = new DBFieldInfo();
                    dbf.name = fAnn.fieldName();
                    dbf.type = fAnn.type();
                    dbf.fieldName = fields[i].getName();
                    dbf.fieldType = fields[i].getType().getSimpleName();
                    fieldInfos.add(dbf);
                    sb.append(fAnn.fieldName());
                    sb.append(" ");
                    sb.append(fAnn.type());
                    if (fields[i].isAnnotationPresent(DBKey.class)) {
                        sb.append(" PRIMARY KEY ");
                        DBKey key = fields[i].getAnnotation(DBKey.class);
                        KEY_NAME = key.keyName();
                        dbf.isKeyId = true;
                    }
                    sb.append(", ");
                }
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append(")");
            SQL_CREATE_TABLE = sb.toString();
            Log.v("SQL_CREATE_TABLE", SQL_CREATE_TABLE);
        } else {
            throw new Exception(clazz.getSimpleName() + " was not config");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("SQLITE", "CREATE TABLE " + TABLE_NAME);
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.v("SQLITE", "DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }

    public void delete(String where) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_CREATE_TABLE);
        db.delete(TABLE_NAME, where, null);
    }

    public List<T> select(String where) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(SQL_CREATE_TABLE);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + where, null);
        List<T> lst = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T obj = parseCursor(cursor);
            lst.add(obj);
            cursor.moveToNext();
        }
        return lst;
    }

    public int insert(T obj) {
        ContentValues cv = new ContentValues();
        int fS = fieldInfos.size();
        Field field;
        try {
            for (int i = 0; i < fS; i++) {

                DBFieldInfo fi = fieldInfos.get(i);
                if (!fi.isKeyId) {
                    field = clazz.getDeclaredField(fi.fieldName);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        if (fi.fieldType.equals("String")) {
                            String v = value.toString();
                            cv.put(fi.name, v);
                        }
                        if (fi.fieldType.equals("int")) {
                            int v = (int) value;
                            cv.put(fi.name, v);
                        }
                        if (fi.fieldType.equals("long")) {
                            long v = (long) value;
                            cv.put(fi.name, v);
                        }
                        if (fi.fieldType.equals("double")) {
                            double v = (double) value;
                            cv.put(fi.name, v);
                        }
                        if (fi.fieldType.equals("float")) {
                            float v = (float) value;
                            cv.put(fi.name, v);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_CREATE_TABLE);
        return (int) db.insert(TABLE_NAME, null, cv);
    }

    public T getRow(int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(SQL_CREATE_TABLE);
        String idName = null;
        for (int i = 0; i < fieldInfos.size(); i++) {
            if (fieldInfos.get(i).isKeyId)
                idName = fieldInfos.get(i).name;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + idName + "=" + id, null);
        if (cursor != null) {
            return parseCursor(cursor);
        }
        return null;
    }

    public List<T> getAllRows() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(SQL_CREATE_TABLE);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<T> lst = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T obj = parseCursor(cursor);
            lst.add(obj);
            cursor.moveToNext();
        }
        return lst;
    }

    private T parseCursor(Cursor cursor) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            Field field;
            for (int i = 0; i < fieldInfos.size(); i++) {
                DBFieldInfo fi = fieldInfos.get(i);
                field = clazz.getDeclaredField(fi.fieldName);
                int colIndex = cursor.getColumnIndex(fi.name);
                if (colIndex != -1) {
                    if (fi.fieldType.equals("String")) {
                        String str = cursor.getString(colIndex);
                        field.set(obj, str);
                    }
                    if (fi.fieldType.equals("int")) {
                        int v = cursor.getInt(colIndex);
                        field.set(obj, v);
                    }
                    if (fi.fieldType.equals("long")) {
                        long v = cursor.getLong(colIndex);
                        field.set(obj, v);
                    }
                    if (fi.fieldType.equals("double")) {
                        double v = cursor.getDouble(colIndex);
                        field.set(obj, v);
                    }
                    if (fi.fieldType.equals("float")) {
                        float v = cursor.getFloat(colIndex);
                        field.set(obj, v);
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return obj;
    }

}