package interval.com.intervalapp.activity;

import android.app.Application;

import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunningMode;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

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
        RunSection fast = new RunSection(RunSection.HIGH, 5000L);
        RunSection slow = new RunSection(RunSection.LOW, 10000L);
        RunSection medium = new RunSection(RunSection.MEDIUM, 15000L);
        RunSection medium2 = new RunSection(RunSection.MEDIUM, 15000L);
        RunSection medium3 = new RunSection(RunSection.MEDIUM, 15000L);

        RealmList<RunSection> list = new RealmList<>();
        list.add(fast);
        list.add(slow);
        list.add(medium);
        list.add(medium2);
        list.add(medium3);

        RunningMode runningMode = new RunningMode();
        runningMode.setRunMode(list);
        runningMode.setName("tabata");
        return runningMode;
    }
}
