package com.example.demi.ejabberdtest.Fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.demi.ejabberdtest.Adapters.ChatAdapter;
import com.example.demi.ejabberdtest.Connection.ChatMessage;
import com.example.demi.ejabberdtest.Connection.CommonMethods;
import com.example.demi.ejabberdtest.Connection.LocalBinder;
import com.example.demi.ejabberdtest.Connection.MyService;
import com.example.demi.ejabberdtest.R;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Demi on 3/12/16.
 */
public class Chat  extends Activity implements View.OnClickListener{

    private EditText msg_edittext;
    private String user1="test1",user2;
    public Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;

    RosterEntry mParticipant=null;
    private MyService cService;

    private final ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            cService=((LocalBinder<MyService>) service).getService();
            Log.d("CHAT", "onServiceConnected");

            if(mConnection !=null){
                Intent i=getIntent();
                if(i!=null){
                    unBudle(i);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("CHAT", "onServiceDisconnected");
            cService=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        dobindService();

        random=new Random();
        msg_edittext= (EditText) findViewById(R.id.messageEditText);
        msgListView= (ListView) findViewById(R.id.msgListView);

        ImageButton sendButton= (ImageButton) findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        //autoscroll la mesaj nou primit
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist=new ArrayList<ChatMessage>();
        chatAdapter=new ChatAdapter(this,chatlist);
        msgListView.setAdapter(chatAdapter);

        Log.d("CHAT", "ONCREATE " +user2);


    }
    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(user1, user2, message, "" + random.nextInt(1000), true);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = CommonMethods.getCurrentDate();
            chatMessage.Time = CommonMethods.getCurrentTime();
            msg_edittext.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
            cService.xmpp.sendMessage(chatMessage);
            Log.d("SENDMESSAGE", chatMessage.receiver);
        }
    }

    public void dobindService() {
        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public void doUnbindService(){
        if(mConnection!=null){
            unbindService(mConnection);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if(mConnection!=null){
            doUnbindService();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
       Log.d("Chat","ONPAUSE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Chat", "ONResume " + user2);
    }

    private void unBudle(Intent intent){
        Bundle data=intent.getExtras();
        if(data!=null){
            String par=data.getString("participant");
            user2=par.replace("@macbookpro", "");

            // mParticipant=cService.xmpp.getRoster().getEntry(par);
            Log.d("CHATUSER",user2);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendTextMessage(v);

        }
    }
}