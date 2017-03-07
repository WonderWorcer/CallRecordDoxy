package com.aykuttasil.callrecord.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * \brief Регистрация звонков.
 * \author WonderWorcer
 * \version 0.5
 * \date 7 марта 2017 года
 *
 * Класс, необходим для записи настроек пользователя в контекст
 * и изменения самим приложением контекста
 */

public class PrefsHelper {

    private static final String DEFAULT_PREF_NAME = PrefsHelper.class.getSimpleName();
    private static final String DEFAULT_STRING_VALUE = null;
    private static final int DEFAULT_INT_VALUE = 0;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;
    private Context mContext;
    private SharedPreferences mPref;

    /**
     *
     * @param context текущий контект приложения
     * @param pref_name
     */
    public PrefsHelper(Context context, String pref_name) {
        mContext = context;
        mPref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    }

    /**
     *
     * @param context текущий контект приложения
     */
    public PrefsHelper(Context context) {
        mContext = context;
        mPref = getDefaultPreference(context);
    }

    /**
     *
     * @param context текущий контект приложения
     * @return
     */
    public static SharedPreferences getDefaultPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Записывает строковое значение переданного параметра в контект
     * @param context текущий контект приложения
     * @param key название параметра
     * @param value значение параметра
     */
    public static void writePrefString(Context context, String key, String value) {
        new PrefsHelper(context).getPrefEditor().putString(key, value).commit();
    }

    /**
     * Получает значение переданного параметра из контекта
     * @param context текущий контект приложения
     * @param key название параметра
     * @return
     */
    public static String readPrefString(Context context, String key) {
        return new PrefsHelper(context).getPreference().getString(key, DEFAULT_STRING_VALUE);
    }

    /**
     * Записывает целочисленное значение переданного параметра в контекст
     * @param context текущий контект приложения
     * @param key название параметра
     * @param value значение параметра
     */
    public static void writePrefInt(Context context, String key, int value) {
        new PrefsHelper(context).getPrefEditor().putInt(key, value).commit();
    }

    /**
     * Получает целочисленное значение переданного параметра из контекста
     * @param context текущий контект приложения
     * @param key название параметра
     * @return
     */
    public static int readPrefInt(Context context, String key) {
        return new PrefsHelper(context).getPreference().getInt(key, DEFAULT_INT_VALUE);
    }

    /**
     * Записывает логическое значение переданного параметра в контекст
     * @param context текущий контект приложения
     * @param key название параметра
     * @param value значение параметра
     */
    public static void writePrefBool(Context context, String key, boolean value) {
        new PrefsHelper(context).getPrefEditor().putBoolean(key, value).commit();
    }

    /**
     * Получает логическое значение переданного параметра из контекста
     * @param context текущий контект приложения
     * @param key
     * @return
     */
    public static boolean readPrefBool(Context context, String key) {
        return new PrefsHelper(context).getPreference().getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }
    //

    /**
     * Отчищает контекст от пользовательских настроек
     * @param context текущий контект приложения
     */
    public static void clearPreference(Context context) {
        new PrefsHelper(context).getPreference().edit().clear().apply();

    }

    /**
     * Необходим для получения настроек контекста
     * @return объект, содержащий настройки текущего контекста приложения
     */
    public SharedPreferences getPreference() {
        return mPref;
    }

    /**
     * Необходим для получения объекта, содержащего общие настройки контекста
     * @return объект, сожержащий настройки контекста
     */
    public SharedPreferences.Editor getPrefEditor() {
        return getPreference().edit();
    }

}
