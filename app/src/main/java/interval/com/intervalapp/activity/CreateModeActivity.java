/**
 * The MIT License (MIT)

 Copyright (c) 2016 Beppi Menozzi

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package interval.com.intervalapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

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

public class CreateModeActivity extends AppCompatActivity implements TriStateToggleButton.OnToggleChanged {

    @BindView(R.id.mode_name_edit_text)
    protected EditText modeNameEditText;

    @BindView(R.id.description_edit_text)
    protected EditText descriptionEditText;

    @BindView(R.id.intensity_text_view)
    protected TextView intensityTextView;

    @BindView(R.id.pie_chart)
    protected PieChart pieChart;

    @BindView(R.id.tristate_intensity_button)
    protected TriStateToggleButton intensityButton;

    private Dialog dialog;

    private RealmList<RunSection> realmSectionList;

    private String intensity;

    private Long duration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mode_layout);
        ButterKnife.bind(this);
        intensityButton.setOnToggleChanged(this);
        realmSectionList = new RealmList<>();
    }

    @Override
    public void onToggle(TriStateToggleButton.ToggleStatus toggleStatus,  boolean booleanToggleStatus,
                         int toggleIntValue) {
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
                intensityTextView.setText(R.string.high);
                break;
        }
    }

    @OnClick(R.id.intensity_duration_button)
    public void setTimePicker(View view) {
        showPicker();
    }


    @OnClick(R.id.add_mode_icon)
    public void createRunSection() {
        createOneRunSection();
        createChart();
    }


    @OnClick(R.id.add_mode_button)
    public void addNewMode() {
        if (modeNameEditText.getText().length() == 0) {
            modeNameEditText.requestFocus();
            modeNameEditText.setError("Insert mode name!");
        } else {
            RunningMode runningMode = new RunningMode(modeNameEditText.getText().toString(), realmSectionList);
            RealmModeDatabase base = new RealmModeDatabase();
            base.saveRunningMode(runningMode);
            Intent intent = new Intent(this, ModeActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void showPicker() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        final ClickNumberPickerView minutePicker = (ClickNumberPickerView) dialog.findViewById(R.id.minutePicker);
        final ClickNumberPickerView secondsPicker = (ClickNumberPickerView) dialog.findViewById(R.id.secondsPicker);
        Button button = (Button) dialog.findViewById(R.id.set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long minutesInMillis = (long) (minutePicker.getValue() * 60000);
                long secondsInMillis = (long) (secondsPicker.getValue() * 1000);
                duration = minutesInMillis + secondsInMillis;
                dialog.dismiss();
            }
        });
    }

    private void createOneRunSection() {
        RunSection section = new RunSection(intensity, duration);
        realmSectionList.add(section);
        Log.e("asd", String.valueOf(realmSectionList.size()));
    }

    private void createChart() {
        switch (intensity) {
            case RunSection.HIGH:
                pieChart.addPieSlice(new PieModel("High", 15, Color.parseColor("#CC1D1D")));
                break;
            case RunSection.MEDIUM:
                pieChart.addPieSlice(new PieModel("Medium", 25, Color.parseColor("#8EAE3C")));
                break;
            case RunSection.LOW:
                pieChart.addPieSlice(new PieModel("Low", 25, Color.parseColor("#FFFFFF")));
            default:
                break;
        }
        pieChart.startAnimation();
    }
}