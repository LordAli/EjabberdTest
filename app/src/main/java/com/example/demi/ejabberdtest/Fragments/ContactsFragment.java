package com.example.demi.ejabberdtest.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.demi.ejabberdtest.Adapters.RecAdapter;
import com.example.demi.ejabberdtest.Connection.MainActivity;
import com.example.demi.ejabberdtest.Connection.MyService;
import com.example.demi.ejabberdtest.R;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Demi on 3/3/16.
 */
public class ContactsFragment extends Fragment  {

     List<RosterEntry> contactList;
     RecyclerView recyclerView;
     RecAdapter recAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Log.d("CONTACTSFRAGMENT", "ONCREATE");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.contacts_fragment, container, false);

        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CONTACTSFRAGMENT", "ONSTART");
        super.onStart();
        IntentFilter intentFilter=new IntentFilter("ServiceConnected");
        getActivity().registerReceiver(br, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CONTACTSFRAGMENT", "ONRESUME");

        if(MyService.ServerChatCreated){
        test();
    }
    }


    public void startChat(int position){
        Intent i=new Intent("com.example.demi.ejabberdtest.STARTCHAT");
        i.setClassName("com.example.demi.ejabberdtest","com.example.demi.ejabberdtest.Fragments.Chat");
        i.putExtra("participant",this.contactList.get(position).getUser());
        startActivity(i);
    }

    public void test(){
        MainActivity activity= (MainActivity) getActivity();
        Collection<RosterEntry> collectionRosters= activity.getmService().xmpp.friendList();
        contactList=new ArrayList<RosterEntry>(collectionRosters);

        for(RosterEntry entry :collectionRosters){
            Log.d("STATUS", String.valueOf(entry.getStatus()));
        }
        try {
            activity.getmService().xmpp.loadVcard("emulator@macbookpro");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }

        Log.d("ContactsFragment", String.valueOf(contactList));

        //adauga lista in adapter
        recAdapter=new RecAdapter(contactList,getActivity());
        recyclerView.setAdapter(recAdapter);

       recAdapter.SetOnItemClickListener(new RecAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {
               Toast.makeText(getActivity(), "CTFRAG User: " +contactList.get(position) ,Toast.LENGTH_SHORT).show();
               startChat(position);
           }
       });

    }

    private BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        test();
        }
    };

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(br);
        super.onStop();
    }


}

