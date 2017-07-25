package com.myolin.optimiser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10;

    ArrayList<Project> projects;
    ListView pList;
    File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        projects = new ArrayList<>();
        pList = (ListView) findViewById(R.id.projectList);
        root = new File(Environment.getExternalStorageDirectory(), "OptimiserData");
        if(!root.exists()){
            root.mkdirs();
        }

        request();

        pList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NavigationActivity.this, HomeActivity.class);
                Project p = (Project) pList.getItemAtPosition(i);
                String s = p.getName();
                intent.putExtra("ProjectName", s);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_create){

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(NavigationActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_create, null);
            final EditText mProject = (EditText) mView.findViewById(R.id.projectName);
            Button createProjectButton = (Button) mView.findViewById(R.id.createProjectButton);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            createProjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mProject.getText().toString().isEmpty()) {
                        Project p = new Project(mProject.getText().toString());
                        projects.add(p);

                        BindDictionary<Project> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.pName, new StringExtractor<Project>() {
                            @Override
                            public String getStringValue(Project item, int position) {
                                return item.getName();
                            }
                        });

                        FunDapter adapter = new FunDapter(NavigationActivity.this, projects, R.layout.project_layout, dictionary);

                        pList.setAdapter(adapter);

                        dialog.dismiss();

                        File gpxFile = new File(root, mProject.getText().toString() + ".txt");
                        try {
                            FileWriter writer = new FileWriter(gpxFile, false);
                            writer.append("");
                            writer.flush();
                            writer.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(NavigationActivity.this, "Please enter project name", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            pList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(NavigationActivity.this, HomeActivity.class);
                    Project p = (Project) pList.getItemAtPosition(i);
                    String s = p.getName();
                    intent.putExtra("ProjectName", s);
                    startActivity(intent);
                }
            });

        }else if(id == R.id.nav_delete){
            pList.setOnItemClickListener(null);

            final CustomListViewAdapter adapter = new CustomListViewAdapter(NavigationActivity.this, R.layout.project_layout, projects);

            pList.setAdapter(adapter);

            pList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            pList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    actionMode.getMenuInflater().inflate(R.menu.delete_menu_option, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.p_delete:
                            SparseBooleanArray selected = adapter.getSelectedIds();
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    Project selectedListItem = (Project) adapter.getItem(selected.keyAt(i));
                                    adapter.remove(selectedListItem);
                                    File f = new File(root.getAbsolutePath() + "/" + selectedListItem.getName() +".txt");
                                    boolean deleted = f.delete();
                                }
                            }
                            actionMode.finish();
                            Toast.makeText(NavigationActivity.this, "Selected Projects Deleted", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                    adapter.removeSelection();
                }

                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                    final int checkedCount = pList.getCheckedItemCount();
                    actionMode.setTitle(checkedCount + "Selected");
                    adapter.toggleSelection(i);
                }
            });

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void request(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }else{
            readStorage();
        }
    }

    private void readStorage(){


        File[] files= root.listFiles();
        for(File f:files){
            String name = f.getName();
            if(name.endsWith(".txt")){
                projects.add(new Project(name.substring(0, name.lastIndexOf('.'))));
            }
        }

        BindDictionary<Project> dictionary = new BindDictionary<>();
        dictionary.addStringField(R.id.pName, new StringExtractor<Project>() {
            @Override
            public String getStringValue(Project item, int position) {
                return item.getName();
            }
        });

        FunDapter adapter = new FunDapter(NavigationActivity.this, projects, R.layout.project_layout, dictionary);

        pList.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readStorage();
                }else{
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                        new AlertDialog.Builder(this).
                                setTitle("Read Storage Permission").
                                setMessage("You need to grant storage permission to read external storage. Retry and grand it.").show();
                    }else{
                        new AlertDialog.Builder(this).
                                setTitle("Read Storage permission denied").
                                setMessage("You denied read storage permission. So the feature is disabled. To enable it, go to settings.").show();
                    }
                }

                break;
        }
    }
}
