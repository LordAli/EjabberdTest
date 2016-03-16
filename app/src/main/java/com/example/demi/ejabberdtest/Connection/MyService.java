package com.example.demi.ejabberdtest.Connection;


import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;

import org.jivesoftware.smack.chat.Chat;

/**
 * Created by Demi on 2/26/16.
 */
public class MyService extends Service {

     //setServiceName
     private static final String DOMAIN="macbookpro";
     private static final String USERNAME="test1";
     private static final String PASSWORD="user";

     public static ConnectivityManager cm;
     public static MyXMPP xmpp;
     public static boolean ServerChatCreated=false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder<MyService>(this);
    }

   public Chat chat;

    @Override
    public void onCreate() {
        super.onCreate();
    cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    xmpp=MyXMPP.getInstance(MyService.this,DOMAIN,USERNAME,PASSWORD);
    xmpp.connect("Oncreate");

    xmpp.setConnection(new MyXMPP.connectionListener() {
        @Override
        public void onConnected() {

            Intent intent=new Intent("ServiceConnected");
            Bundle bundle=new Bundle();
            bundle.putBoolean("Connected",true);
            intent.putExtras(bundle);
            sendBroadcast(intent);
            ServerChatCreated=true;
        }
    });
    }

    @Override
    public int onStartCommand(Intent intent,final int flags,final int startId) {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("XMPP")
                .setTicker("XMPP")
                .setContentText("Connection")
                .build();

        //mod
        startForeground(1,notification);
        return Service.START_STICKY;

    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp.connection.disconnect();
    }

    public static boolean isNetworkConnected(){
        return cm.getActiveNetworkInfo() !=null;
    }



}

