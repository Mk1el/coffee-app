package com.example.coffee_work.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.coffee_work.Domain.ItemsModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class TinyDB {

    private final SharedPreferences preferences;
    private final Context context;
    private String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    private String lastImagePath = "";

    public TinyDB(Context appContext) {
        this.context = appContext;
        this.preferences = appContext.getSharedPreferences("TinyDB_Pref", Context.MODE_PRIVATE);
    }

    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Bitmap getImage(String path) {
        try {
            return BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSavedImagePath() {
        return lastImagePath;
    }

    public String putImage(String theFolder, String theImageName, Bitmap theBitmap) {
        if (theFolder == null || theImageName == null || theBitmap == null)
            return null;

        this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder;
        String mFullPath = setupFullPath(theImageName);

        if (!mFullPath.isEmpty()) {
            lastImagePath = mFullPath;
            saveBitmap(mFullPath, theBitmap);
        }

        return mFullPath;
    }

    public boolean putImageWithFullPath(String fullPath, Bitmap theBitmap) {
        return !(fullPath == null || theBitmap == null) && saveBitmap(fullPath, theBitmap);
    }

    private String setupFullPath(String imageName) {
        File baseDir = context.getExternalFilesDir(null);
        if (baseDir == null) {
            baseDir = context.getFilesDir();
        }

        File mFolder = new File(baseDir, DEFAULT_APP_IMAGEDATA_DIRECTORY);
        if (!mFolder.exists()) {
            if (!mFolder.mkdirs()) {
                Log.e("TinyDB", "Failed to create folder: " + mFolder.getAbsolutePath());
                return "";
            }
        }

        return new File(mFolder, imageName).getAbsolutePath();
    }

    private boolean saveBitmap(String fullPath, Bitmap bitmap) {
        if (fullPath == null || bitmap == null)
            return false;

        File imageFile = new File(fullPath);
        if (imageFile.exists() && !imageFile.delete()) {
            return false;
        }

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            return bitmap.compress(CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================== GETTERS ==================

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public ArrayList<Integer> getListInt(String key) {
        String data = preferences.getString(key, "");
        if (TextUtils.isEmpty(data)) return new ArrayList<>();

        String[] myList = TextUtils.split(data, "‚‗‚");
        ArrayList<Integer> newList = new ArrayList<>();
        for (String item : myList) {
            newList.add(Integer.parseInt(item));
        }
        return newList;
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0L);
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0.0f);
    }

    public double getDouble(String key) {
        String number = getString(key);
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public ArrayList<Double> getListDouble(String key) {
        String data = preferences.getString(key, "");
        if (TextUtils.isEmpty(data)) return new ArrayList<>();

        String[] myList = TextUtils.split(data, "‚‗‚");
        ArrayList<Double> newList = new ArrayList<>();
        for (String item : myList) {
            newList.add(Double.parseDouble(item));
        }
        return newList;
    }

    public ArrayList<Long> getListLong(String key) {
        String data = preferences.getString(key, "");
        if (TextUtils.isEmpty(data)) return new ArrayList<>();

        String[] myList = TextUtils.split(data, "‚‗‚");
        ArrayList<Long> newList = new ArrayList<>();
        for (String item : myList) {
            newList.add(Long.parseLong(item));
        }
        return newList;
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public ArrayList<String> getListString(String key) {
        String data = preferences.getString(key, "");
        if (TextUtils.isEmpty(data)) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(TextUtils.split(data, "‚‗‚")));
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> myList = getListString(key);
        ArrayList<Boolean> newList = new ArrayList<>();
        for (String item : myList) {
            newList.add(Boolean.parseBoolean(item));
        }
        return newList;
    }

    public ArrayList<ItemsModel> getListObject(String key) {
        Gson gson = new Gson();
        ArrayList<String> objStrings = getListString(key);
        ArrayList<ItemsModel> resultList = new ArrayList<>();

        for (String jObjString : objStrings) {
            ItemsModel item = gson.fromJson(jObjString, ItemsModel.class);
            if (item != null) {
                resultList.add(item);
            }
        }
        return resultList;
    }

    public <T> T getObject(String key, Class<T> classOfT) {
        String json = getString(key);
        return new Gson().fromJson(json, classOfT);
    }

    // ================== PUTTERS ==================

    public void putInt(String key, int value) {
        checkForNullKey(key);
        preferences.edit().putInt(key, value).apply();
    }

    public void putListInt(String key, ArrayList<Integer> intList) {
        checkForNullKey(key);
        Integer[] myIntList = intList.toArray(new Integer[0]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply();
    }

    public void putLong(String key, long value) {
        checkForNullKey(key);
        preferences.edit().putLong(key, value).apply();
    }

    public void putListLong(String key, ArrayList<Long> longList) {
        checkForNullKey(key);
        Long[] myLongList = longList.toArray(new Long[0]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply();
    }

    public void putFloat(String key, float value) {
        checkForNullKey(key);
        preferences.edit().putFloat(key, value).apply();
    }

    public void putDouble(String key, double value) {
        checkForNullKey(key);
        putString(key, String.valueOf(value));
    }

    public void putListDouble(String key, ArrayList<Double> doubleList) {
        checkForNullKey(key);
        Double[] myDoubleList = doubleList.toArray(new Double[0]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply();
    }

    public void putString(String key, String value) {
        checkForNullKey(key);
        checkForNullValue(value);
        preferences.edit().putString(key, value).apply();
    }

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[0]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public void putBoolean(String key, boolean value) {
        checkForNullKey(key);
        preferences.edit().putBoolean(key, value).apply();
    }

    public void putListBoolean(String key, ArrayList<Boolean> boolList) {
        checkForNullKey(key);
        ArrayList<String> newList = new ArrayList<>();
        for (Boolean item : boolList) {
            newList.add(String.valueOf(item));
        }
        putListString(key, newList);
    }

    public void putObject(String key, Object obj) {
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    public void putListObject(String key, ArrayList<ItemsModel> itemList) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for (ItemsModel item : itemList) {
            objStrings.add(gson.toJson(item));
        }
        putListString(key, objStrings);
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public boolean deleteImage(String path) {
        return new File(path).delete();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
    }

    private void checkForNullValue(String value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
    }
}