package com.xcyo.baselib.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Created by wanghongyu on 2/1/16.
 */
public class BaseModel extends BaseRecord {
    private static final String TAG = "BaseModel";
    private static final String OBJ_PREFIX = "__object__";
    private static final String SHAREDPREFERENCE_FILE = "__sharedpreference_file__";

    public void save(String key, float value){
        SharedPreferences sharedPreferences = Util.context.getSharedPreferences(SHAREDPREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putFloat(key, value);
        edit.apply();
        edit.commit();
    }

    public void save(String key, int value){
        SharedPreferences sharedPreferences = Util.context.getSharedPreferences(SHAREDPREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.apply();
        edit.commit();
    }

    public void save(String key, String value){
        SharedPreferences sharedPreferences = Util.context.getSharedPreferences(SHAREDPREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
        edit.commit();
    }

    public void save(String key, Serializable value){
        FileOutputStream fos = null;
        try {
            fos = Util.context.openFileOutput(OBJ_PREFIX + TAG + key + OBJ_PREFIX, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(value);
        } catch (FileNotFoundException e) {
            LogUtil.e(TAG, e);
        } catch (IOException e) {
            LogUtil.e(TAG, e);
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    LogUtil.e(TAG, e);
                }
            }
        }
    }

    public String getString(String key){
        return Util.context.getSharedPreferences(SHAREDPREFERENCE_FILE, Context.MODE_PRIVATE).getString(key, "");
    }

    public int getInt(String key){
        return Util.context.getSharedPreferences(SHAREDPREFERENCE_FILE, Context.MODE_PRIVATE).getInt(key, Integer.MAX_VALUE);
    }

    public float getFloat(String key){
        return Util.context.getSharedPreferences(SHAREDPREFERENCE_FILE, Context.MODE_PRIVATE).getFloat(key, Integer.MAX_VALUE);
    }

    public Serializable getSerializable(String key){
        FileInputStream fis = null;
        try {
            fis = Util.context.openFileInput(OBJ_PREFIX + TAG + key + OBJ_PREFIX);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
            LogUtil.e(TAG, e);
        } catch (StreamCorruptedException e) {
            LogUtil.e(TAG, e);
        } catch (IOException e) {
            LogUtil.e(TAG, e);
        } catch (ClassNotFoundException e) {
            LogUtil.e(TAG, e);
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    LogUtil.e(TAG, e);
                }
            }
        }
        return null;
    }
}
