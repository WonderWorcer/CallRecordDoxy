package com.aykuttasil.callrecord;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created with Android Studio
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 11.03.2017
 * Time: 16:08
 */
public class CallRecordTestLogic {

    private CallRecord callRecord;          ///< Рабочий класс
    private Context context;                ///< Контекст приложения

    String fileName = "TestFile";
    String dirName =  "TestDir";
    private final int audioSource = MediaRecorder.AudioSource.MIC;
    private final int outputFormat = MediaRecorder.OutputFormat.AMR_NB;
    private final int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;


    @Before
    public void setUp() throws Exception {
        /**
         * Взяли текущий контекст приложения
         * @link https://developer.android.com/reference/android/support/test/InstrumentationRegistry.html
         */
        context = InstrumentationRegistry.getContext();


        /* Инициализируем CallRecord */
        callRecord = new CallRecord.Builder(context)
                .setRecordFileName(fileName)
                .setRecordDirName(dirName)
                .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()   )
                .setAudioEncoder(outputFormat)
                .setOutputFormat(audioEncoder)
                .setAudioSource(audioSource)
                .setShowSeed(true)
                .build();

        /* инициализируем сервис */
        callRecord.startCallRecordService();
    }

    @Test
    public void testMainLogic() throws Exception {

        //Начинаем запись
        callRecord.startCallReceiver();

        //Ждём чуточек
        Thread.sleep(200);

        //заканчиваем запись
        callRecord.stopCallReceiver();

        // получаем путь к файлу
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dirName);
        if(outputFormat == MediaRecorder.OutputFormat.AMR_NB) {
            fileName += ".amr";
        }
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(path, fileName);

        if(!sdFile.exists()){
            throw new Exception("The file was not created!");
        }

        if( outputFormat == MediaRecorder.OutputFormat.AMR_NB && !sdFile.getName().contains(".amr")){
            throw new Exception("The file extinsion must contain \".amr\" !");
        }

        //проверки закончены удаляем тестовый файл
        sdFile.delete();
    }

}