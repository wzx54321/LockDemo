/*
 * Copyright (C) 2013 litesuits.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.lib.lock.gesture.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * <p>
 * 邮箱：ittfxin@126.com
 * <P>
 * https://github.com/wzx54321/XinFrameworkLib
 */


public class DataKeeper {
    private final SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private static final String TAG = DataKeeper.class.getSimpleName();

    @SuppressLint("CommitPrefEdits")
    DataKeeper(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * *************** get ******************
     */

    public String get(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public float get(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long get(String key, long defValue) {
        return sp.getLong(key, defValue);
    }


    public SharedPreferences.Editor put(String key, String value) {
       /* if (value == null) {
            editor.remove(key);
        } else {*/
        editor.putString(key, value);
       /* }*/
        return editor;
    }

    public SharedPreferences.Editor put(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor;
    }

    public SharedPreferences.Editor put(String key, float value) {
        editor.putFloat(key, value);
        return editor;
    }

    public SharedPreferences.Editor put(String key, long value) {
        editor.putLong(key, value);
        return editor;
    }

    public SharedPreferences.Editor putInt(String key, int value) {
        editor.putInt(key, value);
        return editor;
    }

}
