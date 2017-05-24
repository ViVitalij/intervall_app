package interval.com.intervalapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}

