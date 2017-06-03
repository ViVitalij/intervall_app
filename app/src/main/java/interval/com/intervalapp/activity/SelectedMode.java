package interval.com.intervalapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Chronometer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

public class SelectedMode extends BaseActivity {

    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.start_button)
    FloatingActionButton startButton;

    boolean isStart;
    private int mMorphCounter1 = 1;

    public static void startThisActivity(@NonNull Context context) {
        context.startActivity(new Intent(context, SelectedMode.class));
    }


    @OnClick(R.id.stop_button)
    void stopButtonClicked() {

    }

    @OnClick(R.id.pause_button)
    void pauseButtonClicked() {

    }

    @OnClick(R.id.start_button)
    void startButtonClicked() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_mode_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }
}
