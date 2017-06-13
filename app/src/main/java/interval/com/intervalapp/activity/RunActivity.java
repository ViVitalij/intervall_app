package interval.com.intervalapp.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.database.RealmSongsDataBase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunningMode;
import interval.com.intervalapp.model.Song;
import io.realm.RealmList;

public class RunActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    @BindView(R.id.chronometer)
    protected Chronometer chronometer;
    @BindView(R.id.countdown_textView)
    protected TextView countdownTextView;

    private MediaPlayer mediaPlayer;
    private int startingPosition = 0;
    private List<Song> fastSongList;
    private List<Song> slowSongList;
    private RealmList<RunSection> runMode;
    private Integer counter = 0;
    //TODO new Handler() here or onCreate?
    private Handler handler = new Handler();
    private CountDownTimer countDownTimer;

    private final static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);

        setSongsList();
        setRunningMode();
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

    @OnClick(R.id.start_button2)
    protected void startClicked() {
        //TODO change icon to pause
        //TODO be aware when resume running


        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        handler.postDelayed(runnable, 100);
    }

    //TODO warning - not working
    @OnClick(R.id.pause_button2)
    protected void pauseClicked() {

        chronometer.stop();
        mediaPlayer.pause();
        Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.stop_button2)
    protected void stopClicked() {
        //TODO check if user really want to stop

        //TODO change run_button icon to play

        counter = 0;
        chronometer.stop();
        chronometer.setText("00:00");
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        countdownTextView.setText("0");

        handler.removeCallbacks(runnable);

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Toast.makeText(this, "Stop", Toast.LENGTH_LONG).show();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (counter < runMode.size()) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                RunSection runSection = runMode.get(counter);
                Long duration = runSection.getDuration();
                startCountDownTimer(duration);
                String intensity = runSection.getIntensity();
                if (!(fastSongList.size() == 0 && slowSongList.size() == 0)) {
                    startMusic(intensity);
                }
                Toast.makeText(getApplicationContext(), intensity, Toast.LENGTH_LONG).show();
                counter++;

                handler.postDelayed(this, duration);
            } else {
                handler.removeCallbacks(runnable);
            }
        }
    };

    private void startCountDownTimer(Long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                countdownTextView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if (counter == runMode.size()) {
                    countdownTextView.setText("done!");
                } else {
                    countdownTextView.setText("0");
                }
            }
        };
        countDownTimer.start();
    }

    //TODO when songsList are empty
    //TODO random with no repetitions
    private void startMusic(String intensity) {
        Song song;
        if (intensity.equals(RunSection.HIGH)) {
            song = fastSongList.get(new Random().nextInt(fastSongList.size()));
        } else {
            song = slowSongList.get(new Random().nextInt(slowSongList.size()));
        }
        Log.i(TAG, song.getPath());
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(song.getPath());
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "IOException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "IllegalArgumentException: " + e.getMessage());
        } catch (SecurityException e) {
            Log.d(TAG, "SecurityException: " + e.getMessage());
        }

        try {
            mediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()

        {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
//        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //TODO next song
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
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