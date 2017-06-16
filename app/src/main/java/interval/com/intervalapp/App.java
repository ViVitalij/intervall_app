package interval.com.intervalapp;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import interval.com.intervalapp.database.RealmModeDatabase;
import interval.com.intervalapp.model.RunSection;
import interval.com.intervalapp.model.RunningMode;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class App extends MultiDexApplication {

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
        realmModeDatabase.saveOrUpdateRunningMode(tabata);
    }

    private RunningMode initTabataMode() {
        RealmList<RunSection> list = new RealmList<>();
        list.add(new RunSection(RunSection.HIGH, 5000L));
        list.add(new RunSection(RunSection.LOW, 10000L));
        list.add(new RunSection(RunSection.MEDIUM, 15000L));
        list.add(new RunSection(RunSection.HIGH, 15000L));
        list.add(new RunSection(RunSection.MEDIUM, 15000L));

        RunningMode runningMode = new RunningMode();
        runningMode.setRunMode(list);
        runningMode.setName("tabata");
        return runningMode;
    }
}
