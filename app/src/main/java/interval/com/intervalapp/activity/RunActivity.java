package interval.com.intervalapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.database.RealmSongsDataBase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunSection.Intensity;
import interval.com.intervalapp.model.Song;

public class RunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);
    }

    @OnCheckedChanged(R.id.run_button)
    protected void check(boolean isChecked) {
        if (isChecked) {
            //TODO change icon to pause

            Toast.makeText(this, "Start running now", Toast.LENGTH_LONG).show();

            //TODO be aware when resume running
            List<RunSection> fullRunModel = getFullRunModel();
            RunSection runSection = fullRunModel.get(0);
            RealmSongsDataBase realmSongsDataBase = new RealmSongsDataBase();
            List<Song> slowSongs = realmSongsDataBase.readSongList("slow");
            //TODO get music from realm and random play
            switch (runSection.getIntensity()) {
                case LOW:
                    Toast.makeText(this, "Slow section", Toast.LENGTH_LONG).show();
                    break;
                case MEDIUM:
                    Toast.makeText(this, "Medium section", Toast.LENGTH_LONG).show();
                    break;
                case HIGH:
                    Toast.makeText(this, "Fast section", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }


        } else {
            //TODO change icon to play

            Toast.makeText(this, "Pause", Toast.LENGTH_LONG).show();
        }
    }

    //TODO
    @OnClick(R.id.stop_button)
    protected void stopRunning() {
        //TODO change run_button icon to play

        Toast.makeText(this, "Stop", Toast.LENGTH_LONG).show();

    }

    //TODO move to service class
    private List<RunSection> getFullRunModel() {

        RunSection fast = new RunSection(Intensity.HIGH, 3000);
        RunSection slow = new RunSection(Intensity.LOW, 4000);
        RunSection medium = new RunSection(Intensity.MEDIUM, 2000);

        List<RunSection> tabata = new ArrayList<>();
        tabata.add(fast);
        tabata.add(slow);
        tabata.add(medium);

        return tabata;
    }
}
