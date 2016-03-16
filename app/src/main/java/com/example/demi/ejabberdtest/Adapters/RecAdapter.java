package com.example.demi.ejabberdtest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demi.ejabberdtest.Connection.MyService;
import com.example.demi.ejabberdtest.R;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

/**
 * Created by Demi on 3/10/16.
 */
public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecHolder>{

    private List<RosterEntry> rosterList;
    private LayoutInflater layoutInflater;

    MyService service;

    OnItemClickListener mItemClickListener;

    public RecAdapter(List<RosterEntry> rosterList,Context c ){

        this.layoutInflater=LayoutInflater.from(c);
        this.rosterList=rosterList;
    }

    @Override
    public RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.list_item,parent,false);
        return new RecHolder(view);
    }

    @Override
    public void onBindViewHolder(RecHolder holder, int position) {
     RosterEntry roster=rosterList.get(position);
     holder.title.setText(roster.getUser());

//Incarc vcard firstName din xmpp
//        try {
//            service.xmpp.loadVcard(roster.getUser());
//            holder.title.setText(service.xmpp.loadVcard(roster.getUser()));
//        } catch (SmackException.NotConnectedException e) {
//            e.printStackTrace();
//        } catch (XMPPException.XMPPErrorException e) {
//            e.printStackTrace();
//        } catch (SmackException.NoResponseException e) {
//            e.printStackTrace();
//        }

        // holder.icon.setImageResource();
    }

    @Override
    public int getItemCount() {
        return rosterList.size();
    }

    class RecHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView icon;
        private View container;

        public RecHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_text);
            icon = (ImageView) itemView.findViewById(R.id.item_icon);
            container=itemView.findViewById(R.id.container_item);
            //Click Listener


         itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (mItemClickListener != null) {
                     mItemClickListener.onItemClick(v, getAdapterPosition());
                 }
             }
         });
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}