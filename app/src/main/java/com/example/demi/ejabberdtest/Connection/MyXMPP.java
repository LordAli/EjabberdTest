package com.example.demi.ejabberdtest.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.demi.ejabberdtest.Fragments.ChatView;
import com.example.demi.ejabberdtest.R;
import com.google.gson.Gson;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Demi on 2/18/16.
 */
public class MyXMPP {

    public connectionListener cn;
    public void setConnection(connectionListener cn){
        this.cn=cn;
    }

    private String HOST="192.168.1.107";
    public static boolean connected=false;
    public boolean loggedin=false;
    public static boolean isConnecting=false;
    public static boolean  isToasted=false;
    public static boolean chat_created=false;
    private String serverAdress;
    public static String loginUser;
    public static String passwordUser;
    Gson gson;

     public static XMPPTCPConnection connection;
     MyService context;
     public static MyXMPP instance= null;
     public static boolean instanceCreated= false;

    private Roster mRoster;

   private MyXMPP(MyService context,String serverAdress,String loginUser,String passwordUser){
       this.context=context;
       this.serverAdress=serverAdress;
       this.loginUser=loginUser;
       this.passwordUser=passwordUser;
       init();
   }

   public static MyXMPP getInstance(MyService context,String server,String user,String password){
           if(instance==null){
               instance=new MyXMPP(context,server,user,password);
           }
       return instance;
   }

    public org.jivesoftware.smack.chat.Chat myChat;

    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;

    String text;
    String mMessage="",mReceiver="";

    static{
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        gson=new Gson();
         mMessageListener =new MMessageListener(context);
         mChatManagerListener=new ChatManagerListenerImpl();
       initialiseConnection();
    }

