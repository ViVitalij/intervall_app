package interval.com.intervalapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunningMode;
import io.realm.Realm;
import io.realm.RealmList;
import it.beppi.tristatetogglebutton_library.TriStateToggleButton;
import pl.polak.clicknumberpicker.ClickNumberPickerView;

public class CreateModeActivity extends AppCompatActivity {
    @BindView(R.id.add_mode)
    protected Button addMode;
    @BindView(R.id.mode_name_editor)
    protected EditText modeName;
    @BindView(R.id.delete_mode)
    protected Button deleteMode;

    @BindView(R.id.desription)
    protected TextView description;
    @BindView(R.id.textPercentage)
    protected TextView percentage;
    @BindView(R.id.intensityTextView)
    protected TextView intensityText;


    private Realm realm = Realm.getDefaultInstance();
    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;
    private final float mSeriesMax = 50f;
    private DecoView mDecoView;
    static Dialog d;
    private RealmList<RunSection> sectionList;
    String intensityMode = "";
    Long duration = (long) 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mode_layout);
        ButterKnife.bind(this);


        mDecoView = (DecoView) findViewById(R.id.dynamicArcView);
        createBackSeries();
        createEvents();
        toogleButton();
        sectionList = new RealmList<>();
        RunSection sectionTMP = new RunSection(intensityMode, duration);
        sectionList.add(sectionTMP);
        createDataSeries3();
        createDataSeries2();
        createDataSeries1();


    }


    @OnClick(R.id.time_picker)
    public void setTimePicker(View view) {
        showPicker();
    }

    @OnClick(R.id.add_mode)
    public void createRunSection() {
        createOneRunSection();
    }

    @OnClick(R.id.add_mode_button)
    public void addNewMode() {
        if (modeName.getText().length() == 0) {
            modeName.requestFocus();
            modeName.setError("Insert mode name!");
        } else {
            RunningMode runningMode = new RunningMode(modeName.getText().toString(), sectionList);
            RealmModeDatabase base = new RealmModeDatabase();
            base.saveRunningMode(runningMode);
            Intent intent = new Intent(this, ModeActivity.class);
            startActivity(intent);
        }
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textPercentage = (TextView) findViewById(R.id.textPercentage);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries3Index = mDecoView.addSeries(seriesItem);
    }

    private void createEvents() {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(100)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(42.4f)
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(7000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(16.3f)
                .setIndex(mSeries2Index)
                .setDelay(8500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(12500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(4.36f).setIndex(mSeries3Index).setDelay(14000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries3Index).setDelay(18000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries2Index).setDelay(18000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries1Index)
                .setDelay(20000)
                .setDuration(1000)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        resetText();
                    }
                })
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(21000)
                .setDuration(3000)
                .setDisplayText("GOAL!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        createEvents();
                    }
                })
                .build());

        resetText();
    }

    private void resetText() {
        ((TextView) findViewById(R.id.textPercentage)).setText("");

    }

    public void showPicker() {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.show();
        final ClickNumberPickerView minutePicker = (ClickNumberPickerView) d.findViewById(R.id.minutePicker);
        final ClickNumberPickerView secondsPicker = (ClickNumberPickerView) d.findViewById(R.id.secondsPicker);
        Button button = (Button) d.findViewById(R.id.set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long minutesInMillis = (long) (minutePicker.getValue() * 60000);
                long secondsInMillis = (long) (secondsPicker.getValue() * 1000);
                duration = minutesInMillis + secondsInMillis;
                d.dismiss();
            }
        });


    }


    public void toogleButton() {
        TriStateToggleButton tstb_1 = (TriStateToggleButton) findViewById(R.id.intensityButton);
        tstb_1.setOnToggleChanged(new TriStateToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(TriStateToggleButton.ToggleStatus toggleStatus, boolean booleanToggleStatus, int toggleIntValue) {
                switch (toggleStatus) {
                    case off:
                        intensityText.setText("High");
                        intensityText.setTextColor(Color.parseColor("#CC1D1D"));
                        intensityMode = RunSection.HIGH;
                        break;
                    case mid:
                        intensityText.setText("Medium");
                        intensityText.setTextColor(Color.parseColor("#8EAE3C"));
                        intensityMode = RunSection.MEDIUM;
                        break;
                    case on:
                        intensityText.setText("Low");
                        intensityText.setTextColor(Color.parseColor("#FFFFFF"));
                        intensityMode = RunSection.LOW;
                        break;
                }
            }
        });
    }


    public void createOneRunSection() {
        RunSection section = new RunSection(intensityMode, duration);
        sectionList.add(section);
        Log.e("asd", String.valueOf(sectionList.size()));

    }
}






