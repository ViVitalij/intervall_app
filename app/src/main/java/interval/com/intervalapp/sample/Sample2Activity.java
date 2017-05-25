package interval.com.intervalapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;

import interval.com.intervalapp.R;
import interval.com.intervalapp.utils.ProgressGenerator;

public class Sample2Activity extends BaseActivity {

    private int mMorphCounter1 = 1;

    public static void startThisActivity(@NonNull Context context) {
        context.startActivity(new Intent(context, Sample2Activity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sample_lineral);

        Chronometer mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer chronometer) {
                if (chronometer.getText().toString().equalsIgnoreCase("00:05:0")) {
                    chronometer.stop();
                    Toast.makeText(getBaseContext(), "Reached the end.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

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
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToSquare(btnMorph, integer(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            simulateProgress1(btnMorph);
        }
    }


    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_100))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
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
        int progressColor = color(R.color.mb_purple);
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

//public class Sample2Activity extends BaseActivity {
//    private int mMorphCounter1 = 1;
//
//    public static void startThisActivity(@NonNull Context context) {
//        context.startActivity(new Intent(context, Sample2Activity.class));
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ac_sample_lineral);
//
//        Chronometer mChronometer = (Chronometer) findViewById(R.id.chronometer);
//        mChronometer.start();
//        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//            public void onChronometerTick(Chronometer chronometer) {
//                if (chronometer.getText().toString().equalsIgnoreCase("00:05:0")) {
//                    chronometer.stop();
//                    Toast.makeText(getBaseContext(), "Reached the end.",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        final MorphingButton btnMorph1 = (MorphingButton) findViewById(R.id.startButton);
//        btnMorph1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onMorphButton1Clicked(btnMorph1);
//            }
//        });
//        morphToSquare(btnMorph1, 0);
//    }
//
//    private void onMorphButton1Clicked(final MorphingButton btnMorph) {
//        if (mMorphCounter1 == 0) {
//            mMorphCounter1++;
//            morphToSquare(btnMorph, integer(R.integer.mb_animation));
//        } else if (mMorphCounter1 == 1) {
//            mMorphCounter1 = 0;
//            morphToSuccess(btnMorph);
//        }
//    }
//
//    private void morphToSquare(final MorphingButton btnMorph, int duration) {
//        MorphingButton.Params square = MorphingButton.Params.create()
//                .duration(duration)
//                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
//                .width(dimen(R.dimen.mb_width_200))
//                .height(dimen(R.dimen.mb_height_56))
//                .color(color(R.color.mb_blue))
//                .colorPressed(color(R.color.mb_blue_dark))
//                .text(getString(R.string.mb_button));
//        btnMorph.morph(square);
//    }
//
//    private void morphToSuccess(final MorphingButton btnMorph) {
//        MorphingButton.Params circle = MorphingButton.Params.create()
//                .duration(integer(R.integer.mb_animation))
//                .cornerRadius(dimen(R.dimen.mb_height_56))
//                .width(dimen(R.dimen.mb_width_120))
//                .height(dimen(R.dimen.mb_height_56))
//                .color(color(R.color.mb_green))
//                .colorPressed(color(R.color.mb_green_dark))
//                .icon(R.drawable.ic_done)
//                .text("Success");
//        btnMorph.morph(circle);
//    }
//}
