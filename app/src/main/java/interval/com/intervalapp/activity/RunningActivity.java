package interval.com.intervalapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import interval.com.intervalapp.utils.CircularProgressBar;
import io.realm.RealmList;

import static interval.com.intervalapp.R.id.circular_progress_bar;

public class RunningActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private static final String TAG = "TAG";

    @BindView(R.id.chronometer)
    protected Chronometer chronometer;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.countdown_text_view)
    protected TextView countdownTextView;

    @BindView(circular_progress_bar)
    protected CircularProgressBar circularProgressBar;

    private RealmList<RunSection> runSectionList;

    private List<Song> fastSongList;

    private List<Song> slowSongList;

    private MediaPlayer mediaPlayer;

    private Handler handler;

    private CountDownTimer countDownTimer;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setSongsList();
        setRunningMode();
        setCircularProgressBar();
        handler = new Handler();
    }

    private void setRunningMode() {
        String modeNameFromIntent = getIntent().getStringExtra(getString(R.string.intent_mode_name));
        RealmModeDatabase realmModeDatabase = new RealmModeDatabase();
        RunningMode runningMode = realmModeDatabase.readRunningMode(modeNameFromIntent);
        runSectionList = runningMode.getRunSectionList();
    }

    private void setSongsList() {
        RealmSongsDataBase realmSongsDataBase = new RealmSongsDataBase();
        fastSongList = realmSongsDataBase.readSongList(Song.FAST);
        slowSongList = realmSongsDataBase.readSongList(Song.SLOW);
    }

    private void setCircularProgressBar() {
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.low_intensity));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.medium_intensity));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progress_bar_width));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.progress_bar_background_width));
    }

    @OnClick(R.id.start_button)
    protected void startButtonClicked() {
        handler.postDelayed(runnable, 100);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        circularProgressBar.setProgressWithAnimation(100, 6000);
    }

    @OnClick(R.id.pause_button)
    protected void pauseButtonClicked() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        chronometer.stop();
        circularProgressBar.stopAnimation();
    }

    @OnClick(R.id.stop_button)
    protected void stopButtonClicked() {
        showAlertDialog();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.are_you_sure)
                .setCancelable(true)
                .setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                counter = 0;
                                chronometer.stop();
                                chronometer.setText(R.string.time_zero);
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                countdownTextView.setText(R.string.zero);

                                handler.removeCallbacks(runnable);

                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                }

                                dialog.cancel();
                                circularProgressBar.stopAnimation();
                                circularProgressBar.setProgress(0);
                                Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (counter < runSectionList.size()) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                RunSection runSection = runSectionList.get(counter);
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
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                if (counter == runSectionList.size()) {
                    countdownTextView.setText(R.string.done);
                } else {
                    countdownTextView.setText(R.string.zero);
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
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
