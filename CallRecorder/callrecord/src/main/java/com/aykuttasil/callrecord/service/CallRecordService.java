package com.aykuttasil.callrecord.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aykuttasil.callrecord.CallRecord;
import com.aykuttasil.callrecord.helper.PrefsHelper;

/**
 * \brief Регистрация звонков.
 * \author WonderWorcer
 * \version 0.5
 * \date 7 марта 2017 года
 *
 * Класс, реализующий сервис записи разговора
 * На данный момент не работает
 */


public class CallRecordService extends Service {

    private static final String TAG = CallRecordService.class.getSimpleName();

    CallRecord mCallRecord;


    @Override
    /**
     * Дефолтный метод, на данный момент не используется
     */
    public IBinder onBind(Intent intent) {
        return null;
    }




}
