package interval.com.intervalapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Chronometer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

public class SelectedMode extends AppCompatActivity {

    @BindView(R.id.chronometer)
    protected Chronometer chronometer;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_mode_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.stop_button)
    void stopButtonClicked() {
        showAlertDialog();
    }

    @OnClick(R.id.pause_button)
    void pauseButtonClicked() {
    }

    @OnClick(R.id.start_button)
    void startButtonClicked() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure?")
                .setCancelable(true)
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                                startActivity(intent);
                                dialog.cancel();
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
