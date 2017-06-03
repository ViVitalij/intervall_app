package interval.com.intervalapp.database;

import interval.com.intervalapp.model.RunningMode;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by m.losK on 2017-05-30.
 */

public class RealmModeDatabase {

    private Realm realm = Realm.getDefaultInstance();

    public void saveRunningMode(final RunningMode runningMode) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(runningMode);
            }
        });
    }

    public RunningMode readRunningMode(String modeName) {

        return realm.where(RunningMode.class).equalTo("name", modeName).findFirst();
    }

    public RealmResults<RunningMode> readAllModes() {
        return realm.where(RunningMode.class).findAll();

    }

    public RunningMode findByHash(int hash) {
        for (RunningMode s : realm.where(RunningMode.class).findAll()) {
            if (s.hashCode() == hash) {
                return s;
            }
        }
        return null;
    }
}
