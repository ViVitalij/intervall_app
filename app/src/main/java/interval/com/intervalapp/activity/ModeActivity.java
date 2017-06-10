package interval.com.intervalapp.activity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * Created by m.losK on 19.05.2017.
 */

public class ModeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    @BindView(R.id.listView)
    protected ListView listView;

    private ModeRowAdapter rowAdapter;

    private Realm realm = Realm.getDefaultInstance();

    private final static int REQUEST_PICK = 2;

    private String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_activity);
        ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        initList();
    }

    //TODO unused
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
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK) {
            if (resultCode == RESULT_OK) {
                List<Song> model = new ArrayList<>();
                RealmSongsDataBase list = new RealmSongsDataBase();
                Uri uri = data.getData();
                if (uri != null) {
                    String tittle = getSongId(uri);
                    model.add(new Song(tittle, uri.toString()));
                } else {
                    int count = data.getClipData().getItemCount();

                    for (int x = 0; x < count; x++) {
                        ClipData.Item item = data.getClipData().getItemAt(x);
                        String tittle = getSongId(item.getUri());
                        model.add(new Song(tittle, item.getUri().toString()));
                    }

                }
                list.saveSongs(model);
                Intent intent = new Intent(this, SongDragAndDropActivity.class);
                startActivity(intent);
            }
        }
    }

    public String getSongId(Uri uri) {
        Cursor c = getContentResolver().query(
                uri,
                null,
                null,
                null,
                "");
        //TODO write something?
        if (null == c) {
            // ERROR
        }
        while (c.moveToNext()) {
            name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        }
        return name;
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
                        mode.deleteFromRealm();
                    }
                });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}