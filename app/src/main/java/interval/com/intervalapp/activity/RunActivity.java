package interval.com.intervalapp.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.database.RealmSongsDataBase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunSection.Intensity;
import interval.com.intervalapp.model.RunningMode;
import interval.com.intervalapp.model.Song;

public class RunActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private int startingPosition = 0;
    @BindView(R.id.run_button)
    protected ToggleButton runButton;
    private List<Song> fastSongList;
    private List<Song> slowSongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);
        RealmSongsDataBase realmSongsDataBase = new RealmSongsDataBase();
        fastSongList = realmSongsDataBase.readSongList(Song.FAST);
        slowSongList = realmSongsDataBase.readSongList(Song.SLOW);
    }

    @OnCheckedChanged(R.id.run_button)
    protected void check(boolean isChecked) {

        if (isChecked) {
            //TODO change icon to pause

            Toast.makeText(this, R.string.start_running, Toast.LENGTH_LONG).show();

//            //TODO be aware when resume running
//            RunningMode runModel = getFullRunModel();
//            List<RunSection> runMode = runModel.getRunMode();
//            if (runMode.isEmpty()) {
//                RunSection runSection = runMode.get(0);
//                Duration duration = runSection.getDuration();
//                Intensity intensity = runSection.getIntensity();
//                switch (intensity) {
//                    case LOW:
//                        startMusic(Song.SLOW, duration);
//                        break;
//                    case MEDIUM:
//                        startMusic(Song.SLOW, duration);
//                        break;
//                    case HIGH:
//                        startMusic(Song.FAST, duration);
//                        break;
//                    default:
//                        startMusic(Song.SLOW, duration);
//                        break;
//                }
//            }
        } else {
            //TODO change icon to play
            mediaPlayer.pause();
            Toast.makeText(this, "Pause", Toast.LENGTH_LONG).show();
        }
    }

    //TODO when songList are empty
    private void startMusic(String musicTempo, Duration duration) {
        //TODO temporal
        List<Song> songList;
        if (musicTempo.equals(Song.FAST)) {
            songList = fastSongList;
        } else {
            songList = slowSongList;
        }


        Toast.makeText(this, musicTempo + " section", Toast.LENGTH_LONG).show();

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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mediaPlayer.seekTo(mediaPlayer.getDuration() - 1);
                mediaPlayer.stop();
            }
        }, duration.getMillis());
    }

    @OnClick(R.id.stop_button)
    protected void stopRunning() {
        //TODO change run_button icon to play

        runButton.setChecked(false);
        if (mediaPlayer.isPlaying()) {

            mediaPlayer.stop();
            mediaPlayer.release();
//            mediaPlayer.seekTo(startingPosition);

            //TODO necessary? /release
//            mediaPlayer.reset();
        }
        Toast.makeText(this, "Stop", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //TODO next song
        check(true);
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
}
