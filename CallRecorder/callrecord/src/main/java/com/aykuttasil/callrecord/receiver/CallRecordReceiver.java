package com.aykuttasil.callrecord.receiver;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.aykuttasil.callrecord.CallRecord;
import com.aykuttasil.callrecord.helper.PrefsHelper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * \brief Регистрация звонков.
 * \author WonderWorcer
 * \version 0.5
 * \date 7 марта 2017 года
 *
 * Класс, необходим для обработки всех возможных событий при регистрации вызовов
 */

public class CallRecordReceiver extends PhoneCallReceiver {


    private static final String TAG = CallRecordReceiver.class.getSimpleName();

    public static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    public static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";

    private static MediaRecorder recorder;
    private File audiofile;
    private boolean isRecordStarted = false; ///< флаг запуска записи

    /**
     * Конструктор
     * @param callRecord
     */
    public CallRecordReceiver(CallRecord callRecord) {
        super(callRecord);

    }

    /**
     * Вызывается при принятии входящего вызова
     * На данный момент не работает
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     * На данный момент не работает
     */
    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {

    }

    /**
     * Вызывается при приеме входящего вызова с последующим стартом записи разговора, если такой режим включен
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     */
    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        startRecord(ctx, "incoming", number);
    }

    /**
     * Вызывается после окончания входящего вызова, необходим для остановки записи.
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     * @param end время окончания вызова
     */
    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

        if (recorder != null && isRecordStarted) {

            recorder.stop();
            recorder.reset();
            recorder.release();

            isRecordStarted = false;

            Log.i(TAG, "record stop");
        }
    }

    /**
     * Вызывается при приеме исходящего вызова с последующим стартом записи разговора, если такой режим включен
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     */
    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        startRecord(ctx, "outgoing", number);
    }

    /**
     * Вызывается после окончания исходящего вызова, необходим для остановки записи.
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     * @param end время окончания вызова
     */
    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {

        if (recorder != null && isRecordStarted) {

            recorder.stop();
            recorder.reset();
            recorder.release();

            isRecordStarted = false;

            Log.i(TAG, "record stop");
        }
    }

    /**
     * Возникает при пропуске звонка
     * На данный момент не работает
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     */
    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

    }

    /**
     *
     * @param context контект приложения
     * @param seed
     * @param phoneNumber номер телефона
     */
    private void startRecord(Context context, String seed, String phoneNumber) {

        try {

            boolean isSaveFile = PrefsHelper.readPrefBool(context, CallRecord.PREF_SAVE_FILE);
            Log.i(TAG, "isSaveFile: " + isSaveFile);


            if (!isSaveFile) {
                return;
            }

            String file_name = PrefsHelper.readPrefString(context, CallRecord.PREF_FILE_NAME);
            String dir_path = PrefsHelper.readPrefString(context, CallRecord.PREF_DIR_PATH);
            String dir_name = PrefsHelper.readPrefString(context, CallRecord.PREF_DIR_NAME);
            boolean show_seed = PrefsHelper.readPrefBool(context, CallRecord.PREF_SHOW_SEED);
            boolean show_phone_number = PrefsHelper.readPrefBool(context, CallRecord.PREF_SHOW_PHONE_NUMBER);
            int output_format = PrefsHelper.readPrefInt(context, CallRecord.PREF_OUTPUT_FORMAT);
            int audio_source = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_SOURCE);
            int audio_encoder = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_ENCODER);

            File sampleDir = new File(dir_path + "/" + dir_name);

            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }


            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(file_name);
            fileNameBuilder.append("_");

            if (show_seed) {
                fileNameBuilder.append(seed);
                fileNameBuilder.append("_");
            }

            if (show_phone_number) {
                fileNameBuilder.append(phoneNumber);
                fileNameBuilder.append("_");
            }


            file_name = fileNameBuilder.toString();

            String suffix = "";
            switch (output_format) {
                case MediaRecorder.OutputFormat.AMR_NB: {
                    suffix = ".amr";
                    break;
                }
                case MediaRecorder.OutputFormat.AMR_WB: {
                    suffix = ".amr";
                    break;
                }
                case MediaRecorder.OutputFormat.MPEG_4: {
                    suffix = ".mp4";
                    break;
                }
                case MediaRecorder.OutputFormat.THREE_GPP: {
                    suffix = ".3gp";
                    break;
                }
                default: {
                    suffix = ".amr";
                    break;
                }
            }

            audiofile = File.createTempFile(file_name, suffix, sampleDir);

            recorder = new MediaRecorder();
            recorder.setAudioSource(audio_source);
            recorder.setOutputFormat(output_format);
            recorder.setAudioEncoder(audio_encoder);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            recorder.prepare();

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        recorder.start();

        isRecordStarted = true;

        Log.i(TAG, "record start");
    }

}
