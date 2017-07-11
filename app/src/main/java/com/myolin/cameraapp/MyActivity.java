package com.myolin.cameraapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MyActivity extends AppCompatActivity {

    ReadAsset asset;
    MyPrefs myPrefs;
    Fragment apiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        asset = new ReadAsset(getApplicationContext());
        myPrefs = new MyPrefs(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        ArrayList<String> s = asset.readColumn(1);
        ArrayList<Integer> num = asset.rowsInFrag(1);

        int start = 4;
        int end;
        for(int i=0; i<s.size(); i++){
            end = start+num.get(i);
            CreateFragment f = new CreateFragment();
            actionBar.addTab(actionBar.newTab().setText(s.get(i)).setTabListener(new MyTabsListener(f,getApplicationContext(), num.get(i), start, end)));
            start = end;
        }

        ActionBar.Tab apiTab = actionBar.newTab().setText("API");
        apiFragment = new APIFragment();
        apiTab.setTabListener(new MyTabsListener(apiFragment, getApplicationContext(),0,0,0));
        actionBar.addTab(apiTab);
    }

    class MyTabsListener implements ActionBar.TabListener {
        public Fragment fragment;
        Bundle bundle = new Bundle();

        public MyTabsListener(Fragment f, Context context, int row, int start, int end) {
            fragment = f;
            bundle.putInt("Rows", row);
            bundle.putInt("Start", start);
            bundle.putInt("End", end);
            bundle.putParcelable("Asset", asset);
            bundle.putParcelable("myPrefs", myPrefs);
            fragment.setArguments(bundle);
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }
}