    private void initialiseConnection(){
        XMPPTCPConnectionConfiguration.Builder config=XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(serverAdress);
        config.setHost(HOST);
        config.setPort(5222);
        config.setDebuggerEnabled(true);
        config.setSendPresence(true);

        config.setDebuggerEnabled(true);

        connection=new XMPPTCPConnection(config.build());
        XMPPConnectionListener connectionListener=new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);



    }

    public void login() {
        try {
            connection.login(loginUser, passwordUser);

            Log.i("LOGIN", "Connected to the Xmpp server!");
           // friendList();

        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<RosterEntry> friendList(){
        Roster roster = Roster.getInstanceFor(connection);
        if(!roster.isLoaded()){
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Collection<RosterEntry> entries = roster.getEntries();
        Presence presence;
        Log.d("Lista Contacte: ", entries.toString());
        Log.d("Marime Lista: ", String.valueOf(entries.size()));

        for(RosterEntry entry : entries) {
            presence = roster.getPresence(entry.getUser());

            Log.d("MyXMPP Roster:", "User: " + entry.getUser()+"  -Name " + entry.getName());
           // Log.d("MyXMPP", "Name: " + entry.getName());
            Log.d("Precence" , presence.getType().name());

        }
        return entries;
    }

    public Roster getRoster(){
        return mRoster;

    }

    public void setVcard(String firstName,String secondName){

        String Test1FirstName="Test1FirstName";
        String Test2SecondName="Test2SecondName";
        String EmulatorFirstName="EmulatorFirstName";
        String EmulatorSecondName="EmulatorSecondName";

        VCard vCard = new VCard();
        vCard.setFirstName(firstName);
        vCard.setLastName(secondName);
        vCard.setEmailHome("emulator@mirel.com");
        vCard.setJabberId("emulator");
        vCard.setOrganization("SRL");
        vCard.setNickName("EM");
        //vCard.setField("TITLE", "Mr");
        //vCard.setAddressFieldHome("STREET", "Some street");
        //vCard.setAddressFieldWork("CTRY", "US");
        //vCard.setPhoneWork("FAX", "3443233");
        try {
            vCard.save(connection);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public String loadVcard(String user) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        VCard vCard = new VCard();
        vCard.load(connection); // load own VCard
        vCard.load(connection, user); // load someone's VCard
        Log.d("VCARD-XMPP", String.valueOf(vCard.getFirstName()) +" "+ vCard.getLastName());
        String name=vCard.getFirstName();
      return name;
    }

     public void connect(final String caller){
         AsyncTask<Void,Void,Boolean> connectionThread=new AsyncTask<Void, Void, Boolean>() {
             @Override
             protected synchronized Boolean doInBackground(Void... params) {
                 if(connection.isConnected()){
                     return false;
                 }
                 isConnecting=true;

                 try {
                     connection.connect();
                     DeliveryReceiptManager dm = DeliveryReceiptManager
                             .getInstanceFor(connection);
                     dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
                     dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                         @Override
                         public void onReceiptReceived(final String fromid,
                                                       final String toid, final String msgid,
                                                       final Stanza packet) {

                         }
                     });

                     connected=true;

                 } catch (SmackException e) {
                     e.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (XMPPException e) {
                     e.printStackTrace();
                 }

                 return isConnecting=false;
             }
         };
         connectionThread.execute();
         login();
     }

    public void sendMessage(ChatMessage chatMessage){
         String body=gson.toJson(chatMessage);
        if(!chat_created){
            myChat = ChatManager.getInstanceFor(connection).createChat(
                    chatMessage.receiver + "@"
                            + context.getString(R.string.server),
                    mMessageListener);
        chat_created=false;
        }
        final Message message=new Message();
        message.setBody(body);
        message.setStanzaId(chatMessage.msgid);
        message.setType(Message.Type.chat);

        if(connection.isAuthenticated()){
            try {
                myChat.sendMessage(message);

            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }else{
            login();
        }
    }

    private class ChatManagerListenerImpl implements ChatManagerListener{

        @Override
        public void chatCreated(Chat chat, final boolean createdLocally) {
            if(!createdLocally)
                chat.addMessageListener(mMessageListener);
        }
    }



    public class XMPPConnectionListener implements ConnectionListener{

        @Override
        public void connected(final XMPPConnection connection) {
            Log.d("XMPP", "CONNECTED");
            connected=true;
            if(!connection.isAuthenticated()){
                 login();

                //Set V Card

                setVcard("Test1Name","Test1secName");
            }
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            Log.d("xmpp", "Authenticated!");
            loggedin = true;
            ChatManager.getInstanceFor(connection).addChatListener(mChatManagerListener);
            cn.onConnected();


        }

        @Override
        public void connectionClosed() {
            connected = false;
            loggedin = false;
            Log.d("xmpp", "ConnectionCLosed!");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            Log.d("xmpp", "ConnectionClosedOn Error!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            Log.d("xmpp", "ReconnectionSuccessful");
            connected = true;
            chat_created = false;
            loggedin = false;

        }

        @Override
        public void reconnectingIn(int seconds) {
            Log.d("xmpp", "Reconnectingin " + seconds);
        }

        @Override
        public void reconnectionFailed(Exception e) {
            Log.d("xmpp", "ReconnectionFailed!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }
    }

    private class MMessageListener implements ChatMessageListener {

        public MMessageListener(Context contxt) {
        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                    + message);
            Log.d("MESSAGE","MEESAGE RECEIVED");
            if (message.getType() == Message.Type.chat
                    && message.getBody() != null) {
                final ChatMessage chatMessage = gson.fromJson(
                        message.getBody(), ChatMessage.class);
                Log.d("MESSAGE isss",chatMessage.body);
                processMessage(chatMessage);
            }
        }

        private void processMessage(final ChatMessage chatMessage) {
            chatMessage.isMine = false;

            com.example.demi.ejabberdtest.Fragments.Chat.chatlist.add(chatMessage);
            //ChatView.chatlist.add(chatMessage);

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    com.example.demi.ejabberdtest.Fragments.Chat.chatAdapter.notifyDataSetChanged();
                    //ChatView.chatAdapter.notifyDataSetChanged();

                }
            });
        }

    }

    public interface connectionListener{
        void onConnected();
}
    }

