package com.example.demi.ejabberdtest.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.demi.ejabberdtest.Fragments.ChatView;
import com.example.demi.ejabberdtest.Fragments.ContactsFragment;
import com.example.demi.ejabberdtest.Fragments.MapFragment;

/**
 * Created by Demi on 2/16/16.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT=3;
    private String tabTitles[]=new String[]{"Chats","Contacts","Map"};
    private Context context;


    public SampleFragmentPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
       if(position==0){
          // return ChatView.newInstance(position);
       }
        if(position==1){
         return new ContactsFragment();
        }else{
            return new MapFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

