package com.example.demi.ejabberdtest.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demi.ejabberdtest.Connection.ChatMessage;
import com.example.demi.ejabberdtest.R;

import java.util.ArrayList;


/**
 * Created by Demi on 11/18/15.
 */
public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    ArrayList<ChatMessage> chatMessagelist;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> list) {

        chatMessagelist = list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatMessagelist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = chatMessagelist.get(position);
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.chatbubble, null);
        }
            TextView msg = (TextView) vi.findViewById(R.id.message_text);
            msg.setText(message.body);
            LinearLayout layout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
            LinearLayout parent_layout = (LinearLayout) vi.findViewById(R.id.bubble_layout_parent);

            if (message.isMine) {
                layout.setBackgroundResource(R.drawable.bubble2);
                parent_layout.setGravity(Gravity.RIGHT);
            } else {
                layout.setBackgroundResource(R.drawable.bubble1);
                parent_layout.setGravity(Gravity.LEFT);
            }
            msg.setTextColor(Color.BLACK);
            return vi;

        }

    public void add(ChatMessage object) {
        chatMessagelist.add(object);
    }
}

