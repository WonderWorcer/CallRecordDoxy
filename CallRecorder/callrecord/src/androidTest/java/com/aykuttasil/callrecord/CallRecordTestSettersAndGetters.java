package com.aykuttasil.callrecord;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.v7.app.AppCompatActivity;
import android.test.AndroidTestCase;

import com.aykuttasil.callrecord.helper.PrefsHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created with Android Studio
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 11.03.2017
 * Time: 12:05
 */

public class CallRecordTestSettersAndGetters {

    private CallRecord callRecord;          ///< Рабочий класс
    private Context context;                ///< Контекст приложения

    @Before
    public void setUp() throws Exception {

        /**
         * Взяли текущий контекст приложения
         * @link https://developer.android.com/reference/android/support/test/InstrumentationRegistry.html
         */
        context = InstrumentationRegistry.getContext();

    }

    @Test
    public void enableSaveFile() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        PrefsHelper.writePrefBool(context, CallRecord.PREF_SAVE_FILE, false);
        callRecord.enableSaveFile();
        assertTrue(callRecord.getStateSaveFile());
    }

    @Test
    public void disableSaveFile() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        PrefsHelper.writePrefBool(context, CallRecord.PREF_SAVE_FILE, true);
        callRecord.disableSaveFile();
        assertFalse(callRecord.getStateSaveFile());
    }

    @Test
    public void getStateSaveFile() throws Exception {
        /* ложь */
        CallRecord callRecord = new CallRecord.Builder(context).build();
        PrefsHelper.writePrefBool(context, CallRecord.PREF_SAVE_FILE, false);
        assertFalse(callRecord.getStateSaveFile());

        /* истина */
        callRecord = new CallRecord.Builder(context).build();
        PrefsHelper.writePrefBool(context, CallRecord.PREF_SAVE_FILE, true);
        assertTrue(callRecord.getStateSaveFile());
    }

    @Test
    public void changeRecordFileName() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).setRecordFileName("FileName").build();
        callRecord.changeRecordFileName("newFileName");
        assertEquals("newFileName", callRecord.getRecordFileName());
    }

    /* Проверка на null */
    @Test(expected = Exception.class)
    public void changeRecordFileNameCheckNull() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        callRecord.changeRecordFileName(null);
    }

    /* Проверка на Empty */
    @Test(expected = Exception.class)
    public void changeRecordFileNameCheckEmpty() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        callRecord.changeRecordFileName("");
    }

    @Test
    public void getRecordFileName() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).setRecordFileName("FileName").build();
        assertEquals("FileName", callRecord.getRecordFileName());
    }

    @Test
    public void changeRecordDirName() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).setRecordDirName("DirName").build();
        callRecord.changeRecordDirName("newDirName");
        assertEquals("newDirName", callRecord.getRecordDirName());
    }

    /* Проверка на null */
    @Test(expected = Exception.class)
    public void changeRecordDirNameCheckNull() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        callRecord.changeRecordDirName(null);
    }

    /* Проверка на Empty */
    @Test(expected = Exception.class)
    public void changeRecordDirNameCheckheckEmpty() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        callRecord.changeRecordDirName("");
    }

    @Test
    public void getRecordDirName() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).setRecordDirName("DirName").build();
        assertEquals("DirName", callRecord.getRecordDirName());
    }

    @Test
    public void changeRecordDirPath() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).setRecordDirPath("DirPath").build();
        callRecord.changeRecordDirPath("newDirPath");
        assertEquals("newDirPath", callRecord.getRecordDirPath());
    }

    /* Проверка на null */
    @Test(expected = Exception.class)
    public void changeRecordDirPathCheckNull() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        callRecord.changeRecordDirPath(null);
    }

    /* Проверка на Empty */
    @Test(expected = Exception.class)
    public void changeRecordDirPathCheckEmpty() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).build();
        callRecord.changeRecordDirPath("");
    }

    @Test
    public void getRecordDirPath() throws Exception {
        CallRecord callRecord = new CallRecord.Builder(context).setRecordDirPath("DirPath").build();
        assertEquals("DirPath", callRecord.getRecordDirPath());
    }

}