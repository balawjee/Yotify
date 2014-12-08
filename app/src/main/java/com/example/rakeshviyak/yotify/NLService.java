package com.example.rakeshviyak.yotify;

/**
 * Created by rakeshviyak on 12/6/2014.
 */
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static String message_text="";
    public static String message_title="";

    private NLServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification yNotification=sbn.getNotification();
        Bundle nBundle=yNotification.extras;
        message_title=nBundle.getString(EXTRA_TITLE);
        message_text=nBundle.getString(EXTRA_TEXT);
        if(message_text==null)
        {
            message_text=nBundle.getString(EXTRA_SUB_TEXT);
            if(message_text==null)
            {
                if(yNotification.tickerText!=null)
                message_text=yNotification.tickerText.toString();
            }
        }
        Bitmap app_icon=yNotification.largeIcon;//(Bitmap)nBundle.getParcelable(EXTRA_LARGE_ICON);
//        Context context=getBaseContext();
//        Context remotePackageContext = null;
//        try {
//            remotePackageContext = context.createPackageContext(sbn.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        Drawable icon = remotePackageContext.getResources().getDrawable(app_icon);
        Date date=new Date(sbn.getPostTime());
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateFormatted = formatter.format(date);
        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
        Intent i = new  Intent("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event_title",message_title);
        Log.d("qwerwqr",message_title);
        i.putExtra("notification_event_text",message_text);
        i.putExtra("notification_event_icon",app_icon);
        i.putExtra("notification_event_time",dateFormatted);
        i.putExtra("notification_package_name",sbn.getPackageName());
        //i.putExtra("notification_tagNo",sbn.getId());
        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Notification yNotification=sbn.getNotification();
        Bundle nBundle=yNotification.extras;
        String message_title="\n" + nBundle.getString(EXTRA_TITLE);
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText +"t" + sbn.getPackageName());
        Intent i = new  Intent("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_EXAMPLE");
        //i.putExtra("notification_event_title","onNotificationRemoved :" + message_title);
        //sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Notification yNotification=sbn.getNotification();
                    Bundle nBundle=yNotification.extras;
                    String message="\n" + nBundle.getString(EXTRA_TITLE) + "\n" + nBundle.getString(EXTRA_TEXT) + "\n";
                    Intent i2 = new  Intent("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event_text",i +" " + message);
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);

            }

        }


    }


}
