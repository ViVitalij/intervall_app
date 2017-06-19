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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.adapter.ModeRowAdapter;
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

public class ModeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private static final int REQUEST_PICK = 2;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    @BindView(R.id.listView)
    protected ListView listView;

    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    private ModeRowAdapter rowAdapter;

    private Realm realm = Realm.getDefaultInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_activity);
        ButterKnife.bind(this);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        initList();
    }

    @OnClick(R.id.menu_image_view)
    void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }


    @OnClick(R.id.fab)
    protected void buttonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateModeActivity.class);
        startActivity(intent);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_musicPicker:
                if (checkPermission()) {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT,
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    chooseFile.setType("audio/*");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }
                    startActivityForResult(Intent.createChooser(chooseFile, getString(R.string.choose_a_file)),
                            REQUEST_PICK);
                } else {
                    requestPermission();
                }
                break;
            case R.id.nav_musicList:
                startActivity(new Intent(getApplicationContext(), SongDragAndDropActivity.class));
                break;
            case R.id.nav_runScreen:
                startActivity(new Intent(getApplicationContext(), RunningActivity.class)
                        .putExtra(getString(R.string.intent_mode_name), getString(R.string.tabata)));
                break;
            case R.id.nav_settings:
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkPermission() {
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int readExternalStorageResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);

        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED
                && readExternalStorageResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeExternalStorageAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalStorageAccepted = grantResults[1]
                            == PackageManager.PERMISSION_GRANTED;

                    if (writeExternalStorageAccepted && readExternalStorageAccepted)
                        Snackbar.make(coordinatorLayout, R.string.permission_granted,
                                Snackbar.LENGTH_LONG).show();
                    else {
                        Snackbar.make(coordinatorLayout, R.string.permission_denied,
                                Snackbar.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel(getString(R.string.both_permissions_requirement),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]
                                                                    {WRITE_EXTERNAL_STORAGE,
                                                                            READ_EXTERNAL_STORAGE},
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK) {
            if (resultCode == RESULT_OK) {
                List<Song> songsList = new ArrayList<>();
                RealmSongsDataBase realmSongsDatabase = new RealmSongsDataBase();
                Uri uri = data.getData();
                if (uri != null) {
                    songsList.add(createSongModel(uri, data));
                } else {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        ClipData.Item item = data.getClipData().getItemAt(i);
                        songsList.add(createSongModel(item.getUri(), data));
                    }
                }
                realmSongsDatabase.saveOrUpdateSongs(songsList);
                Intent intent = new Intent(this, SongDragAndDropActivity.class);
                startActivity(intent);
            }
        }
    }

    private Song createSongModel(Uri uri, Intent data) {
        String filePath = null;
        String title = null;
        if (Build.VERSION.SDK_INT < 19) {
            Uri selectedAudio = data.getData();
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver()
                    .query(selectedAudio,
                            filePathColumn,
                            null,
                            null,
                            null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor
                        .getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                title = getTitle(selectedAudio);
                cursor.close();
            }
        } else {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            Cursor cursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Audio.Media._ID + "=?",
                    new String[]{id},
                    null);
            if (cursor != null) {
                cursor.moveToFirst();
                filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                title = cursor.getString(cursor.getColumnIndex(getString(R.string.title)));
                cursor.close();
            }
        }
        return new Song(title, filePath, Song.FAST);
    }

    private String getTitle(Uri uri) {
        String nameWithoutExtension = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            cursor.close();
            nameWithoutExtension = name.split("\\.")[0];
        }
        return nameWithoutExtension;
    }

    private void initList() {
        RealmModeDatabase base = new RealmModeDatabase();
        RealmResults<RunningMode> runningModes = base.readAllModes();
        rowAdapter = new ModeRowAdapter(this, runningModes);
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
                        if (mode != null) {
                            mode.deleteFromRealm();
                        }
                    }
                });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ModeActivity.this)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }
}