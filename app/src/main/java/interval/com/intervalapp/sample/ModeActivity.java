package interval.com.intervalapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;

import interval.com.intervalapp.R;
import interval.com.intervalapp.utils.ProgressGenerator;

/**
 * Created by RENT on 2017-05-25.
 */


public class ModeActivity extends BaseActivity {

    private int startCounter = 1;

    public static void startThisActivity(@NonNull Context context) {
        context.startActivity(new Intent(context, ModeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sample_lineral);

        final LinearProgressButton startButton = (LinearProgressButton) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton1Clicked(startButton);
            }
        });

        morphToSquare(startButton, 0);
    }

    private void onMorphButton1Clicked(final LinearProgressButton btnMorph) {
        if (startCounter == 0) {
            startCounter++;
            morphToSquare(btnMorph, integer(R.integer.mb_animation));
        } else if (startCounter == 1) {
            startCounter = 0;
            simulateProgress1(btnMorph);
        }
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_100))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.background))
                .colorPressed(color(R.color.background))
                .text(getString(R.string.mb_button));
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    private void simulateProgress1(@NonNull final LinearProgressButton button) {
        int progressColor = color(R.color.colorAccent);
        int color = color(R.color.primary_background);
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
