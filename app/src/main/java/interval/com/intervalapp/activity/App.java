package interval.com.intervalapp.activity;

import android.app.Application;

import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;

import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunSection.Intensity;
import interval.com.intervalapp.model.RunningMode;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        addDefaultRunningModesToDatabase();
    }

    private void addDefaultRunningModesToDatabase() {
        RunningMode tabata = initTabataMode();

        RealmModeDatabase realmModeDatabase = new RealmModeDatabase();
        realmModeDatabase.saveRunningMode(tabata);
    }

    private RunningMode initTabataMode() {
        RunSection fast = new RunSection(Intensity.HIGH, Duration.millis(5000));
        RunSection slow = new RunSection(Intensity.LOW, Duration.millis(10000));
        RunSection medium = new RunSection(Intensity.MEDIUM, Duration.millis(15000));

        RunningMode runningMode = new RunningMode();
        List<RunSection> list = new ArrayList<>();
        list.add(fast);
        list.add(slow);
        list.add(medium);
        runningMode.setRunMode(list);
        runningMode.setName("tabata");
        return runningMode;
    }
}
