package com.example.rakeshviyak.yotify;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//package com.example.rakeshviyak.yotify;

public class MainActivity extends Activity {

    private TextView txtView;
    private NotificationReceiver nReceiver;
    private ListView list;
    private List<String> title = new ArrayList<String>();
    private List<String> message = new ArrayList<String>();
    private List<Bitmap> image = new ArrayList<Bitmap>();
    private List<String> time = new ArrayList<String>();
    private List<String> packageName=new ArrayList<String>();
    private List<String> tagNo=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        title.add("google");
//        title.add("apple");
//        message.add("blah");
//        message.add("arghh");
//        time.add("10:45");
//        time.add("20:56");
//        image.add(null);
//        image.add(null);
//        packageName.add(null);
//        packageName.add(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my);
        //txtView = (TextView) findViewById(R.id.textView);

        CustomList adapter = new
                CustomList(MainActivity.this, title, image,message, time,tagNo);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at "  + position, Toast.LENGTH_SHORT).show();
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(packageName.get(position));
                LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(LaunchIntent);
            }
        });

        final float[] historicX = {Float.NaN};
        final float[] historicY = {Float.NaN};
        final int DELTA = 50;
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        historicX[0] = event.getX();
                        historicY[0] = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        if (event.getX() - historicX[0] < -DELTA)
                        {
                            //DeleteEntry(v.getTag(0).toString());
                            return true;
                        }
                        else if (event.getX() - historicX[0] > DELTA)
                        {
                            //DeleteEntry(v.getTag(0).toString());
                            return true;
                        } break;
                    default: return false;
                }
                return false;
            }
        });
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void DeleteEntry(String tag) {
        //Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
        int i=tagNo.indexOf(tag);
        title.remove(i);
        image.remove(i);
        message.remove(i);
        packageName.remove(i);
        time.remove(i);
        CustomList adapter = new CustomList(MainActivity.this, title, image,message, time,tagNo);
        list.setAdapter(adapter);

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.cancel(Integer.parseInt(tag));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }


    public void buttonClicked(View v){

        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("My Notification");
            ncomp.setContentText("Notification Listener Service Example");
            ncomp.setTicker("Notification Listener Service Example");
            ncomp.setSmallIcon(R.drawable.ic_launcher);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }
        else if(v.getId() == R.id.btnClearNotify){
            Intent i = new Intent("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","clearall");
            sendBroadcast(i);
            clearNotificationList();
        }
        else if(v.getId() == R.id.btnListNotify){
            Intent i = new Intent("com.example.rakeshviyak.yotify.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","list");
            sendBroadcast(i);
        }
        else if(v.getId() == R.id.btnSend2BS){
            Intent LaunchIntent = new Intent(this,BSMainActivity.class);
            LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(LaunchIntent);
        }
    }

    private void clearNotificationList() {
        title.clear();
        image.clear();
        message.clear();
        time.clear();
        CustomList adapter = new CustomList(MainActivity.this, title, image,message, time,tagNo);
        list.setAdapter(adapter);
    }

    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
//            String temp = intent.getStringExtra("notification_event_title") + txtView.getText();
//            txtView.setText(temp);
            title.add(0,intent.getStringExtra("notification_event_title"));
            //Log.d("asdfasf",intent.getStringExtra("notification_event_title"));
            message.add(0,intent.getStringExtra("notification_event_text"));
            image.add(0,(Bitmap) intent.getParcelableExtra("notification_event_icon"));
            time.add(0,intent.getStringExtra("notification_event_time"));
            packageName.add(0,intent.getStringExtra("notification_package_name"));
            //tagNo.add(0,intent.getStringExtra("notification_tagNo"));
            CustomList adapter = new CustomList(MainActivity.this, title, image,message, time,tagNo);
            list.setAdapter(adapter);
        }
    }
}