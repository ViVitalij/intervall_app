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

public class CreateModeActivity extends AppCompatActivity {
    @BindView(R.id.add_mode)
    protected Button addMode;
    @BindView(R.id.mode_name)
    protected EditText modeName;
    @BindView(R.id.delete_mode)
    protected Button deleteMode;

    @BindView(R.id.description)
    protected EditText description;
    @BindView(R.id.intensityTextView)
    protected TextView intensityText;
    @BindView(R.id.piechart)
    protected PieChart pieChart;


    private Realm realm = Realm.getDefaultInstance();

    static Dialog d;
    private RealmList<RunSection> sectionList;
    String intensityMode;
    Long duration;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mode_layout);
        ButterKnife.bind(this);
        toogleButton();
        sectionList = new RealmList<>();
    }


    @OnClick(R.id.time_picker)
    public void setTimePicker(View view) {
        showPicker();
    }

    @OnClick(R.id.add_mode)
    public void createRunSection() {
        createOneRunSection();
        createChart();
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

    public void createChart() {
        switch (intensityMode) {
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