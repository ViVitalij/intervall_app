package interval.com.intervalapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.Chronometer;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

import static interval.com.intervalapp.R.id.circuralProgress;

public class SelectedMode extends BaseActivity {

    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.start_button)
    FloatingActionButton startButton;
    @BindView(circuralProgress)
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_mode_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        CircularProgressBar circularProgressBar = (CircularProgressBar) findViewById(circuralProgress);
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.start));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.pause));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
//        int animationDuration = 2500; // 2500ms = 2,5s
//        circularProgressBar.setProgressWithAnimation(65, animationDuration); // Default duration = 1500ms
    }

    @OnClick(R.id.stop_button)
    void stopButtonClicked() {
        circularProgressBar.stopAnimation();
        circularProgressBar.setProgress(0);
        showAlertDialog();
    }

    @OnClick(R.id.pause_button)
    void pauseButtonClicked() {

        circularProgressBar.stopAnimation();
//        float prodresStatus = circularProgressBar.getProgress();
//        circularProgressBar.setProgress(prodresStatus);
//        circularProgressBar.getAnimation().cancel();
//        float prodresStatus = circularProgressBar.getProgress();
//        circularProgressBar
//        circularProgressBar.setProgress(prodresStatus);
    }

    @OnClick(R.id.start_button)
    void startButtonClicked() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        circularProgressBar.setProgressWithAnimation(100, 6000);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure fatass?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        circularProgressBar.stopAnimation();
                        circularProgressBar.setProgress(0);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
