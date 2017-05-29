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
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.database.RealmSongsDataBase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunSection.Intensity;
import interval.com.intervalapp.model.Song;

public class RunActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private int startingPosition = 0;
    @BindView(R.id.run_button)
    protected ToggleButton runButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);
    }

    @OnCheckedChanged(R.id.run_button)
    protected void check(boolean isChecked) {

        if (isChecked) {
            //TODO change icon to pause

            Toast.makeText(this, R.string.start_running, Toast.LENGTH_LONG).show();

            //TODO be aware when resume running
            List<RunSection> fullRunModel = getFullRunModel();
            RunSection runSection = fullRunModel.get(0);
            Duration duration = runSection.getDuration();

            Intensity intensity = runSection.getIntensity();
            switch (intensity) {
                case LOW:
                    startMusic("slow", duration);
                    break;
                case MEDIUM:
                    startMusic("slow", duration);
                    break;
                case HIGH:
                    startMusic("fast", duration);
                    break;
                default:
                    startMusic("slow", duration);
                    break;
            }

        } else {
            //TODO change icon to play
            mediaPlayer.pause();
            Toast.makeText(this, "Pause", Toast.LENGTH_LONG).show();
        }
    }

    //TODO when songList are empty
    private void startMusic(String musicTempo, Duration duration) {
        //TODO shouldn't be singleton?
        RealmSongsDataBase realmSongsDataBase = new RealmSongsDataBase();

        Toast.makeText(this, musicTempo + " section", Toast.LENGTH_LONG).show();
        final List<Song> songList = realmSongsDataBase.readSongList(musicTempo);
        Iterator iterator = songList.iterator();

        if (iterator.hasNext()) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);

            //TODO randomise songs
//            Collections.shuffle(songList);

            Song song = (Song) iterator.next();
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
                int almostEnd = mediaPlayer.getDuration();
                mediaPlayer.seekTo(almostEnd-1);
            }
        }, duration.getMillis());
    }

    @OnClick(R.id.stop_button)
    protected void stopRunning() {
        //TODO change run_button icon to play

        runButton.setChecked(false);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.seekTo(startingPosition);

            //TODO necessary? /release
            mediaPlayer.reset();
        }
        Toast.makeText(this, "Stop", Toast.LENGTH_LONG).show();
    }

    //TODO move to service class
    private List<RunSection> getFullRunModel() {

        RunSection fast = new RunSection(Intensity.HIGH, Duration.millis(5000));
        RunSection slow = new RunSection(Intensity.LOW, Duration.millis(10000));
        RunSection medium = new RunSection(Intensity.MEDIUM, Duration.millis(15000));

        List<RunSection> tabata = new ArrayList<>();
        tabata.add(fast);
        tabata.add(slow);
        tabata.add(medium);

        return tabata;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //TODO next song
        check(true);
//        mediaPlayer.start();
//        mediaPlayer.seekTo(startingPosition);
    }

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
