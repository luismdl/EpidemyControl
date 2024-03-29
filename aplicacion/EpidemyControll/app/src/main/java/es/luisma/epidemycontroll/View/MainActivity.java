package es.luisma.epidemycontroll.View;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import es.luisma.epidemycontroll.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener{

    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("login",MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(sp.getInt("admin", 0)==1){
            Menu m = navigationView.getMenu();
            m.getItem(4).setVisible(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        Bundle bundle = new Bundle();
        bundle.putString("user", sp.getString("user", "Not Logged"));

        // Set the home as default
        Fragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content, fragment).commit();

        TextView r = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navViewName);
        r.setText(sp.getString("user", "Not Logged"));



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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if(id == R.id.nav_home){
            Bundle bundle = new Bundle();
            bundle.putString("user", sp.getString("user", "Not Logged"));
            fragment = new HomeFragment();
            fragment.setArguments(bundle);
        }else if(id == R.id.nav_profile){
            Bundle bundle = new Bundle();
            bundle.putString("user", sp.getString("user", "Not Logged"));
            fragment = new ProfileFragment();
            fragment.setArguments(bundle);
        }else if (id == R.id.nav_delete){
            fragment = new DeleteFragment();
        }else if(id == R.id.nav_alerts){
            fragment = new AlertsFragment();
        }else if(id == R.id.nav_misAlerts){
            Bundle bundle = new Bundle();
            bundle.putString("user", sp.getString("user", "Not Logged"));
            fragment = new EventsFragment();
            fragment.setArguments(bundle);
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
