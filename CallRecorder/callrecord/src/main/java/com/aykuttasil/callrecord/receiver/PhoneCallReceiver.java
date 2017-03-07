package com.aykuttasil.callrecord.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.aykuttasil.callrecord.CallRecord;

import java.util.Date;

/**
 * \brief Регистрация звонков.
 * \author WonderWorcer
 * \version 0.5
 * \date 7 марта 2017 года
 *
 * Абстрактный класс, необходим для обработки всех
 * возможных событий при регистрации вызовов
 * Так же обрабатывает всевозможное изменение статуса вызова
 */

public abstract class PhoneCallReceiver extends BroadcastReceiver {


    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    private CallRecord mCallRecord;

    /**
     * Конструктор
     * @param callRecord
     */
    public PhoneCallReceiver(CallRecord callRecord) {
        this.mCallRecord = callRecord;
    }

    /**
     * Необходимо для проверки есть ли новый исходящий вызов,
     * если таковой имеется запишем номер, иначе запишем номер
     * текущего вызова, его статус
     * @param context Текущий контект приложения
     * @param intent описывает операцию, которую требуется запустить, а также содержит все остальные необходимые данные.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals(CallRecordReceiver.ACTION_OUT)) {

            savedNumber = intent.getExtras().getString(CallRecordReceiver.EXTRA_PHONE_NUMBER);

        } else {

            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);

            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            savedNumber = number;

            int state = 0;

            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);
        }
    }

    /**
     * Вызывается при принятии входящего вызова
     * На данный момент не работает
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     * На данный момент не работает
     */
    //Derived classes should override these to respond to specific events of interest
    protected abstract void onIncomingCallReceived(Context ctx, String number, Date start);

    /**
     * Вызывается при приеме входящего вызова с последующим стартом записи разговора, если такой режим включен
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     */
    protected abstract void onIncomingCallAnswered(Context ctx, String number, Date start);

    /**
     * Вызывается после окончания входящего вызова, необходим для остановки записи.
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     * @param end время окончания вызова
     */
    protected abstract void onIncomingCallEnded(Context ctx, String number, Date start, Date end);

    /**
     * Вызывается при приеме исходящего вызова с последующим стартом записи разговора, если такой режим включен
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     */
    protected abstract void onOutgoingCallStarted(Context ctx, String number, Date start);

    /**
     * Вызывается после окончания исходящего вызова, необходим для остановки записи.
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     * @param end время окончания вызова
     */
    protected abstract void onOutgoingCallEnded(Context ctx, String number, Date start, Date end);
    /**
     * Возникает при пропуске звонка
     * На данный момент не работает
     * @param ctx контект приложения
     * @param number номер телефона
     * @param start время принятия вызова
     */
    protected abstract void onMissedCall(Context ctx, String number, Date start);


    /**
     * Вызывается при смене статуса звонка
     * @param context текущий контекст приложения
     * @param state статус звонка
     *  0 - конец звонка (CALL_STATE_IDLE)
     *  1 - звонок (CALL_STATE_RINGING)
     *  2 - звонок на удержании (CALL_STATE_OFFHOOK)
     *  Остальные варианты статуса в программе не присутствуют
     * @param number номер телефона
     */
    public void onCallStateChanged(Context context, int state, String number) {

        if (lastState == state) {
            //No change, debounce extras
            return;
        }

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:

                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;

                onIncomingCallReceived(context, number, callStartTime);

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {

                    isIncoming = false;
                    callStartTime = new Date();

                    onOutgoingCallStarted(context, savedNumber, callStartTime);

                } else {

                    isIncoming = true;
                    callStartTime = new Date();

                    onIncomingCallAnswered(context, savedNumber, callStartTime);

                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:

                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss

                    onMissedCall(context, savedNumber, callStartTime);

                } else if (isIncoming) {

                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());

                } else {

                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    
                }
                break;
        }

        lastState = state;
    }
}