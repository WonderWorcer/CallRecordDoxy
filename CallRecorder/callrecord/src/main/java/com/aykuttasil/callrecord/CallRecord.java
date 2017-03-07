package com.aykuttasil.callrecord;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.aykuttasil.callrecord.helper.PrefsHelper;
import com.aykuttasil.callrecord.receiver.CallRecordReceiver;
import com.aykuttasil.callrecord.service.CallRecordService;

/**
 * \brief Регистрация звонков.
 * \author WonderWorcer
 * \version 0.5
 * \date 7 марта 2017 года
 *
 * Класс, реализующий регистрацию звонков
 */

public class CallRecord {

    private static final String TAG = CallRecord.class.getSimpleName();

    public static final String PREF_SAVE_FILE = "PrefSaveFile";///<константа для сохранения файла
    public static final String PREF_FILE_NAME = "PrefFileName";///<константа для сохранения имени файла
    public static final String PREF_DIR_NAME = "PrefDirName";///<константа для сохранения имени директории
    public static final String PREF_DIR_PATH = "PrefDirPath";///<константа для сохранения пути до директории
    public static final String PREF_SHOW_SEED = "PrefShowSeed";///<константа, непанятна зачем нужна
    public static final String PREF_SHOW_PHONE_NUMBER = "PrefShowPhoneNumber";///<константа для отображения номера телефона
    public static final String PREF_AUDIO_SOURCE = "PrefAudioSource";///<константа для задания источника записи
    public static final String PREF_AUDIO_ENCODER = "PrefAudioEncoder";///<константа для задания аудио кодека
    public static final String PREF_OUTPUT_FORMAT = "PrefOutputFormat";///<константа для задания разрешения выходного файла

    /**
     * Context это объект, который предоставляет доступ
     * к базовым функциям приложения: доступ к ресурсам,
     * к файловой системе, вызов активности и т.д.
     */
    private Context mContext;

    private CallRecordReceiver mCallRecordReceiver;

    /**
     * Коструктор
     * Присваивает текущий котекст
     * @param context текущий контекст приложения
     */
    private CallRecord(Context context) {
        this.mContext = context;
    }

    /**
     * Необходим для инициализации ресивера
     * В данный момент не используется
     * @param context текущий контекст приложения
     * @return Возвращает
     */
    public static CallRecord initReceiver(Context context) {

        CallRecord callRecord = new Builder(context).build();

        callRecord.startCallReceiver();

        return callRecord;
    }

    /**
     * Необходим для инициализации сервиса записи разговора
     * В данный момент не используется
     * @param context текущий контекст приложения
     * @return объект, реализующий регистрацию вызовов, с присвоенныны
     * ему значениями контекста
     */
    public static CallRecord initService(Context context) {

        CallRecord callRecord = new Builder(context).build();

        callRecord.startCallRecordService();

        return callRecord;
    }

