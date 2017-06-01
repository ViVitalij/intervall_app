package interval.com.intervalapp.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.database.RealmSongsDataBase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunningMode;
import interval.com.intervalapp.model.Song;
import io.realm.RealmList;

public class RunActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, Chronometer.OnChronometerTickListener {


    @BindView(R.id.chronometer)
    protected Chronometer chronometer;

    private MediaPlayer mediaPlayer;
    private int startingPosition = 0;
    @BindView(R.id.run_button)
    protected ToggleButton runButton;
    private List<Song> fastSongList;
    private List<Song> slowSongList;
    private RealmList<RunSection> runMode;
    @BindView(R.id.countdown_textView)
    protected TextView countdownTextView;

    private Integer counter = 0;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);

        setSongsList();
        setRunningMode();

        chronometer.setOnChronometerTickListener(this);
    }

    private void setRunningMode() {
        String modeNameFromIntent = getIntent().getStringExtra("modeName");
        RealmModeDatabase realmModeDatabase = new RealmModeDatabase();
        RunningMode runningMode = realmModeDatabase.readRunningMode(modeNameFromIntent);
        runMode = runningMode.getRunMode();
    }

    private void setSongsList() {
        RealmSongsDataBase realmSongsDataBase = new RealmSongsDataBase();
        fastSongList = realmSongsDataBase.readSongList(Song.FAST);
        slowSongList = realmSongsDataBase.readSongList(Song.SLOW);
    }

    @OnCheckedChanged(R.id.run_button)
    protected void check(boolean isChecked) {
        if (isChecked) {
            //TODO change icon to pause
            //TODO be aware when resume running

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            handler.postDelayed(runnable, 100);
        } else {
            //TODO change icon to play
            chronometer.stop();
            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (counter < runMode.size()) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                RunSection runSection = runMode.get(counter);
                Long duration = runSection.getDuration();
                String intensity = runSection.getIntensity();
                startMusic(intensity);
                Toast.makeText(getApplicationContext(), intensity, Toast.LENGTH_LONG).show();
                counter++;

                handler.postDelayed(this, duration);
            } else {
                handler.removeCallbacks(runnable);
            }
        }
    };

    //TODO when songsList are empty
    private void startMusic(String intensity) {
        Song song;
        if (intensity.equals(RunSection.HIGH)) {
            song = fastSongList.get(new Random().nextInt(fastSongList.size()));
        } else {
            song = slowSongList.get(new Random().nextInt(slowSongList.size()));
        }

        mediaPlayer = new MediaPlayer();

        //TODO ask if it can be somewhere else
        mediaPlayer.setOnCompletionListener(this);

        String stringSongUri = song.getUri();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(stringSongUri));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    @OnClick(R.id.stop_button)
    protected void stopRunning() {
        //TODO check if user really want to stop

        //TODO change run_button icon to play

        runButton.setChecked(false);
        chronometer.stop();
        counter=0;


        if (mediaPlayer != null && mediaPlayer.isPlaying()) {

            mediaPlayer.stop();
            mediaPlayer.release();
            //TODO ask about release() and error state
        }
        Toast.makeText(this, "Stop", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //TODO next song
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
    }
}

/* fade in and out
        public void load(String path, boolean looping)
        {
            mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(path)));
            mediaPlayer.setLooping(looping);
        public void load(int address, boolean looping)
        {
            mediaPlayer = MediaPlayer.create(context, address);
            mediaPlayer.setLooping(looping);
            if(!mediaPlayer.isPlaying()) mediaPlayer.start();
            {
                final Timer timer = new Timer(true);
                TimerTask timerTask = new TimerTask()
                {
                    @Override
                    {
    */