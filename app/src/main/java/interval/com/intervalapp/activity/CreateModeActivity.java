/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Beppi Menozzi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package interval.com.intervalapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunningMode;
import io.realm.RealmList;
import it.beppi.tristatetogglebutton_library.TriStateToggleButton;
import pl.polak.clicknumberpicker.ClickNumberPickerView;

public class CreateModeActivity extends AppCompatActivity
        implements TriStateToggleButton.OnToggleChanged {

    @BindView(R.id.mode_name_edit_text)
    protected EditText modeNameEditText;

    @BindView(R.id.description_edit_text)
    protected EditText descriptionEditText;

    @BindView(R.id.intensity_text_view)
    protected TextView intensityTextView;

    @BindView(R.id.pie_chart)
    protected PieChart pieChart;

    @BindView(R.id.tristate_intensity_button)
    protected TriStateToggleButton tristateIntensityButton;

    @BindView(R.id.intensity_duration_button)
    protected Button intensityDurationButton;

    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    private RealmList<RunSection> realmSectionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mode_layout);
        ButterKnife.bind(this);
        tristateIntensityButton.setOnToggleChanged(this);
        realmSectionList = new RealmList<>();
    }

    @Override
    public void onToggle(TriStateToggleButton.ToggleStatus toggleStatus,
                         boolean booleanToggleStatus, int toggleIntValue) {
        switch (toggleStatus) {
            case off:
                intensityTextView.setText(R.string.high);
                break;
            case mid:
                intensityTextView.setText(R.string.medium);
                break;
            case on:
                intensityTextView.setText(R.string.low);
                break;
            default:
                intensityTextView.setText(R.string.medium);
                break;
        }
    }

    @OnClick(R.id.intensity_duration_button)
    public void showDurationPicker(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        final ClickNumberPickerView minutesPicker =
                (ClickNumberPickerView) dialog.findViewById(R.id.minutes_picker);
        final ClickNumberPickerView secondsPicker =
                (ClickNumberPickerView) dialog.findViewById(R.id.seconds_picker);
        Button setButton = (Button) dialog.findViewById(R.id.set_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long minutesInMillis = (long) (minutesPicker.getValue() * 60000);
                long secondsInMillis = (long) (secondsPicker.getValue() * 1000);
                long sectionDurationInMillis = minutesInMillis + secondsInMillis;
                if (sectionDurationInMillis != 0) {
                    String durationProperStringFormat = String.format(Locale.US, "%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(sectionDurationInMillis),
                            TimeUnit.MILLISECONDS.toMinutes(sectionDurationInMillis)
                                    - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours
                                    (sectionDurationInMillis)),
                            TimeUnit.MILLISECONDS.toSeconds(sectionDurationInMillis)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                    (sectionDurationInMillis)));
                    intensityDurationButton.setText(durationProperStringFormat);
                }
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.add_run_section_icon)
    public void addRunSection(View view) {
        if (checkDuration()) {
            Snackbar.make(coordinatorLayout, R.string.request_section_duration, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            String sectionIntensity = getUserIntensity();
            long sectionDuration = getUserSectionDuration();
            RunSection runSection = new RunSection(sectionIntensity, sectionDuration);
            realmSectionList.add(runSection);
            addRunSectionToChart(sectionIntensity, sectionDuration / 1000);
        }

    }

    private boolean checkDuration() {
        return intensityDurationButton.getText().toString()
                .equals(getString(R.string.select_section_duration));
    }

    private String getUserIntensity() {
        String intensity;
        TriStateToggleButton.ToggleStatus toggleStatus = tristateIntensityButton.getToggleStatus();
        switch (toggleStatus) {
            case off:
                intensity = RunSection.HIGH;
                break;
            case mid:
                intensity = RunSection.MEDIUM;
                break;
            case on:
                intensity = RunSection.LOW;
                break;
            default:
                intensity = RunSection.MEDIUM;
                break;
        }
        return intensity;
    }

    private Long getUserSectionDuration() {
        String stringUserSectionDuration = intensityDurationButton.getText().toString();
        String[] splitDuration = stringUserSectionDuration.split(":");
        long secondsInMillis = Integer.parseInt(splitDuration[2]) * 1000;
        long minutesInMillis = Integer.parseInt(splitDuration[1]) * 60000;
        long hoursInMillis = Integer.parseInt(splitDuration[0]) * 3600000;
        return secondsInMillis + minutesInMillis + hoursInMillis;
    }

    private void addRunSectionToChart(String sectionIntensity, long sectionDurationInSeconds) {
        switch (sectionIntensity) {
            case RunSection.HIGH:
                pieChart.addPieSlice(new PieModel(getString(R.string.high), sectionDurationInSeconds,
                        ContextCompat.getColor(this, R.color.high_intensity)));
                break;
            case RunSection.MEDIUM:
                pieChart.addPieSlice(new PieModel(getString(R.string.medium), sectionDurationInSeconds,
                        ContextCompat.getColor(this, R.color.medium_intensity)));
                break;
            case RunSection.LOW:
                pieChart.addPieSlice(new PieModel(getString(R.string.low), sectionDurationInSeconds,
                        ContextCompat.getColor(this, R.color.low_intensity)));
                break;
            default:
                pieChart.addPieSlice(new PieModel(getString(R.string.medium), sectionDurationInSeconds,
                        ContextCompat.getColor(this, R.color.medium_intensity)));
                break;
        }
        pieChart.startAnimation();
    }

    @OnClick(R.id.add_mode_button)
    public void addModeToDatabase() {
        if (modeNameEditText.getText().length() == 0) {
            modeNameEditText.requestFocus();
            modeNameEditText.setError(getString(R.string.insert_mode_name));
        } else {
            RunningMode runningMode = new RunningMode(modeNameEditText.getText().toString(),
                    realmSectionList);
            RealmModeDatabase base = new RealmModeDatabase();
            base.saveOrUpdateRunningMode(runningMode);
            Intent intent = new Intent(this, ModeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}