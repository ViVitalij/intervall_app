package interval.com.intervalapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Chronometer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

import static interval.com.intervalapp.R.id.circular_progress_bar;

public class SelectedMode extends AppCompatActivity {

    @BindView(R.id.chronometer)
    protected Chronometer chronometer;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(circular_progress_bar)
    protected CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_mode_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setCircularProgressBar();
    }

    private void setCircularProgressBar() {
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.low_intensity));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.medium_intensity));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progress_bar_width));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.progress_bar_background_width));
    }

    @OnClick(R.id.start_button)
    protected void startButtonClicked() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        circularProgressBar.setProgressWithAnimation(100, 6000);
    }

    @OnClick(R.id.pause_button)
    protected void pauseButtonClicked() {
        circularProgressBar.stopAnimation();
    }

    @OnClick(R.id.stop_button)
    protected void stopButtonClicked() {
        showAlertDialog();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure?")
                .setCancelable(true)
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                circularProgressBar.stopAnimation();
                                circularProgressBar.setProgress(0);
                                Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
    }
}
