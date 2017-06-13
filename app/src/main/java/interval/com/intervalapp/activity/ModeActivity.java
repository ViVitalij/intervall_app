package interval.com.intervalapp.activity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.adapter.TrybRowAdapter;
import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.database.RealmSongsDataBase;
import interval.com.intervalapp.model.RunningMode;
import interval.com.intervalapp.model.Song;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by m.losK on 19.05.2017.
 */

public class ModeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;


    private static final int READ_EXTERNAL_STORAGE_REQUEST = 234;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    @BindView(R.id.listView)
    protected ListView listView;

    private TrybRowAdapter rowAdapter;
    private RealmResults<RunningMode> runningModes;
    private Realm realm = Realm.getDefaultInstance();

    private final static int REQUEST_PICK = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_activity);
        ButterKnife.bind(this);

        Button check_permission = (Button) findViewById(R.id.check_permission);
        Button request_permission = (Button) findViewById(R.id.request_permission);
        check_permission.setOnClickListener(this);
        request_permission.setOnClickListener(this);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        initList();
    }

    @Override
    public void onClick(View v) {

        view = v;

        int id = v.getId();
        switch (id) {
            case R.id.check_permission:
                if (checkPermission()) {

                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.request_permission:
                if (!checkPermission()) {

                    requestPermission();

                } else {

                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

                }
                break;
        }

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Snackbar.make(view, "Permission Granted, Now you can access write and read from external storage.", Snackbar.LENGTH_LONG).show();
                    else {

                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ModeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @OnClick(R.id.fab)
    protected void buttonClicked(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_musicPicker) {

            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            chooseFile.setType("audio/*");
            chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            startActivityForResult(Intent.createChooser(chooseFile, "Choose a file"), REQUEST_PICK);

            Toast.makeText(getApplicationContext(), R.string.music_picker, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_musicList) {
            startActivity(new Intent(getApplicationContext(), SongDragAndDropActivity.class));
            Toast.makeText(getApplicationContext(), R.string.music_list, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_runScreen) {
            startActivity(new Intent(getApplicationContext(), RunActivity.class));
            Toast.makeText(getApplicationContext(), R.string.run_screen, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_settings) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PICK) {

            if (resultCode == RESULT_OK) {
                List<Song> model = new ArrayList<>();
                RealmSongsDataBase list = new RealmSongsDataBase();
                Uri uri = data.getData();
                if (uri != null) {
                    model.add(createSongModel(uri));
                } else {
                    int count = data.getClipData().getItemCount();

                    for (int x = 0; x < count; x++) {
                        ClipData.Item item = data.getClipData().getItemAt(x);
                        model.add(createSongModel(item.getUri()));
                    }

                }
                list.saveSongs(model);
                Intent intent = new Intent(this, SongDragAndDropActivity.class);
                startActivity(intent);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Song createSongModel(Uri uri) {

        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        try (Cursor c = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + "=?",
                new String[]{id},
                null)) {

            assert c != null;
            c.moveToFirst();

            String path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
            String name = c.getString(c.getColumnIndex("title"));
            String artist = c.getString(c.getColumnIndex("artist"));
            return new Song(name, path, artist, Song.FAST);
        }
    }

    private void initList() {
        RealmModeDatabase base = new RealmModeDatabase();
        runningModes = base.readAllModes();
        rowAdapter = new TrybRowAdapter(this, runningModes);
        listView.setAdapter(rowAdapter);
        registerForContextMenu(listView);
        runningModes.addChangeListener(new RealmChangeListener<RealmResults<RunningMode>>() {
            @Override
            public void onChange(RealmResults<RunningMode> runningModes) {
                rowAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                final RunningMode mode = rowAdapter.getItem(info.position);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        mode.deleteFromRealm();
                    }
                });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}