package interval.com.intervalapp.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.danlew.android.joda.JodaTimeAndroid;

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

public class RunActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        Chronometer.OnChronometerTickListener {

    @BindView(R.id.android_chronometer)
    protected Chronometer androidChronometer;
    @BindView(R.id.class_chronometer)
    protected interval.com.intervalapp.chronometer.Chronometer classChronometer;

    private MediaPlayer mediaPlayer;
    private int startingPosition = 0;
    @BindView(R.id.run_button)
    protected ToggleButton runButton;
    private List<Song> fastSongList;
    private List<Song> slowSongList;
    private RealmList<RunSection> runMode;
    @BindView(R.id.countdown_timer)
    protected TextView countdownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);

        setSongsList();
        setRunningMode();

//        mainChronometer.setOnChronometerTickListener(this);
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
            androidChronometer.setBase(SystemClock.elapsedRealtime());
            androidChronometer.start();

            classChronometer.setBase(SystemClock.elapsedRealtime());
            classChronometer.start();

            RunSection runSection = runMode.get(0);
            Long duration = runSection.getDuration();
            String intensity = runSection.getIntensity();

            startCountdownTimer(duration, intensity);
            Toast.makeText(this, "tabata started", Toast.LENGTH_SHORT).show();

            //TODO be aware when resume running


        } else {
            //TODO change icon to play
//            mainChronometer.stop();
            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCountdownTimer(Long duration, final String intensity) {
        CountDownTimer countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTimer.setText("pozostalo: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                countdownTimer.setText("0");
                Toast.makeText(getApplicationContext(), intensity, Toast.LENGTH_LONG).show();
                startingPosition++;
                startNextIntensitySong();
            }
        }.start();
    }

    private void startNextIntensitySong() {
        if(startingPosition<runMode.size()){
            RunSection runSection = runMode.get(startingPosition);
            Long duration = runSection.getDuration();
            String intensity = runSection.getIntensity();

            if (intensity.equals(RunSection.LOW)) {
                startCountdownTimer(duration, intensity);
//                startMusic(Song.SLOW);
            } else if (intensity.equals(RunSection.MEDIUM)) {
                startCountdownTimer(duration, intensity);
//                startMusic(Song.FAST);
            } else if (intensity.equals(RunSection.HIGH)) {
                startCountdownTimer(duration, intensity);
//                startMusic(Song.FAST);
            } else {
                startCountdownTimer(duration, intensity);
//                startMusic(Song.SLOW);
            }
        }
    }

    //TODO when songsList are empty
    private void startMusic(String musicTempo) {
        //TODO temporal
        List<Song> songList;
        if (musicTempo.equals(Song.FAST)) {
            songList = fastSongList;
        } else {
            songList = slowSongList;
        }


        Toast.makeText(this, musicTempo + " section", Toast.LENGTH_SHORT).show();

        if (songList.isEmpty()) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);

            Song song = songList.get(new Random().nextInt(songList.size()));
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

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                mediaPlayer.seekTo(mediaPlayer.getDuration() - 1);
//                mediaPlayer.stop();
//            }
//        }, duration);
    }

    @OnClick(R.id.stop_button)
    protected void stopRunning() {
        //TODO check if user really want to stop

        //TODO change run_button icon to play

        runButton.setChecked(false);
        androidChronometer.stop();
        classChronometer.stop();


//        if (mediaPlayer.isPlaying()) {
//
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer.seekTo(startingPosition);

        //TODO necessary? /release
//            mediaPlayer.reset();
//        }
        Toast.makeText(this, "Stop", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //TODO next song
//        check(true);
//        mediaPlayer.start();
//        mediaPlayer.seekTo(startingPosition);
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

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
//        RunSection runSection = runMode.get(0);
//        Long duration = runSection.getDuration();
//        String intensity = runSection.getIntensity();
//
//        if(intensity.equals(RunSection.LOW)){
//            startMusic(Song.SLOW);
//        } else if(intensity.equals(RunSection.MEDIUM)){
//            startMusic(Song.FAST);
//        } else if (intensity.equals(RunSection.HIGH)){
//            startMusic(Song.FAST);
//        } else {
//            startMusic(Song.SLOW);
//        }
        if (chronometer.equals("0:20")) {
            Toast.makeText(this, "HAAAAAAAA", Toast.LENGTH_LONG).show();
        }

    }
}
