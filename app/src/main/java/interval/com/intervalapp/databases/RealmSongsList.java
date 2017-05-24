package interval.com.intervalapp.databases;

import java.util.ArrayList;
import java.util.List;

import interval.com.intervalapp.models.SongsModel;
import io.realm.Realm;

/**
 * Created by RENT on 2017-05-24.
 */

public class RealmSongsList {
    private Realm realm = Realm.getDefaultInstance();

    public void createSpeedSongs(final List<SongsModel> allSongs) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                 realm.copyToRealm(allSongs);

            }
        });
    }
}
