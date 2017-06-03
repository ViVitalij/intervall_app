package interval.com.intervalapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Chronometer;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.utils.ProgressGenerator;

public class SelectedMode extends BaseActivity {

    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.start_button)
    LinearProgressButton startButton;

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
    void startButtonClicked(View view) {
        onMorphButton1Clicked((LinearProgressButton) view);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_mode_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        morphToSquare(startButton, 0);
    }


    private void onMorphButton1Clicked(final LinearProgressButton btnMorph) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToSquare(btnMorph, integer(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            simulateProgress1(btnMorph);
        }
    }


    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.start))
                .colorPressed(color(R.color.start))
                .text(getString(R.string.mb_button));
        btnMorph.morph(circle);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.colorAccent))
                .colorPressed(color(R.color.colorAccent))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    private void simulateProgress1(@NonNull final LinearProgressButton button) {
        int progressColor = color(R.color.colorAccent);
        int color = color(R.color.mb_gray);
        int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
        int width = dimen(R.dimen.mb_width_200);
        int height = dimen(R.dimen.mb_height_8);
        int duration = integer(R.integer.mb_animation);

        ProgressGenerator generator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
            @Override
            public void onComplete() {
                morphToSuccess(button);
                button.unblockTouch();
            }
        });
        button.blockTouch(); // prevent user from clicking while button is in progress
        button.morphToProgress(color, progressColor, progressCornerRadius, width, height, duration);
        generator.start(button);
    }
}
