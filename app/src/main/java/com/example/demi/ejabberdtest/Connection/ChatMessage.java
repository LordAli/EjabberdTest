package com.example.demi.ejabberdtest.Connection;

import java.util.Random;

/**
 * Created by Demi on 11/18/15.
 */
public class ChatMessage {

    public String body,sender,receiver,senderName;
    public String Date,Time;
    public String msgid;
    public boolean isMine;

    public ChatMessage(String Sender, String Receiver, String messageString, String ID, boolean IsMINE){
        body=messageString;
        isMine=IsMINE;
        sender=Sender;
        msgid=ID;
        receiver=Receiver;
        senderName=sender;

    }
    public void setMsgID(){
        msgid += "-" + String.format("%02d", new Random().nextInt(100));
    }
}