    /**
     * Необходим для включения записи разговора
     */
    public void startCallReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CallRecordReceiver.ACTION_IN);
        intentFilter.addAction(CallRecordReceiver.ACTION_OUT);

        if (mCallRecordReceiver == null) {

            mCallRecordReceiver = new CallRecordReceiver(this);
        }

        mContext.registerReceiver(mCallRecordReceiver, intentFilter);
    }

    /**
     * Необходим для отключения записи разговора
     */
    public void stopCallReceiver() {

        try {

            mContext.unregisterReceiver(mCallRecordReceiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Необходим для старта серивисов записи разговора
     */
    public void startCallRecordService() {

        Intent intent = new Intent();
        intent.setClass(mContext, CallRecordService.class);

        mContext.startService(intent);

        Log.i(TAG, "startService()");
    }

    /**
     * Включить сохрание файлов записи
     */
    public void enableSaveFile() {

        PrefsHelper.writePrefBool(mContext, PREF_SAVE_FILE, true);

        Log.i("CallRecord", "Save file enabled");
    }

    /**
     * Выключить сохранение файлов записи
     */
    public void disableSaveFile() {

        Log.i("CallRecord", "Save file disabled");

        PrefsHelper.writePrefBool(mContext, PREF_SAVE_FILE, false);
    }

    /**
     * Необходим для полчения состояния сохранения файлов
     * @return возвращает true - если файлы сохраняются, false - не сохраняются
     */
    public boolean getStateSaveFile() {
        return PrefsHelper.readPrefBool(mContext, PREF_SAVE_FILE);
    }

    /**
     * Необходим для смены имени записываемого файла
     * @param newFileName новое имя файла
     */
    public void changeRecordFileName(String newFileName) {

        if (newFileName == null || newFileName.isEmpty()) {

            try {
                throw new Exception("newFileName can not be empty or null");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        PrefsHelper.writePrefString(mContext, PREF_FILE_NAME, newFileName);

        Log.i("CallRecord", "New file name: " + newFileName);
    }

    /**
     * Необходим для получение имени записываемого файла
     * @return возвращает имя записываемого файла
     */
    public String getRecordFileName() {

        return PrefsHelper.readPrefString(mContext, PREF_FILE_NAME);
    }

    /**
     * Необходим для смены директории сохранения файлов
     * @param newDirName - новое имя директории
     */
    public void changeRecordDirName(String newDirName) {

        if (newDirName == null || newDirName.isEmpty()) {

            try {
                throw new Exception("newDirName can not be empty or null");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        PrefsHelper.writePrefString(mContext, PREF_DIR_NAME, newDirName);

        Log.i("CallRecord", "New dir name: " + newDirName);
    }

    /**
     * Необходим для получения имени директории сохранения файлов
     * @return возвращает имя директории
     */
    public String getRecordDirName() {

        return PrefsHelper.readPrefString(mContext, PREF_DIR_NAME);
    }

    /**
     * Необходим для изменения директории хранения новых файлов
     * @param newDirPath новый путь
     */
    public void changeRecordDirPath(String newDirPath) {

        if (newDirPath == null || newDirPath.isEmpty()) {

            try {
                throw new Exception("newDirPath can not be empty or null");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        PrefsHelper.writePrefString(mContext, PREF_DIR_PATH, newDirPath);

        Log.i("CallRecord", "New dir path: " + newDirPath);
    }

    /**
     * Необходим для определения директории хранения файлов
     * @return путь для хранения файлов
     */
    public String getRecordDirPath() {

        return PrefsHelper.readPrefString(mContext, PREF_DIR_PATH);
    }

    /**
     * Необходим для смены способа записи файлов
     * @param receiver объект, наследник BroadcastReceiver,
     * необходим для реализации всевозможных событий,
     * возникающих во время разговора
     */
    public void changeReceiver(CallRecordReceiver receiver) {
        mCallRecordReceiver = receiver;
    }

    /**
     * Необходим для создания объекта класса CallRecord,
     * реализуя его функционал по частям
     */
    public static class Builder {

        private Context mContext;

        /**
         * Инициализирует всевозможные функции приложения,
         * записывает все данные в контект приложения
         * @param context Текущий контекст приложения
         */
        public Builder(Context context) {

            this.mContext = context;

            PrefsHelper.writePrefString(mContext, PREF_FILE_NAME, "Record");
            PrefsHelper.writePrefString(mContext, PREF_DIR_NAME, "CallRecord");
            PrefsHelper.writePrefString(mContext, PREF_DIR_PATH, Environment.getExternalStorageDirectory().getPath());
            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_ENCODER, MediaRecorder.AudioEncoder.AMR_NB);
            PrefsHelper.writePrefInt(mContext, PREF_OUTPUT_FORMAT, MediaRecorder.OutputFormat.AMR_NB);
            PrefsHelper.writePrefBool(mContext, PREF_SHOW_SEED, true);
            PrefsHelper.writePrefBool(mContext, PREF_SHOW_PHONE_NUMBER, true);

        }

        /**
         * Необходим для сборки объекта CallRecord
         * @return объект класса, реализующий регистрацию вызовов
         */
        public CallRecord build() {

            CallRecord callRecord = new CallRecord(mContext);

            callRecord.enableSaveFile();

            return callRecord;
        }

        /**
         * Необходим для задания имени записанного файла
         * @param recordFileName название файла
         * @return экземляр билдера
         */
        public Builder setRecordFileName(String recordFileName) {

            PrefsHelper.writePrefString(mContext, PREF_FILE_NAME, recordFileName);

            return this;
        }



        /**
         * необходим для задания имени директории хранения файла
         * @param recordDirName Имя директории
         * @return экземляр билдера
         */
        public Builder setRecordDirName(String recordDirName) {

            PrefsHelper.writePrefString(mContext, PREF_DIR_NAME, recordDirName);

            return this;
        }



        /**
         * Необходим для получения источника записи
         * @return параметр, характеризующий источник записи
         * 0 - по умолчанию
         * 1 - микрофон
         * 2 - UPLINK
         * 3 - DOWNLINK
         * 4 - разговор
         * 5 - микрофон с камеры, если нету такого, используется обычный микрофон
         * 7 - VoIp, эхоподавляющий, автоматически усиливающий.
         * 8 - Субмикс аудиопоток
         * 9 - сырой звук(работает так же как и по умолчанию)
         */
        public int getAudioSource() {

            return PrefsHelper.readPrefInt(mContext, PREF_AUDIO_SOURCE);
        }

        /**
         * Необходим для задания способа записи разговора
         * @param audioSource - источник записи
         * 0 - по умолчанию
         * 1 - микрофон
         * 2 - UPLINK
         * 3 - DOWNLINK
         * 4 - разговор
         * 5 - микрофон с камеры, если нету такого, используется обычный микрофон
         * 7 - VoIp, эхоподавляющий, автоматически усиливающий.
         * 8 - Субмикс аудиопоток
         * 9 - сырой звук(работает так же как и по умолчанию)
         * @return экземляр билдера
         */
        public Builder setAudioSource(int audioSource) {

            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_SOURCE, audioSource);

            return this;
        }

        /**
         * Получает кодек файла
         * 0 - по умолчанию
         * 1- 3GPP
         * 2- MPEG4
         * 3- AMR NB
         * 4 - AMR WB
         * 6 - AAC ADTS
         * 9 - VP8/VORBIS
         * @return параметр, характеризующий кодек файла
         */
        public int getAudioEncoder() {

            return PrefsHelper.readPrefInt(mContext, PREF_AUDIO_ENCODER);
        }

        /**
         * Необходим для задания кодека файла
         * @param audioEncoder номер кодека
         * 0 - по умолчанию
         * 1- 3GPP
         * 2- MPEG4
         * 3- AMR NB
         * 4 - AMR WB
         * 6 - AAC ADTS
         * 9 - VP8/VORBIS
         * @return экземляр билдера
         */
        public Builder setAudioEncoder(int audioEncoder) {

            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_ENCODER, audioEncoder);

            return this;
        }

        /**
         * Необходим для получения выходного формата файла
         * 0 - по умолчанию
         * 1- 3GPP
         * 2- MPEG4
         * 3- AMR NB
         * 4 - AMR WB
         * 6 - AAC ADTS
         * 9 - VP8/VORBIS
         * @return номер, характеризующий формат файла
         */
        public int getOutputFormat() {

            return PrefsHelper.readPrefInt(mContext, PREF_OUTPUT_FORMAT);
        }

        /**
         * необходим для задания формата сохраняемого файла
         * @param outputFormat номер формата сохраняемого файла
         * 0 - по умолчанию
         * 1 - 3GPP
         * 2 - MPEG4
         * 3 - AMR NB
         * 4 - AMR WB
         * 6 - AAC ADTS
         * 9 - VP8/VORBIS
         * @return экземляр билдера
         */
        public Builder setOutputFormat(int outputFormat) {

            PrefsHelper.writePrefInt(mContext, PREF_OUTPUT_FORMAT, outputFormat);

            return this;
        }

        /**
         * Проверяет неведомый параметр
         * @return не понятно что
         */
        public boolean isShowSeed() {

            return PrefsHelper.readPrefBool(mContext, PREF_SHOW_SEED);
        }

        /**
         * Не имею понятия что делает функция
         * @param showSeed неведомый параметр имеющий значение true и false
         * @return экземляр билдера
         */

        public Builder setShowSeed(boolean showSeed) {

            PrefsHelper.writePrefBool(mContext, PREF_SHOW_SEED, showSeed);

            return this;
        }

        /**
         * Необходим для проверки показывается ли номер телефона
         * @return true - номер показыватся, false - не показывается
         */
        public boolean isShowPhoneNumber() {

            return PrefsHelper.readPrefBool(mContext, PREF_SHOW_PHONE_NUMBER);
        }

        /**
         * Необходим для показа номера телефона
         * @param showNumber true - включить, false - выключить
         * @return экземляр билдера
         */
        public Builder setShowPhoneNumber(boolean showNumber) {

            PrefsHelper.writePrefBool(mContext, PREF_SHOW_PHONE_NUMBER, showNumber);

            return this;
        }


        /**
         * необходим для задания пули хранения файла
         * @param recordDirPath путь , куда будет сохранен файл
         * @return экземляр билдера
         */
        public Builder setRecordDirPath(String recordDirPath) {

            PrefsHelper.writePrefString(mContext, PREF_DIR_PATH, recordDirPath);

            return this;
        }

    }
}
