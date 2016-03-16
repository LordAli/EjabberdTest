package com.example.demi.ejabberdtest.Connection;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.demi.ejabberdtest.Adapters.SampleFragmentPagerAdapter;
import com.example.demi.ejabberdtest.R;

public class MainActivity extends AppCompatActivity {


    private MyService mService;

    private final ServiceConnection mConnection=new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            mService = ((LocalBinder<MyService>) service).getService();
            Log.d("MAIN", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MAIN", "onServiceDisconnected");
             mService=null;
        }
    };



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        startService(new Intent(MainActivity.this,MyService.class));

        setContentView(R.layout.activity_main);

        dobindService();

        ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);
        SampleFragmentPagerAdapter pagerAdapter=new SampleFragmentPagerAdapter(getSupportFragmentManager(),MainActivity.this);

        //pastreaza starea fragmetului
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout= (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for(int i=0;i<tabLayout.getTabCount();i++){
            TabLayout.Tab tab=tabLayout.getTabAt(i);

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

    public MyService getmService(){
        return mService;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
         if(mConnection!=null){
            doUnbindService();
        }
    }


}



