package com.myolin.optimiser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ReadAsset asset;
    MyPrefs myPrefs;
    Fragment apiFragment;

    Fragment currentFragment = null;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        asset = new ReadAsset(getApplicationContext());
        myPrefs = new MyPrefs(getApplicationContext());

        gridView = (GridView) findViewById(R.id.gridview);
        //ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext());
        //gridView.setAdapter(imageAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.back).setTabListener(new MyListener(gridView)));
        actionBar.addTab(actionBar.newTab().setText("").setTabListener(new MyListener2()));
        actionBar.setSelectedNavigationItem(1);

        ArrayList < String > s = asset.readColumn(1);
        ArrayList<Integer> num = asset.rowsInFrag(1);

        final CreateFragment[] array = new CreateFragment[s.size()];
        final String[] fragmentName = new String[s.size()];
        String[] gridNames = new String[s.size()+1];

        int start = 4;
        int end;
        for(int i=0; i<s.size(); i++){
            end = start+num.get(i);
            CreateFragment f = new CreateFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("Rows", num.get(i));
            bundle.putInt("Start", start);
            bundle.putInt("End", end);
            bundle.putParcelable("Asset", asset);
            bundle.putParcelable("myPrefs", myPrefs);
            fragmentName[i] = s.get(i);
            gridNames[i] = s.get(i);
            f.setArguments(bundle);
            array[i] = f;
            start = end;
        }
        gridNames[gridNames.length-1] = "API";

        apiFragment = new APIFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Rows", 0);
        bundle.putInt("Start", 0);
        bundle.putInt("End", 0);
        bundle.putParcelable("Asset", asset);
        bundle.putParcelable("myPrefs", myPrefs);
        apiFragment.setArguments(bundle);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gridNames);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if(position == array.length){
                    gridView.setVisibility(View.GONE);
                    currentFragment = apiFragment;
                    ft.replace(R.id.fragment_container3, currentFragment);
                    ft.commit();
                    ActionBar.Tab tab = getSupportActionBar().getTabAt(1);
                    tab.setText("API");
                }else {
                    for (int i = 0; i < array.length; i++) {
                        if (position == i) {
                            gridView.setVisibility(View.GONE);
                            currentFragment = array[position];
                            ft.replace(R.id.fragment_container3, currentFragment);
                            ft.commit();
                            ActionBar.Tab tab = getSupportActionBar().getTabAt(1);
                            tab.setText(fragmentName[position]);
                            break;
                        }
                    }
                }
            }
        });
    }

    public class MyListener implements ActionBar.TabListener{
        GridView gridView;

        public MyListener(GridView gridView){
            this.gridView = gridView;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(currentFragment != null){
                ft.remove(currentFragment);
                tab = getSupportActionBar().getTabAt(1);
                tab.setText("");
            }
            if(gridView.getVisibility() == View.GONE){
                gridView.setVisibility(View.VISIBLE);
                tab = getSupportActionBar().getTabAt(1);
                tab.setText("");
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(currentFragment != null){
                ft.remove(currentFragment);
                tab = getSupportActionBar().getTabAt(1);
                tab.setText("");
            }
            if(gridView.getVisibility() == View.GONE){
                gridView.setVisibility(View.VISIBLE);
                tab = getSupportActionBar().getTabAt(1);
                tab.setText("");
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }

    public class MyListener2 implements ActionBar.TabListener{

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }

}
