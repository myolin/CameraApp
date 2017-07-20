package com.myolin.optimiser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Project> projects;

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
            final ListView pList = (ListView) findViewById(R.id.projectList);

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

                    }else{
                        Toast.makeText(NavigationActivity.this, "Please enter project name", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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

        }else if(id == R.id.nav_delete){

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
