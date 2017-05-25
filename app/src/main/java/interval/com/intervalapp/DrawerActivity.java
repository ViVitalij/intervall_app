package interval.com.intervalapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE = 1;
    private static final int PICK_FILE_REQUEST = 1;
    private FloatingActionButton floatingButton;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Context context;

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
//            Intent i2 = new Intent(getApplicationContext(), FileChooser.class);
//            i2.putExtra(Constants.SELECTION_MODE,Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal());
//            startActivityForResult(i2,PICK_FILE_REQUEST);
////            Intent intent = new Intent();
//            intent.setAction(android.content.Intent.ACTION_PICK);
//            intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, REQUEST_CODE);
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("audio/*");
            chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(chooseFile, "Choose a file") , 2);



//            startActivity(new Intent(getApplicationContext(), ModeActivity.class));
            Toast.makeText(getApplicationContext(), "picker", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), DragDropActivity.class));
            Toast.makeText(getApplicationContext(), "drag and drop", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_settings) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_FILE_REQUEST && data!=null) {
//            if (resultCode == RESULT_OK) {
//                ArrayList<Uri> selectedFiles  = data.getParcelableArrayListExtra(Constants.SELECTED_ITEMS);
//            }
//        }
        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void getMP3Id() {
        String path = null;
        try {
            path = new File(new URI("/sdcard/music.mp3").getPath()).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                },
                null,null,null);

        if (null == c) {
            // ERROR
        }

        while (c.moveToNext()) {
            c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));

        }
    }
}