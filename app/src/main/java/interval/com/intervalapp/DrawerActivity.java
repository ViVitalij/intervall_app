package interval.com.intervalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton floatingButton;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initToolbar();

        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_music) {
            startActivity(new Intent(getApplicationContext(), ModeActivity.class));
            Toast.makeText(getApplicationContext(), "gallery", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), DragDropActivity.class));
            Toast.makeText(getApplicationContext(), "drag and drop", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_settings) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}