package com.example.demi.ejabberdtest.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.demi.ejabberdtest.Adapters.ChatAdapter;
import com.example.demi.ejabberdtest.Connection.ChatMessage;
import com.example.demi.ejabberdtest.Connection.CommonMethods;
import com.example.demi.ejabberdtest.Connection.MainActivity;
import com.example.demi.ejabberdtest.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Demi on 2/16/16.
 */
public class ChatView extends Fragment implements View.OnClickListener{

    private EditText msg_edittext;
    private String user1="test1",user2="emulator";
    public Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;


    public static ChatView newInstance(int page){
        ChatView fragment=new ChatView();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chat_layout,container,false);

        random=new Random();
        msg_edittext= (EditText) view.findViewById(R.id.messageEditText);
        msgListView= (ListView) view.findViewById(R.id.msgListView);

        ImageButton sendButton= (ImageButton) view.findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        //autoscroll la mesaj nou primit
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist=new ArrayList<ChatMessage>();
        chatAdapter=new ChatAdapter(getActivity(),chatlist);
        msgListView.setAdapter(chatAdapter);
        Log.d("CHATSVIEW", "ONCREATEVIEW");
        return view;

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

            MainActivity activity= (MainActivity) getActivity();
            activity.getmService().xmpp.sendMessage(chatMessage);
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
