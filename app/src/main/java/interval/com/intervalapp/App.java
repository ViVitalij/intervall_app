package interval.com.intervalapp;

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
        RunningMode hiit = initHiitMode();
        RunningMode beginner = initBeginnerMode();

        RealmModeDatabase realmModeDatabase = new RealmModeDatabase();
        realmModeDatabase.saveOrUpdateRunningMode(tabata);
        realmModeDatabase.saveOrUpdateRunningMode(hiit);
        realmModeDatabase.saveOrUpdateRunningMode(beginner);
    }

    private RunningMode initBeginnerMode() {
        RealmList<RunSection> beginnerRunSectionList = new RealmList<>();
        beginnerRunSectionList.add(new RunSection(RunSection.HIGH, 4000L));
        beginnerRunSectionList.add(new RunSection(RunSection.LOW, 20000L));
        beginnerRunSectionList.add(new RunSection(RunSection.MEDIUM, 15000L));

        RunningMode runningMode = new RunningMode();
        runningMode.setRunSectionList(beginnerRunSectionList);
        runningMode.setName("beginner");
        return runningMode;
    }

    private RunningMode initHiitMode() {
        RealmList<RunSection> hiitRunSectionList = new RealmList<>();
        hiitRunSectionList.add(new RunSection(RunSection.HIGH, 5000L));
        hiitRunSectionList.add(new RunSection(RunSection.LOW, 10000L));
        hiitRunSectionList.add(new RunSection(RunSection.HIGH, 15000L));
        hiitRunSectionList.add(new RunSection(RunSection.MEDIUM, 15000L));
        hiitRunSectionList.add(new RunSection(RunSection.HIGH, 15000L));
        hiitRunSectionList.add(new RunSection(RunSection.MEDIUM, 15000L));
        hiitRunSectionList.add(new RunSection(RunSection.HIGH, 15000L));
        hiitRunSectionList.add(new RunSection(RunSection.MEDIUM, 15000L));
        hiitRunSectionList.add(new RunSection(RunSection.HIGH, 15000L));

        RunningMode runningMode = new RunningMode();
        runningMode.setRunSectionList(hiitRunSectionList);
        runningMode.setName("hiit");
        return runningMode;
    }

    private RunningMode initTabataMode() {
        RealmList<RunSection> tabataRunSectionList = new RealmList<>();
        tabataRunSectionList.add(new RunSection(RunSection.HIGH, 5000L));
        tabataRunSectionList.add(new RunSection(RunSection.LOW, 10000L));
        tabataRunSectionList.add(new RunSection(RunSection.MEDIUM, 15000L));
        tabataRunSectionList.add(new RunSection(RunSection.HIGH, 15000L));
        tabataRunSectionList.add(new RunSection(RunSection.MEDIUM, 15000L));

        RunningMode runningMode = new RunningMode();
        runningMode.setRunSectionList(tabataRunSectionList);
        runningMode.setName("tabata");
        return runningMode;
    }
}
